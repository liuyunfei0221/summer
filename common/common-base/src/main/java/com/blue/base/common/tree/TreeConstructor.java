package com.blue.base.common.tree;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.blue.base.common.base.BlueChecker.isNotNull;
import static com.blue.base.common.base.BlueChecker.isNull;
import static java.util.stream.Collectors.toMap;

/**
 * tree constructor
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
@FunctionalInterface
public interface TreeConstructor<T> {

    /**
     * generate
     *
     * @param nodes
     * @return
     */
    List<TreeNode<T>> construct(final List<TreeNode<T>> nodes);

    /**
     * default generate
     *
     * @param nodes
     * @return
     */
    @SuppressWarnings("UnusedAssignment")
    default List<TreeNode<T>> defaultConstruct(final List<TreeNode<T>> nodes) {
        Map<Long, TreeNode<T>> mapping = nodes.parallelStream()
                .filter(Objects::nonNull)
                .collect(toMap(TreeNode::getIdentity, n -> n, (a, b) -> a));

        List<TreeNode<T>> result = new LinkedList<>();

        Long pIdentity;
        TreeNode<T> pNode;
        List<TreeNode<T>> pChildren;
        for (TreeNode<T> cNode : nodes) {
            if (isNotNull(pIdentity = cNode.getParentIdentity()) && isNotNull(pNode = mapping.get(pIdentity))) {
                if (isNull(pChildren = pNode.getChildren())) {
                    pChildren = new LinkedList<>();
                    pNode.setChildren(pChildren);
                }
                pChildren.add(cNode);
            } else {
                result.add(cNode);
            }
        }
        mapping = null;

        return result;
    }
}
