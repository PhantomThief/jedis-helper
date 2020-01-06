package com.github.phantomthief.jedis;

import java.lang.reflect.Method;

import javax.annotation.Nullable;

import redis.clients.jedis.PipelineBase;

/**
 * @author w.vela
 * Created on 2017-06-14.
 */
public interface PipelineOpListener<P, T> {

    /**
     * @throws Exception any exception would be catch except {@link com.github.phantomthief.jedis.exception.RethrowException}
     */
    @Nullable
    default T onPipelineStarted(@Nullable P pool) throws Exception {
        return null;
    }

    /**
     * @param pool {@code null} if there is no available pool.
     * @param obj the object return by {@link #onPipelineStarted}
     * @param requestTime pipeline request timestamp in ms, NOT COST!
     * @param pipelineResponse pipeline response holder, typically is {@link redis.clients.jedis.Response}
     */
    @Deprecated
    void onRequest(P pool, PipelineBase pipeline, @Nullable T obj, long requestTime, Method method,
            Object[] args, Object pipelineResponse) throws Exception;

    default void onRequest(P pool, PipelineBase pipeline, @Nullable T obj, long requestTime, long requestNanoTime,
            Method method, Object[] args, Object pipelineResponse) throws Exception {
    }

    /**
     * @param obj the object return by {@link #onPipelineStarted}
     */
    default void beforeSync(P pool, PipelineBase pipeline, @Nullable T obj) throws Exception {
    }

    /**
     * @param obj the object return by {@link #onPipelineStarted}
     * @throws Exception any exception would be catch except {@link com.github.phantomthief.jedis.exception.RethrowException}
     */
    default void afterSync(P pool, @Nullable T obj, @Nullable Throwable t) throws Exception {
    }
}
