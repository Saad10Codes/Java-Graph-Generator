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
package co.paralleluniverse.galaxy.cluster;

import co.paralleluniverse.common.collection.LinkedHashMap2;
import co.paralleluniverse.galaxy.cluster.DistributedTree.Listener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helps deal with a branch of a distributed tree, when we want to consider the nodes only when they have given sub-nodes ("required properties").
 * <p/>
 * If the underlying nodes are ordered and the helper is constructed with the ordered option, the handlers are called in the order the main nodes
 * (the branch's children) were originally created, not the order in which they've become "completed" (their required properties satisfied)! <b>Important:</b> Assumes that once required properties are
 * set they are never removed or unset (set to null), though they can be changed.
 *
 * @author pron
 */
public abstract class DistributedBranchHelper {
    private static final Logger LOG = LoggerFactory.getLogger(DistributedBranchHelper.class);
    private final DistributedTree tree;
    private final String branchRoot;
    private final boolean ordered;
    private final LinkedHashMap2<String, Node> nodes = new LinkedHashMap2<String, Node>();
    private final List<Listener> listeners = new CopyOnWriteArrayList<Listener>();
    private boolean doneInit = false;

    public DistributedBranchHelper(DistributedTree tree, final String branchRoot, boolean ordered) {
        if (!branchRoot.startsWith("/"))
            throw new IllegalArgumentException("Branch root must start with a '/', but is " + branchRoot);
        this.tree = tree;
        this.branchRoot = branchRoot;
        this.ordered = ordered;

        tree.addListener(branchRoot, new Listener() {
            @Override
            public void nodeChildAdded(String node, String childName) {
                assert node.equals(branchRoot);
                DistributedBranchHelper.this.nodeAdded(childName);
            }

            @Override
            public void nodeChildDeleted(String node, String childName) {
                assert node.equals(branchRoot);
                DistributedBranchHelper.this.nodeRemoved(childName);
            }

            @Override
            public void nodeChildUpdated(String node, String childName) {
                assert node.equals(branchRoot);
                DistributedBranchHelper.this.nodeUpdated(childName);
            }

            @Override
            public void nodeUpdated(String node) {
                assert node.equals(branchRoot);
                for (Listener listener : listeners)
                    listener.nodeUpdated(node);
            }

            @Override
            public void nodeAdded(String node) {
                assert node.equals(branchRoot);
                for (Listener listener : listeners)
                    listener.nodeAdded(node);
            }

            @Override
            public void nodeDeleted(String node) {
                assert node.equals(branchRoot);
                for (Listener listener : listeners)
                    listener.nodeDeleted(node);
            }
        });

        synchronized (nodes) {
            final List<String> children = tree.getChildren(branchRoot);
            if (children != null) {
                for (String nodeName : children)
                    nodeAdded(nodeName);
            }

            if (ordered) {
                final Node first = getFirstNode();
                if (first != null)
                    first.predecessorsComplete();
            }
            doneInit = true;
        }
    }

    public void addListener(Listener listener) {
        synchronized (nodes) {
            for (Node node : nodes.values()) {
                if (!node.isComplete())
                    break;
                listener.nodeChildAdded(branchRoot, node.name);
            }
            listeners.add(listener);
        }
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    public List<String> getChildren() {
        final List<String> children = new ArrayList<String>();
        synchronized (nodes) {
            for (Node node : nodes.values()) {
                if (!node.isComplete())
                    break;
                children.add(node.name);
            }
        }
        return Collections.unmodifiableList(children);
    }

    private void nodeAdded(String name) {
        synchronized (nodes) {
            // by re-inserting existent nodes, we take care of race conditions between the handler and the getChildren call in the constructor
            // we make sure that the order is that of getChildren

            Node node = nodes.remove(name);
            if (node == null)
                node = new Node(name);
            nodes.put(name, node);

            LOG.debug("Node {} added.", name);

            if (ordered) {
                final Node previous = getPreviousNode(name);
                if ((previous == null && doneInit) || (previous != null && previous.isComplete()))
                    node.predecessorsComplete();
            }
        }
    }

    private void nodeCompleted(String name) {
        LOG.debug("Node {} completed", name);
        for (Listener listener : listeners)
            listener.nodeChildAdded(branchRoot, name);

        if (ordered) {
            synchronized (nodes) {
                final Node next = getNextNode(name);
                if (next != null)
                    next.predecessorsComplete(); // possibly sets off recursion
            }
        }
    }

    private void nodeUpdated(String name) {
        final Node node;
        synchronized (nodes) {
            node = nodes.get(name);
        }
        assert node != null;
        LOG.debug("Node {} updated. ({})", name, node.isComplete() ? "complete" : "incomplete");
        if (node.isComplete()) {
            for (Listener listener : listeners)
                listener.nodeChildUpdated(branchRoot, name);
        }
    }

    private void nodeRemoved(String name) {
        boolean _doneInit;
        final Node node;
        Node previous = null, next = null;
        synchronized (nodes) {
            if (ordered) {
                previous = nodes.previousValue(name);
                next = nodes.nextValue(name);
            }
            node = nodes.remove(name);
            _doneInit = this.doneInit;
        }
        if (node.isComplete()) {
            for (Listener listener : listeners)
                listener.nodeChildDeleted(branchRoot, name);
        }
        if (ordered) {
            if (_doneInit && next != null && (previous == null || previous.isComplete()))
                next.predecessorsComplete();
        }
    }

    private Node getNextNode(String node) {
        assert ordered;
        synchronized (nodes) {
            final Map.Entry<String, Node> entry = nodes.nextEntry(node);
            return (entry != null ? entry.getValue() : null);
        }
    }

    private Node getPreviousNode(String node) {
        assert ordered;
        synchronized (nodes) {
            final Map.Entry<String, Node> entry = nodes.previousEntry(node);
            return (entry != null ? entry.getValue() : null);
        }
    }

    private Node getFirstNode() {
        assert ordered;
        synchronized (nodes) {
            final Map.Entry<String, Node> entry = nodes.firstEntry();
            return (entry != null ? entry.getValue() : null);
        }
    }

    protected abstract boolean isNodeComplete(String node, Set<String> properties);

    private class Node extends DistributedTree.ListenerAdapter {
        final String name;
        private final String treePath;
        private final Set<String> properties = new HashSet<String>();
        private boolean predecessorsComplete;
        private boolean complete = false;

        public Node(String name) {
            this.name = name;
            this.treePath = branchRoot + '/' + name;
            this.predecessorsComplete = !ordered;
            tree.addListener(treePath, this);
            for (String property : tree.getChildren(treePath))
                testProperty(property);
        }

        public synchronized boolean isComplete() {
            return complete;
        }

        public void predecessorsComplete() {
            assert ordered;
            synchronized (this) {
                if (predecessorsComplete)
                    return;
                predecessorsComplete = true;
            }
            testComplete();
        }

        private void testProperty(String property) {
            LOG.debug("Test property for {}: {}", name, property);
            synchronized (this) {
                if (tree.get(treePath + '/' + property) == null)
                    return;
                properties.add(property);
            }
            testComplete();
        }

        private void testComplete() {
            synchronized (this) {
                if (complete)
                    return;
                if (!predecessorsComplete || !isNodeComplete(treePath, properties))
                    return;
                complete = true;
                tree.removeListener(treePath, this);
                LOG.debug("Node {} is now complete!", name);
            }
            nodeCompleted(name); // must be called outside the sync block because it syncs on nodes - might cause deadlock
        }

        @Override
        public void nodeChildAdded(String node, String childName) {
            nodeChildUpdated(node, childName); // possibly wasteful. consider.
        }

        @Override
        public void nodeChildUpdated(String node, String childName) {
            assert node.equals(treePath);
            testProperty(childName);
        }
    }
}
