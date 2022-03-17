package test;

import com.blue.base.common.message.DictProcessor;
import com.blue.base.common.message.MessageProcessor;
import com.blue.base.constant.base.DictKey;

public class LanguageTest {

    public static void main(String[] args) {

        System.err.println(MessageProcessor.listSupportLanguages());
        System.err.println(MessageProcessor.resolveToMessage(200));
        System.err.println(DictProcessor.resolveToValue(DictKey.EMAIL.key));

    }

}
