package com.blue.database.api.conf;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import java.util.List;

/**
 * @author liuyunfei
 * @date 2021/9/10
 * @apiNote
 */
@SuppressWarnings("unused")
public class TransConfParams implements TransConf {

    protected Isolation isolation;

    protected Propagation propagation;

    protected List<String> methodPreWithTrans;

    protected Integer transTimeout;

    protected String pointCutExpression;

    public TransConfParams() {
    }

    public TransConfParams(Isolation isolation, Propagation propagation, List<String> methodPreWithTrans, Integer transTimeout, String pointCutExpression) {
        this.isolation = isolation;
        this.propagation = propagation;
        this.methodPreWithTrans = methodPreWithTrans;
        this.transTimeout = transTimeout;
        this.pointCutExpression = pointCutExpression;
    }

    @Override
    public Isolation getIsolation() {
        return isolation;
    }

    @Override
    public Propagation getPropagation() {
        return propagation;
    }

    @Override
    public List<String> getMethodPreWithTrans() {
        return methodPreWithTrans;
    }

    @Override
    public Integer getTransTimeout() {
        return transTimeout;
    }

    @Override
    public String getPointCutExpression() {
        return pointCutExpression;
    }

    public void setIsolation(Isolation isolation) {
        this.isolation = isolation;
    }

    public void setPropagation(Propagation propagation) {
        this.propagation = propagation;
    }

    public void setMethodPreWithTrans(List<String> methodPreWithTrans) {
        this.methodPreWithTrans = methodPreWithTrans;
    }

    public void setTransTimeout(Integer transTimeout) {
        this.transTimeout = transTimeout;
    }

    public void setPointCutExpression(String pointCutExpression) {
        this.pointCutExpression = pointCutExpression;
    }

    @Override
    public String toString() {
        return "TransConfParams{" +
                "isolation=" + isolation +
                ", propagation=" + propagation +
                ", methodPreWithTrans=" + methodPreWithTrans +
                ", transTimeout=" + transTimeout +
                ", pointCutExpression='" + pointCutExpression + '\'' +
                '}';
    }

}
