package com.github.phantomthief.jedis;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author w.vela
 * Created on 2017-09-26.
 */
public interface PoolListener<P> {

    /**
     * @param borrowTime request timestamp in ms, NOT COST!
     */
    @Deprecated
    void onPoolBorrowed(@Nonnull P pool, long borrowTime, @Nullable Throwable t) throws Exception;

    default void onPoolBorrowed(@Nonnull P pool, long borrowTime, long borrowNanoTime, @Nullable Throwable t)
            throws Exception {
    }
}
