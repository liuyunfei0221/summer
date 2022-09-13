package com.blue.basic.component.message.api.loader;

import com.blue.basic.component.message.api.conf.MessageConf;

import static com.blue.basic.common.base.BlueChecker.isBlank;
import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.common.message.InternationalProcessor.loadElement;
import static com.blue.basic.common.message.InternationalProcessor.loadMessage;

/**
 * message loader
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "JavadocDeclaration"})
public final class MessageLoader {

    /**
     * refresh static resources
     */
    public static void load(MessageConf messageConf) {
        confAssert(messageConf);

        loadMessage(messageConf.getMessageLocation());
        loadElement(messageConf.getElementLocation());
    }

    /**
     * assert conf
     *
     * @param messageConf
     */
    private static void confAssert(MessageConf messageConf) {
        if (isNull(messageConf))
            throw new RuntimeException("messageConf can't be null");

        if (isBlank(messageConf.getMessageLocation()))
            throw new RuntimeException("messageLocation can't be blank");
        if (isBlank(messageConf.getElementLocation()))
            throw new RuntimeException("elementLocation can't be blank");
    }

}
