
package org.swiftboot.collections.tree;

import java.util.List;

/**
 * @author swiftech
 * @since
 */
public class MyInitiator implements Initiator<NodeData> {

    public static final String DEFAULT_ROOT_NAME = "Root";

    @Override
    public NodeData defaultRoot() {
        return new NodeData(0, 0, DEFAULT_ROOT_NAME);
    }

    public NodeData findParent(List<NodeData> allElement, NodeData current) {
        for (NodeData nodeData : allElement) {
            if (current.getPid() == nodeData.getId()) {
                return nodeData;
            }
        }
        return null;
    }

}
