package test;

import com.blue.base.common.message.MessageProcessor;
import com.blue.base.constant.base.DictKey;

import static com.blue.base.constant.base.ResponseElement.INVALID_EMAIL_ADDRESS;
import static java.util.Collections.singletonList;

public class LanguageTest {

    public static void main(String[] args) {
        System.err.println(MessageProcessor.listSupportLanguages());

        System.err.println(MessageProcessor.resolveToMessage(INVALID_EMAIL_ADDRESS.code, singletonList("en-US"), new DictKey[]{DictKey.DEFAULT}));
    }

}
