package com.github.phantomthief.jedis;

import java.lang.reflect.Method;

/**
 * @author w.vela
 * Created on 2017-06-14.
 */
public interface PipelineOpListener<P> {

    /**
     * @param pool {@code null} if there is no available pool.
     * @param requestTime pipeline request timestamp in ms, NOT COST!
     */
    void onRequest(P pool, long requestTime, Method method, Object[] args) throws Exception;
}
