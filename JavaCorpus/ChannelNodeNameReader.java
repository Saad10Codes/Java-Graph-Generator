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

import co.paralleluniverse.galaxy.Cluster;
import co.paralleluniverse.galaxy.cluster.NodeInfo;
import com.google.common.base.Charsets;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author pron
 */
@ChannelHandler.Sharable
class ChannelNodeNameReader extends SimpleChannelUpstreamHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ChannelNodeNameReader.class);
    private final Cluster cluster;
    private volatile ChannelStateEvent connectEvent;
    private volatile String nodeName;
    private volatile NodeInfo node;

    public ChannelNodeNameReader(Cluster cluster) {
        this.cluster = cluster;
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        final ChannelBuffer message = (ChannelBuffer) e.getMessage();
        final int size = message.readableBytes();
        final byte[] array = new byte[size];
        message.readBytes(array);
        this.nodeName = new String(array, Charsets.UTF_8);
        LOG.info("Channel {} is node {}.", ctx.getChannel(), nodeName);
        this.node = cluster.getNodeInfoByName(nodeName);
        if (node == null) {
            LOG.error("Node info for {} not found!", ctx.getChannel(), nodeName);
            throw new RuntimeException("No node info for channel");
        }
        final SocketAddress address = ctx.getChannel().getRemoteAddress();
        final InetAddress host = ((InetSocketAddress) address).getAddress();
        if (!host.equals((InetAddress) node.get(IpConstants.IP_ADDRESS))) {
            LOG.error("Channel coming from {} claims to be node {} - host address mismatch!", address, node);
            throw new RuntimeException("Node identity problem!");
        }

        LOG.info("Channel {} is {}.", ctx.getChannel(), node);
        ChannelNodeInfo.nodeInfo.set(ctx.getChannel(), node);

        assert connectEvent != null;
        ctx.sendUpstream(connectEvent);

        ctx.getPipeline().remove(this);
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        this.connectEvent = e; // hold connect event until we get the node's name
    }

    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        node = null;
        super.channelClosed(ctx, e);
    }

    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        node = null;
        super.channelDisconnected(ctx, e);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        node = null;
        super.exceptionCaught(ctx, e);
    }
}
