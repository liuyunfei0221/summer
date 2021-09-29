package com.blue.identity.constant;

import static com.blue.identity.constant.SnowflakeBits.DATA_CENTER;
import static com.blue.identity.constant.SnowflakeBits.WORKER;

/**
 * identity schema
 *
 * @author DarkBlue
 */
public enum IdentitySchema {

    /**
     * Maximum ID of the data center (the upper limit that the sub-database cannot exceed)
     */
    MAX_DATA_CENTER_ID(~(-1 << DATA_CENTER.len)),

    /**
     * Maximum machine identification (upper limit that the sub-table cannot exceed)
     */
    MAX_WORKER_ID(~(-1 << WORKER.len));

    public int max;

    IdentitySchema(int max) {
        this.max = max;
    }

}
