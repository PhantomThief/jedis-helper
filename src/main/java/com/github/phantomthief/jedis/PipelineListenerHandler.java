package com.github.phantomthief.jedis;

import static java.lang.System.currentTimeMillis;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author w.vela <wangtianzhou@kuaishou.com>
 * Created on 2017-06-14.
 */
class PipelineListenerHandler<P> implements InvocationHandler {

    private static final Logger logger = LoggerFactory.getLogger(PipelineListenerHandler.class);
    private final Object pool;
    private final P pipeline;
    private final List<PipelineOpListener<Object>> listeners;

    PipelineListenerHandler(Object pool, P pipeline, List<PipelineOpListener<Object>> listeners) {
        this.pool = pool;
        this.pipeline = pipeline;
        this.listeners = listeners;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object invoke = method.invoke(pipeline, args);
        for (PipelineOpListener<Object> opListener : listeners) {
            try {
                opListener.onRequest(pool, currentTimeMillis(), method, args);
            } catch (Throwable e) {
                logger.error("", e);
            }
        }
        return invoke;
    }
}
