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
     * @param borrowNanoTime request moment's System.nanoTime(), NOT COST!
     */
    void onPoolBorrowed(@Nonnull P pool, long borrowTime, long borrowNanoTime, @Nullable Throwable t) throws Exception;
}
