/*
 * Galaxy
 * Copyright (C) 2012 Parallel Universe Software Co.
 * 
 * This file is part of Galaxy.
 *
 * Galaxy is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as 
 * published by the Free Software Foundation, either version 3 of 
 * the License, or (at your option) any later version.
 *
 * Galaxy is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public 
 * License along with Galaxy. If not, see <http://www.gnu.org/licenses/>.
 */
package co.paralleluniverse.galaxy.jgroups;

import co.paralleluniverse.common.collection.ConcurrentMultimap;
import static co.paralleluniverse.common.collection.Util.reverse;
import co.paralleluniverse.galaxy.Cluster;
import co.paralleluniverse.galaxy.core.AbstractComm;
import co.paralleluniverse.galaxy.core.Comm;
import co.paralleluniverse.galaxy.core.Message;
import co.paralleluniverse.galaxy.core.Message.LineMessage;
import co.paralleluniverse.galaxy.core.MessageReceiver;
import co.paralleluniverse.galaxy.core.NodeNotFoundException;
import gnu.trove.set.hash.TShortHashSet;
import java.beans.ConstructorProperties;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import org.jgroups.Address;
import org.jgroups.ReceiverAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author pron
 */
class JGroupsComm extends AbstractComm<Address> {
    private static final Logger LOG = LoggerFactory.getLogger(JGroupsComm.class);
    private final Channel channel;
    private final Comm serverComm;
    private final ConcurrentMultimap<Short, Message, Deque<Message>> pendingReply = new ConcurrentMultimap<Short, Message, Deque<Message>>(new ArrayDeque<Message>(0)) {
        @Override
        protected Deque<Message> allocateElement() {
            return new ConcurrentLinkedDeque<Message>();
        }
    };
    private ConcurrentMap<Long, BroadcastEntry> pendingBroadcasts;

    @ConstructorProperties({"name", "cluster", "serverComm"})
    public JGroupsComm(String name, Cluster cluster, Comm serverComm) {
        super(name, cluster, new JGroupsNodeAddressResolver(cluster));
        this.channel = getCluster().getDataChannel();
        this.serverComm = serverComm;
        channel.setReceiver(new ReceiverAdapter() {
            @Override
            public void receive(org.jgroups.Message msg) {
                JGroupsComm.this.receive(msg);
            }
        });
        this.sendToServerInsteadOfMulticast = (serverComm != null); // this is just the default
    }

    @Override
    public void setReceiver(MessageReceiver receiver) {
        super.setReceiver(receiver);
        if (serverComm != null)
            serverComm.setReceiver(receiver);
    }

    @Override
    public void init() throws Exception {
        super.init();
        if (sendToServerInsteadOfMulticast && serverComm == null)
            throw new RuntimeException("sendToServerInsteadOfBroadcast is set to true but no serverComm set");
    }

    @Override
    public void postInit() throws Exception {
        if (!sendToServerInsteadOfMulticast)
            this.pendingBroadcasts = new ConcurrentHashMap<Long, BroadcastEntry>();
        super.postInit();
    }

