package com.zst.week8.q2.utils;

import java.util.concurrent.atomic.AtomicInteger;

public class IDGenerator {
    static final AtomicInteger suffix = new AtomicInteger(0);
    static final int SUFFIX_LENGTH  = 16;

    public static long get() {
        return (System.currentTimeMillis() << SUFFIX_LENGTH)
                | suffix.incrementAndGet() & -1 >>> (32 - SUFFIX_LENGTH);
    }
}
