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
package co.paralleluniverse.galaxy.netty;

import co.paralleluniverse.common.collection.Util;
import co.paralleluniverse.galaxy.core.Message;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author pron
 */
public class MessagePacket implements Iterable<Message>, Cloneable {
    private int size;
    private int numBuffers;
    private transient boolean multicast;
    private transient long timestamp;
    private ArrayList<Message> messages = new ArrayList<Message>();

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void addMessage(Message message) {
        assert messages.size() < 256;
        messages.add(message);
        size += message.size();
        numBuffers += 1 + message.getNumDataBuffers();
    }

    public boolean removeMessage(Message m) {
        int index = messages.indexOf(m);
        Message message = messages.get(index); // m != message. often m will be the response to message
        if (index >= 0) {
            messages.remove(index);
            messageRemoved(message);
            return true;
        } else
            return false;
    }

    public boolean removeMessage(long id) {
        for (Iterator<Message> it = messages.iterator(); it.hasNext();) {
            final Message message = it.next();
            if (message.getMessageId() != id)
                continue;

            it.remove();
            messageRemoved(message);
            return true;
        }
        return false;
    }

    private void messageRemoved(Message message) {
        size -= message.size();
        numBuffers -= 1 + message.getNumDataBuffers();
    }

    public Message getMessage(Message m) {
        int index = messages.indexOf(m);
        if(index < 0)
            return null;
        Message message = messages.get(index); // m != message. often m will be the response to message
        return message;
    }

    public boolean contains(Message m) {
        return messages.contains(m);
    }

    public boolean contains(long id) {
        for (Iterator<Message> it = messages.iterator(); it.hasNext();) {
            final Message message = it.next();
            if (message.getMessageId() != id)
                continue;

            return true;
        }
        return false;
    }

    public int numMessages() {
        return messages.size();
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public boolean isMulticast() {
        return multicast;
    }

    public void setMulticast() {
        this.multicast = true;
    }

    @Override
    public Iterator<Message> iterator() {
        return wrapIterator(messages.iterator());
    }

    public Iterator<Message> reverseIterator() {
        return wrapIterator(Util.reverse(messages).iterator());
    }

    private Iterator<Message> wrapIterator(final Iterator<Message> it) {
        return new Iterator<Message>() {
            private Message message;

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public Message next() {
                message = it.next();
                return message;
            }

            @Override
            public void remove() {
                it.remove();
                messageRemoved(message);
            }
        };
    }

    public boolean isEmpty() {
        return messages.isEmpty();
    }

    public int sizeInBytes() {
        return size;
    }

    public int getNumBuffers() {
        return numBuffers;
    }

    public ByteBuffer[] toByteBuffers() {
        ByteBuffer[] buffers = new ByteBuffer[numBuffers];
        int i = 0;
        for (Message message : messages) {
            ByteBuffer[] bs = message.toByteBuffers();
            for (ByteBuffer b : bs) {
                b.rewind();
                buffers[i] = b;
                i++;
            }
        }
        return buffers;
    }

    public void fromByteBuffer(ByteBuffer buffer) {
        while (buffer.hasRemaining())
            addMessage(Message.fromByteBuffer(buffer));
    }

    public short getNode() {
        return messages.iterator().next().getNode();
    }

    public void setNode(short node) {
        for (Message m : messages)
            m.setNode(node);
    }

    @Override
    public String toString() {
        return "MessagePacket[" + messages + ']';
    }

    @Override
    public MessagePacket clone() {
        try {
            final MessagePacket clone = (MessagePacket) super.clone();
            clone.messages = (ArrayList<Message>) messages.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }
}
