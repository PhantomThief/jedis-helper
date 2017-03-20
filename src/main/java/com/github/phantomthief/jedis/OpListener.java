package com.github.phantomthief.jedis;

import java.lang.reflect.Method;

/**
 * @author w.vela <wangtianzhou@kuaishou.com>
 * Created on 2017-03-02.
 */
public interface OpListener<P> {

    /**
     * @param pool {@code null} if there is no available pool.
     * @param requestTime request timestamp in ms, NOT COST!
     * @param method {@code null} if it's a pipeline op
     * @param args {@code null} if it's a pipeline op
     * @param t {@link com.github.phantomthief.jedis.exception.NoAvailablePoolException} if there is no available pool
     */
    void onComplete(P pool, long requestTime, Method method, Object[] args, long costInMs,
            Throwable t) throws Exception;
}
