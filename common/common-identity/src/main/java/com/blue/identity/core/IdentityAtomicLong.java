/*
 * Copyright (c) 2017 Baidu, Inc. All Rights Reserve.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.blue.identity.core;

import java.util.concurrent.atomic.AtomicLong;


/**
 * Padding bits are used to prevent false sharing
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "JavaDoc"})
public final class IdentityAtomicLong extends AtomicLong {

    private static final long serialVersionUID = -3415778863941386253L;

    public volatile long p1, p2, p3, p4, p5, p6 = 7L;

    public IdentityAtomicLong() {
        super();
    }

    public IdentityAtomicLong(long initialValue) {
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