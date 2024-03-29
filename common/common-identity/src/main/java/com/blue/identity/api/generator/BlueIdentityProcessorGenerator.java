package com.blue.identity.api.generator;

import com.blue.identity.api.conf.IdentityConf;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.identity.core.exp.IdentityException;
import org.slf4j.Logger;

import static com.blue.basic.common.base.BlueChecker.isNull;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * database components generator
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
public final class BlueIdentityProcessorGenerator {

    private static final Logger LOGGER = getLogger(BlueIdentityProcessorGenerator.class);

    /**
     * generate identity processor
     *
     * @param identityConf
     * @return
     */
    public static BlueIdentityProcessor generateBlueIdentityProcessor(IdentityConf identityConf) {
        LOGGER.info("identityConf = {}", identityConf);
        if (isNull(identityConf))
            throw new IdentityException("identityConf can't be null");

        return new BlueIdentityProcessor(identityConf);
    }

}
