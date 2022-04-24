package org.swiftboot.collections.tree;

import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author swiftech
 */
public class TreeTest {

    public TreeTest() {
    }

    @BeforeAll
    public static void setUpClass() throws Exception {
    }

    @AfterAll
    public static void tearDownClass() throws Exception {
    }

    @BeforeEach
    public void setUp() {
        System.out.println("setup()");
    }

    @AfterEach
    public void tearDown() {
        System.out.println("tearDown()");
    }

    @Test
    public void testRootOnly() {
        List<NodeData> list = new ArrayList<>();
        list.add(new NodeData(1, 0, "root"));
        Tree tree = new Tree();
        tree.init(list, new MyInitiator());
        for (Iterator<Node> it = tree.DFSIterator(); it.hasNext(); ) {
            Node node = it.next();
            System.out.println("# " + node.getData());
        }
    }

    @Test
    public void testNormal() {
        List<NodeData> list = new ArrayList<>();
        list.add(new NodeData(10, 4, "1-1-1"));
        list.add(new NodeData(11, 4, "1-1-2"));
        list.add(new NodeData(2, 5, "1-2-1"));
        list.add(new NodeData(3, 5, "1-2-2"));
        list.add(new NodeData(4, 1, "1-1"));
        list.add(new NodeData(5, 1, "1-2"));
        list.add(new NodeData(1, 0, "1-root"));


        Tree tree = new Tree();
        tree.init(list, new MyInitiator());

        Assertions.assertTrue(tree.getRootNode() != null);
        Assertions.assertEquals(list.size(), tree.size());
        Assertions.assertEquals(3, tree.getLevels());

        System.out.println("TREE: \r\n" + tree.toString());
    }

    @Test
    public void testMultiRootItem() {
        List<NodeData> list = new ArrayList<>();
        list.add(new NodeData(10, 4, "1-1-1"));
        list.add(new NodeData(11, 4, "1-1-2"));
        list.add(new NodeData(2, 5, "1-2-1"));
        list.add(new NodeData(3, 5, "1-2-2"));
        list.add(new NodeData(4, 1, "1-1"));
        list.add(new NodeData(5, 1, "1-2"));
        list.add(new NodeData(1, 0, "1-root"));
        list.add(new NodeData(99, 0, "1-root_"));
        list.add(new NodeData(999, 0, "1-root__"));
        Tree tree = new Tree();
        tree.init(list, new MyInitiator());

        System.out.println("TREE: \r\n" + tree.toString());

        Assertions.assertTrue(tree.getRootNode() != null);
        Assertions.assertEquals(list.size() + 1, tree.size());
        Assertions.assertEquals(3 + 1, tree.getLevels());
    }

    @Test
    public void testPolymorphism() {
        Tree tree = new Tree();
        tree.init(new Node(new NodeData(0, 0, "Root")));
        Node rootNode = tree.getRootNode();
        Node subNode = new Node(new SubNodeData(1, 0, "Sub"));
        rootNode.addChild(subNode);
    }

}
