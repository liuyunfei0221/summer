package com.blue.basic.common.tree;

import com.blue.basic.model.exps.BlueException;

import java.io.Serializable;
import java.util.List;

import static com.blue.basic.common.base.BlueChecker.isNotNull;
import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.constant.common.ResponseElement.*;

/**
 * tree node
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "WeakerAccess", "AliControlFlowStatementWithoutBraces"})
public final class TreeNode<T> implements Serializable {

    private static final long serialVersionUID = 2588759405716451873L;

    /**
     * id
     */
    private Long identity;

    /**
     * parent id
     */
    private Long parentIdentity;

    /**
     * element
     */
    private T data;

    /**
     * children node
     */
    private List<TreeNode<T>> children;


    private static void checkIdentity(Long identity) {
        if (isNull(identity))
            throw new BlueException(INVALID_IDENTITY);
    }

    private static <T> void checkData(T data) {
        if (isNull(data))
            throw new BlueException(EMPTY_PARAM);
    }

    public TreeNode() {
    }

    public TreeNode(Long identity, Long parentIdentity, T data) {
        checkIdentity(identity);
        checkData(data);
        this.identity = identity;
        this.parentIdentity = parentIdentity;
        this.data = data;
    }

    public TreeNode(Long identity, Long parentIdentity, T data, List<TreeNode<T>> children) {
        checkIdentity(identity);
        checkData(data);
        this.identity = identity;
        this.parentIdentity = parentIdentity;
        this.data = data;
        this.children = children;
    }

    public Long getIdentity() {
        return identity;
    }

    public void setIdentity(Long identity) {
        checkIdentity(identity);
        this.identity = identity;
    }

    public Long getParentIdentity() {
        return parentIdentity;
    }

    public void setParentIdentity(Long parentIdentity) {
        this.parentIdentity = parentIdentity;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<TreeNode<T>> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode<T>> children) {
        this.children = children;
    }

    @Override
    public int hashCode() {
        int hash = 5381;
        if (isNotNull(identity))
            hash = hash ^ ((int) ((long) identity) << 5);
        if (isNotNull(parentIdentity))
            hash = hash ^ ((int) ((long) parentIdentity) << 5);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (isNull(obj) || this.getClass() != obj.getClass())
            return false;

        TreeNode<T> other;

        //noinspection unchecked
        if (isNull(this.identity) || isNull((other = (TreeNode<T>) obj).getIdentity()))
            return false;
        if (isNull(this.parentIdentity) || isNull(other.getParentIdentity()))
            return false;

        T otherData;
        return isNotNull(data) && isNotNull((otherData = other.getData())) && data.equals(otherData);
    }

    @Override
    public String toString() {
        return "TreeNode{" +
                "identity=" + identity +
                ", parentIdentity=" + parentIdentity +
                ", data=" + data +
                ", children=" + children +
                '}';
    }

}