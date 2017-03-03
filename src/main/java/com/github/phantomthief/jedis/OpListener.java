package com.github.phantomthief.jedis;

import java.lang.reflect.Method;

/**
 * @author w.vela <wangtianzhou@kuaishou.com>
 * Created on 2017-03-02.
 */
public interface OpListener<P> {

    void onSuccess(P pool, long requestTime, Method method, Object[] args) throws Exception;
}
