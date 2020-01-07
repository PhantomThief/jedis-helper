package com.github.phantomthief.jedis;

import static java.lang.System.currentTimeMillis;
import static java.lang.System.nanoTime;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.PipelineBase;

/**
 * @author w.vela
 * Created on 2017-06-14.
 */
class PipelineListenerHandler<P extends PipelineBase> implements InvocationHandler {

    private static final Logger logger = LoggerFactory.getLogger(PipelineListenerHandler.class);
    private final Object pool;
    private final P pipeline;
    private final List<PipelineOpListener<Object, Object>> listeners;
    private final Map<PipelineOpListener<Object, Object>, Object> startPipeline;

    PipelineListenerHandler(Object pool, P pipeline,
            List<PipelineOpListener<Object, Object>> listeners,
            Map<PipelineOpListener<Object, Object>, Object> startPipeline) {
        this.pool = pool;
        this.startPipeline = startPipeline;
        this.pipeline = pipeline;
        this.listeners = listeners;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object invoke = method.invoke(pipeline, args);
        long requestTime = currentTimeMillis();
        for (PipelineOpListener<Object, Object> opListener : listeners) {
            try {
                Object context = startPipeline.get(opListener);
                opListener.onRequest(pool, pipeline, context, requestTime, nanoTime(), method, args, invoke);
            } catch (Throwable e) {
                logger.error("", e);
            }
        }
        return invoke;
    }
}
