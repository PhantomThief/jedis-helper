package com.github.phantomthief.jedis;

import java.lang.reflect.Method;

import javax.annotation.Nullable;

/**
 * @author w.vela
 * Created on 2017-06-14.
 */
public interface PipelineOpListener<P, T> {

    @Nullable
    default T onPipelineStarted(P pool) throws Exception {
        return null;
    }

    /**
     * @param pool {@code null} if there is no available pool.
     * @param obj the object return by {@link #onPipelineStarted}
     * @param requestTime pipeline request timestamp in ms, NOT COST!
     */
    void onRequest(P pool, @Nullable T obj, long requestTime, Method method, Object[] args)
            throws Exception;

    /**
     * @param obj the object return by {@link #onPipelineStarted}
     */
    default void afterSync(P pool, @Nullable T obj, @Nullable Throwable t) throws Exception {
    }
}
