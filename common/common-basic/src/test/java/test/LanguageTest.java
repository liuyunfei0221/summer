package test;

import com.blue.basic.common.message.MessageProcessor;
import com.blue.basic.constant.common.ElementKey;

import static com.blue.basic.constant.common.ResponseElement.INVALID_EMAIL_ADDRESS;
import static java.util.Collections.singletonList;

public class LanguageTest {

    public static void main(String[] args) {
        System.err.println(MessageProcessor.supportLanguages());

        System.err.println(MessageProcessor.resolveToMessage(INVALID_EMAIL_ADDRESS.code, singletonList("en-US"), new ElementKey[]{ElementKey.DEFAULT}));
    }

}
