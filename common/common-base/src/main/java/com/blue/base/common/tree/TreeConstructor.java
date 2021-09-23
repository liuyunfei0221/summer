package com.blue.base.common.tree;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.toMap;

/**
 * 树构建
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused"})
@FunctionalInterface
public interface TreeConstructor<T> {

    /**
     * 约束
     *
     * @param nodes
     * @return
     */
    List<TreeNode<T>> construct(final List<TreeNode<T>> nodes);

    /**
     * 默认实现
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
            if ((pIdentity = cNode.getParentIdentity()) != null && (pNode = mapping.get(pIdentity)) != null) {
                if ((pChildren = pNode.getChildren()) == null) {
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
