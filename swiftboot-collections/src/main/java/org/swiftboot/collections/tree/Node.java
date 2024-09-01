package org.swiftboot.collections.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Node in Tree with data object reference, parent Node reference, node level(depth) in tree and children list.
 *
 * @author swiftech
 * @see Tree
 * @since 2.2
 */
public class Node {
    // Start from 0, which means root node.
    private int level;
    private Node parent;
    private List<Node> children = new ArrayList<>();
    private Object data;

    /**
     * means this node is auto created, internal use only.
     */
    boolean isAutoCreated = false;

    public Node(Object data) {
        this.data = data;
    }

    public void addChild(Node child) {
        if (child == null) {
            throw new NullPointerException();
        }
        children.add(child);
    }

    public Node findAncestor(Predicate<Node> predicate) {
        if (this.parent == null) {
            return this;// stop at the top.
        }
        if (predicate.test((this.parent))) {
            return this.parent;
        }
        return this.parent.findAncestor(predicate);
    }

    public boolean isLeaf() {
        return children.isEmpty();
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
        if (!parent.containsChild(this)) {
            parent.addChild(this);
        }
    }

    public boolean containsChild(Node node) {
        return this.children.contains(node);
    }

    @Override
    public String toString() {
        return String.format("Node{level=%d, data=%s}", level, data);
    }
}
