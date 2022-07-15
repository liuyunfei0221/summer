package com.blue.media.model;

import com.blue.basic.inter.Asserter;
import com.blue.basic.model.exps.BlueException;

import java.io.Serializable;
import java.util.Map;

import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;

/**
 * params for generate qr code
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public class QrCodeGenerateParam implements Serializable, Asserter {

    private static final long serialVersionUID = 1972556695742026462L;

    /**
     * unique qr code type
     */
    private Integer type;

    Map<String, String> elements;

    public QrCodeGenerateParam() {
    }

    public QrCodeGenerateParam(Integer type, Map<String, String> elements) {
        this.type = type;
        this.elements = elements;
    }

    @Override
    public void asserts() {
        if (isNull(this.type))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid type");
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Map<String, String> getElements() {
        return elements;
    }

    public void setElements(Map<String, String> elements) {
        this.elements = elements;
    }

    @Override
    public String toString() {
        return "QrCodeGenerateParam{" +
                "type=" + type +
                ", elements=" + elements +
                '}';
    }

}
