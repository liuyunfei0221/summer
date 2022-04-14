package com.blue.base.common.tree;

import com.blue.base.model.exps.BlueException;

import java.io.Serializable;
import java.util.List;

import static com.blue.base.constant.base.ResponseElement.*;

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
        if (identity == null)
            throw new BlueException(INVALID_IDENTITY);
    }

    private static <T> void checkData(T data) {
        if (data == null)
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
        if (identity != null)
            hash = hash + (((int) (long) identity) << 5);
        if (parentIdentity != null)
            hash = hash + (((int) (long) parentIdentity) << 5);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || this.getClass() != obj.getClass())
            return false;

        TreeNode<T> other;

        //noinspection unchecked
        if (this.identity == null || (other = (TreeNode<T>) obj).getIdentity() == null)
            return false;
        if (this.parentIdentity == null || other.getParentIdentity() == null)
            return false;

        T otherData;
        return data != null && (otherData = other.getData()) != null && data.equals(otherData);
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