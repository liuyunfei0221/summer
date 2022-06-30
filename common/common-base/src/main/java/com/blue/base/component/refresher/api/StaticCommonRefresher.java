package com.blue.base.component.refresher.api;

import com.blue.base.common.message.ElementProcessor;
import com.blue.base.common.message.MessageProcessor;

/**
 * static resources refresher
 *
 * @author liuyunfei
 */
public final class StaticCommonRefresher {

    /**
     * refresh static resources
     */
    public static void refresh() {
        MessageProcessor.refresh();
        ElementProcessor.refresh();
    }

}