    @Override
    protected void start(boolean master) {
        final long timeoutNano = TimeUnit.NANOSECONDS.convert(getTimeout(), TimeUnit.MILLISECONDS);
        getScheduler().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                final long now = System.nanoTime();

                if (pendingBroadcasts != null) {
                    for (BroadcastEntry entry : pendingBroadcasts.values()) {
                        final Message message = entry.message;
                        if (message.getType() != Message.Type.INVACK && now - message.getTimestamp() > timeoutNano) {
                            if (pendingBroadcasts.remove(message.getMessageId()) != null) {
                                LOG.debug("Timeout on message {}", message);
                                receive(Message.TIMEOUT((LineMessage) message).setIncoming());
                            }
                        }
                    }
                }
                for (Deque<Message> pending : pendingReply.values()) {
                    for (Message message : reverse(pending)) {
                        if (message.getType() != Message.Type.INVACK && now - message.getTimestamp() > timeoutNano) {
                            if (pending.removeLastOccurrence(message)) {// we're using this instead of iterators to safeguard against the case that a reply just arrives
                                LOG.debug("Timeout on message {}", message);
                                receive(Message.TIMEOUT((LineMessage) message).setIncoming());
                            }
                        } else
                            break; // the rest are younger b/c/ new messages are appended to the head.
                    }
                }
                if (hasPendingBroadcasts()) { // flush broadcasts (necessary because of NACKACK)
                    try {
                        channel.send(new org.jgroups.Message(null, new byte[0]));
                    } catch (Exception ex) {
                        LOG.error("Error while broadcasting flush.", ex);
                    }
                }
            }
        }, 0, getTimeout() / 2, TimeUnit.MILLISECONDS);
        setReady(true);
    }

    @Override
    public final JGroupsCluster getCluster() {
        return (JGroupsCluster) super.getCluster();
    }

    protected boolean hasPendingBroadcasts() {
        return !pendingBroadcasts.isEmpty();
    }

    protected boolean addToPending(Message message, short node) {
        if (!message.getType().isOf(Message.Type.REQUIRES_RESPONSE)) {
            LOG.debug("Message {} does not require a response.", message);
            return true;
        }

        if (node >= 0) {
            if (LOG.isDebugEnabled())
                LOG.debug("Enqueing message in pending-replies {}", message);
            pendingReply.getOrAllocate(node).addFirst(message);
        } else {
            assert message.isBroadcast();
            assert message instanceof LineMessage;
            final Set<Short> nodes = getCluster().getNodes();
            if (message instanceof LineMessage) {
                if (nodes.isEmpty() || (nodes.size() == 1 && nodes.contains(Comm.SERVER))) {
                    LOG.debug("No other nodes in cluster. Responding with NOT_FOUND to message {}", message);
                    receive(Message.NOT_FOUND((LineMessage) message).setIncoming());
                    return false;
                } else {
                    pendingBroadcasts.put(message.getMessageId(), new BroadcastEntry((LineMessage) message, nodes));
                    return true;
                }
            }
        }
        return true;
    }

    @Override
    protected void sendToNode(Message message, short node, Address address) {
        assignMessageId(message);
        addToPending(message, node);
        try {
            if (LOG.isDebugEnabled())
                LOG.debug("Sending to node {} ({}): {}", new Object[]{node, address, message});
            channel.send(new org.jgroups.Message(address, message.toByteArray()));
        } catch (Exception ex) {
            LOG.error("Error while sending message " + message + " to node " + node, ex);
        }
    }

    @Override
    protected void sendToServer(Message message) {
        super.sendToServer(message);
        try {
            serverComm.send(message);
        } catch (NodeNotFoundException e) {
            throw new RuntimeException("Server not found!", e);
        }
    }

    @Override
    protected void broadcast(Message message) {
        assignMessageId(message);
        if (addToPending(message, (short) -1))
            broadcast(message);
        try {
            LOG.debug("Broadcasting (null): {}", message);
            channel.send(new org.jgroups.Message(null, message.toByteArray()));
        } catch (Exception ex) {
            LOG.error("Error while broadcasting message " + message, ex);
        }
    }

    private void receive(org.jgroups.Message msg) {
        try {
            LOG.debug("Received {}", msg);
            if (getCluster().getMyAddress() != null && msg.getSrc() != null && getCluster().getMyAddress().equals(msg.getSrc()))
                return; // discard own (cannot set the flag because it screws up th control channel. not much to do about it - annoing up handler in JChannel)
            final byte[] buffer = msg.getRawBuffer();
            if (buffer.length == 0)
                return; // probably just a flush
            final Message message = Message.fromByteArray(buffer);
            final Address source = msg.getSrc();

            if (message.isResponse()) {
                final Deque<Message> pending = pendingReply.get(message.getNode());

                if (pending != null) {
                    boolean res = pending.removeLastOccurrence(message); // relies on Message.equals that matches request/reply
                    if (res)
                        LOG.debug("Message {} is a reply! (removing from pending)", message);
                }
            }

            if (message.isResponse()) {
                final BroadcastEntry entry = pendingBroadcasts.get(message.getMessageId());
                if (entry != null) {
                    if (message.getType() != Message.Type.ACK) {// this is a response - no need to wait for further acks
                        LOG.debug("Message {} is a reply to a broadcast! (discarding pending)", message);
                        pendingBroadcasts.remove(message.getMessageId());
                    } else
                        removeFromPendingBroadcasts(message.getMessageId(), message.getNode());
                }
            }

            final short sourceNode = getNode(source);
            if (sourceNode < 0)
                throw new RuntimeException("Node not found for source address " + source);
            message.setNode(sourceNode);
            receive(message);
        } catch (Exception ex) {
            LOG.error("Error receiving message", ex);
        }
    }

    @Override
    public void nodeAdded(short id) {
        super.nodeAdded(id);
        try {
            for (Message message : reverse(pendingReply.get(id)))
                sendToNode(message, id);
        } catch (NodeNotFoundException e) {
            throw new AssertionError();
        }
    }

    @Override
    public void nodeSwitched(short id) {
        super.nodeSwitched(id);
        try {
            for (Message message : reverse(pendingReply.get(id)))
                sendToNode(message, id);
            for (BroadcastEntry entry : pendingBroadcasts.values())
                sendToNode(entry.message, id);
        } catch (NodeNotFoundException e) {
            throw new AssertionError();
        }
    }

    @Override
    public void nodeRemoved(short id) {
        super.nodeRemoved(id);
        pendingReply.remove(id);
        for (Long messageId : pendingBroadcasts.keySet())
            removeFromPendingBroadcasts(messageId, id);
    }

    private void removeFromPendingBroadcasts(long messageId, short node) {
        final BroadcastEntry entry = pendingBroadcasts.get(messageId);
        if (LOG.isDebugEnabled())
            LOG.debug("Got ACK from {} to message {}", node, entry.message);
        if (entry.removeNode(node)) {
            LOG.debug("Got all ACKs for message {}, but no response - sending NOT_FOUND to cache!", entry.message);
            receive(Message.NOT_FOUND(entry.message).setIncoming());
            pendingBroadcasts.remove(messageId);
        }
    }

    private static class BroadcastEntry {
        final LineMessage message;
        final TShortHashSet nodes;

        public BroadcastEntry(LineMessage message, Set<Short> nodes) {
            this.message = message;
            this.nodes = new TShortHashSet(nodes);
            this.nodes.remove(Comm.SERVER); // NOT TO SERVER
            LOG.debug("Awaiting ACKS for message {} from nodes {}", message, this.nodes);
        }

        public synchronized void addNode(short node) {
            nodes.add(node);
        }

        public synchronized boolean removeNode(short node) {
            nodes.remove(node);
            return nodes.isEmpty();
        }
    }
}
