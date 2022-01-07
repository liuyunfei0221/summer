package com.blue.identity.core;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Padding bits are used to prevent false sharing
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "JavaDoc"})
public final class IdentityAtomicBoolean extends AtomicBoolean {

    private static final long serialVersionUID = 5201477379823890713L;

    public volatile long p1, p2, p3, p4, p5, p6 = 7L;

    public IdentityAtomicBoolean() {
        super();
    }

    public IdentityAtomicBoolean(boolean initialValue) {
        super(initialValue);
    }

    /**
     * prevent GC recycling
     *
     * @return
     */
    public long doNotRecycling() {
        return p1 & p2 & p3 & p4 & p5 & p6;
    }

}