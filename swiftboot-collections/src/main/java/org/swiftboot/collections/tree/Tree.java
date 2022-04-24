package org.swiftboot.collections.tree;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * Tree structure composed with {@link Node}s.
 * The tree model can be initiated from record set which has parent-child relationship.
 * The node without parent found supposed to be root node, but if more than one such node exist,
 * one new node will be created as root node, and tree <code>size()</size> will be +1.
 * HOWTO:
 * <ul>
 * <li>Implement an {@link Initiator} instance to load a tree structure form list or any type record set.</li>
 * <li>Invoke <code>tree.init()</code> to init tree structure with list of data and {@link Initiator} instance.</li>
 * <li>If data with no error, the Tree instance will has root node found, invoke <code>getRootNode()</code> access.</li>
 * </ul>
 *
 * @author swiftech
 * @see Node
 * @see Initiator
 * @since 2.2
 */
public class Tree {

    /**
     * How many levels this tree has.
     * TODO will be changed if add more nodes to lowest level.
     */
    private int levels;

    /**
     * Root node of the tree， this root node must be created by init() methods.
     */
    private Node rootNode;

    /**
     * Keep all unique elements reference here which has been added to tree structure,
     * to be easy to lookup.
     * Key: Data object
     * Value: Node object
     */
    private final Map<Object, Node> allUniqueElements = new HashMap<>();

    /**
     * Init the tree with root object.
     *
     * @param rootObject
     */
    public void init(Object rootObject) {
        this.rootNode = new Node(rootObject);
    }

    /**
     * Init tree by tracking nodes from child to parent, until the root node.
     * If multiple root node found, a new root node will be created.
     *
     * @param treeItems
     * @param initiator    Call-back method to initiate from element list.
     */
    public <T> void init(List<T> treeItems, Initiator<T> initiator) {
        for (T element : treeItems) {
            System.out.println("Data element: " + element);

            // create new node if not exist(check by data object)
            Node newNode = null;
            if (allUniqueElements.get(element) == null) {
                newNode = new Node(element);
                allUniqueElements.put(element, newNode);
            }
            else {
                newNode = allUniqueElements.get(element);
            }

            Object parentItem = initiator.findParent(treeItems, element);
            System.out.println("Find parent : " + parentItem);

            // Found root node which has no parent
            if (parentItem == null) {

                // Found and supposed to be root node.
                if (rootNode == null) {
                    rootNode = newNode;
                    System.out.println("Root Node Probably Found.");
                }
                else {// Found more than one root node.
                    // Root node already determined, just set with it as root.
                    if (rootNode.isAutoCreated) {
                        newNode.setParent(rootNode);
                    }
                    else {
                        // create a node as new root node
                        Object defaultRoot = initiator.defaultRoot();
                        if (defaultRoot == null) {
                            throw new NotImplementedException(String.format("Implement %s to provide default root", Initiator.class.getName()));
                        }
                        if (!allUniqueElements.containsKey(defaultRoot)) {
                            Node newRootNode = new Node(defaultRoot);
                            newRootNode.isAutoCreated = true;
                            rootNode.setParent(newRootNode);
                            rootNode = newRootNode;

                            allUniqueElements.put(defaultRoot, newRootNode);
                            System.out.println("Default Root Node Created: " + rootNode);
                            newNode.setParent(rootNode);
                        }
                    }
                }
            }
            else {
                Node parentNode;

                // Parent node already constructed, just share parent node.
                if (allUniqueElements.containsKey(parentItem)) {
                    parentNode = allUniqueElements.get(parentItem);
                }
                else {
                    parentNode = new Node(parentItem);
                    allUniqueElements.put(parentItem, parentNode);
                }
                newNode.setParent(parentNode);
            }
        }

        // Init level for all node by starting from root node(which level is default 0).
        for (Iterator<Node> it = this.DFSIterator(); it.hasNext(); ) {
            Node node = it.next();
            System.out.println("NODE: " + node);
            if (node == rootNode) {
                continue;
            }
            node.setLevel(node.getParent().getLevel() + 1);
            // BTW, set the total levels.
            if (levels <= node.getLevel()) {
                levels = node.getLevel() + 1;
            }
        }

        System.out.println("Init complete.");
    }

    /**
     * Init tree with root node.
     *
     * @param rootNode
     */
    public void init(Node rootNode){
        this.rootNode = rootNode;
    }

    public Node getRootNode() {
        return rootNode;
    }

    public int size() {
        return allUniqueElements.size();
    }

    public int getLevels() {
        return levels;
    }

    /**
     * Get Deep-first Search iterator to visit all tree nodes by DFS.
     *
     * @return
     */
    public Iterator<Node> DFSIterator() {
        return new DFSIterator(rootNode);
    }

    public Iterator<Node> BFSIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        if (rootNode == null) {
            return "Tree[]";
        }
        StringBuilder buf = new StringBuilder();
        for (Iterator<Node> it = this.DFSIterator(); it.hasNext(); ) {
            Node node = it.next();
            buf.append(StringUtils.repeat("  ", node.getLevel())).append("|-").append(node).append("\r\n");
        }
        return buf.toString();
    }

    /**
     * Deep-first Search iterator implementation.
     * This iterator uses stack to go deep in tree and trace-back.
     */
    private class DFSIterator implements Iterator<Node> {
        int cursor = 0;

        Stack<Iterator<Node>> stack = new Stack<>();

        public DFSIterator(Node rootNode) {
            if (rootNode == null) {
                throw new IllegalArgumentException("Root node is required");
            }
            List<Node> list = new ArrayList<>();
            list.add(rootNode);

            // Init stack with root node(as iterator)
            stack.push(list.iterator());
        }

        public boolean hasNext() {
            if (stack.empty()) {
                System.out.println("No more nodes can be iterate in the tree.");
                if (cursor < allUniqueElements.size()) {
                    throw new IllegalStateException(
                            String.format("%d nodes iterated but still more nodes: %d", cursor, allUniqueElements.size()));
                }
                return false;
            }
            Iterator<Node> it = stack.peek();
            if (it != null && it.hasNext()) {
                return true;
            }
            else {
                // All of this level in sub-tree visited, up to last level.
                // (put here because hasNext() invokes before next())
                stack.pop();
                return hasNext();
            }
        }

        public Node next() {
            Iterator<Node> it = stack.peek();
            Node currentNode = null;
            if (it.hasNext()) {
                currentNode = it.next();

                // Down to next level if possible.
                if (!currentNode.getChildren().isEmpty()) {
                    List<Node> children = currentNode.getChildren();
                    stack.push(children.iterator());
                }
            }
            else {
                return null;
            }
            cursor++;
            return currentNode;
        }

        public void remove() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

    }
}
