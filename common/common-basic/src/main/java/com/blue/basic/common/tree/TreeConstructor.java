package com.blue.basic.common.tree;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.blue.basic.common.base.BlueChecker.isNotNull;
import static com.blue.basic.common.base.BlueChecker.isNull;
import static java.util.function.Function.identity;
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
                .collect(toMap(TreeNode::getIdentity, identity(), (a, b) -> a));

        List<TreeNode<T>> roots = new LinkedList<>();

        Long pIdentity;
        TreeNode<T> pNode;
        List<TreeNode<T>> pChildren;

        for (TreeNode<T> cNode : nodes) {
            if (isNotNull(pIdentity = cNode.getParentIdentity()) && isNotNull(pNode = mapping.get(pIdentity))) {
                if (isNull(pChildren = pNode.getChildren()))
                    pNode.setChildren(pChildren = new LinkedList<>());

                pChildren.add(cNode);
            } else {
                roots.add(cNode);
            }
        }

        mapping = null;

        return roots;
    }
}
