package com.github.phantomthief.jedis;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;

import javax.annotation.Nullable;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.phantomthief.jedis.exception.RethrowException;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.PipelineBase;

/**
 * @author w.vela
 * Created on 2020-05-29.
 */
class JedisHelperTest extends BaseJedisTest {

    private static final Logger logger = LoggerFactory.getLogger(JedisHelperTest.class);
    @Test
    void testAfterSyncRethrow() {
        boolean[] pipelineAfterSync = {false, false};
        try (JedisPool jedisPool = getPool()) {
            JedisHelper<Jedis> helper = JedisHelper.newBuilder(() -> jedisPool)
                    .addPipelineOpListener(new PipelineOpListener<JedisPool, Object>() {
                        @Override
                        public void onRequest(JedisPool pool, PipelineBase pipeline, @Nullable Object obj,
                                long requestTime, long requestNanoTime, Method method, Object[] args,
                                Object pipelineResponse) {
                        }

                        @Override
                        public void afterSync(JedisPool pool, @Nullable Object obj, @Nullable Throwable t)
                                throws Exception {
                            pipelineAfterSync[0] = true;
                            logger.info("try re-throw new exception.1");
                            throw new RethrowException(new RuntimeException("test1"));
                        }
                    })
                    .addPipelineOpListener(new PipelineOpListener<JedisPool, Object>() {
                        @Override
                        public void onRequest(JedisPool pool, PipelineBase pipeline, @Nullable Object obj,
                                long requestTime, long requestNanoTime, Method method, Object[] args,
                                Object pipelineResponse) {
                        }

                        @Override
                        public void afterSync(JedisPool pool, @Nullable Object obj, @Nullable Throwable t)
                                throws Exception {
                            pipelineAfterSync[1] = true;
                            logger.info("try re-throw new exception.2");
                            throw new RethrowException(new RuntimeException("test2"));
                        }
                    })
                    .build();
            RuntimeException e = assertThrows(RuntimeException.class, () -> {
                helper.pipeline(p -> {
                    p.hget("test", "test");
                });
            });
            assertEquals("test1", e.getMessage());
            assertTrue(pipelineAfterSync[0]);
            assertTrue(pipelineAfterSync[1]);

            assertThrows(RuntimeException.class, () -> {
                helper.binaryPipeline(p -> {
                    p.hget("test".getBytes(), "test".getBytes());
                });
            });
        }
    }

    @Test
    void testOnStartedRethrow() {
        boolean[] onStarted = {false, false};
        try (JedisPool jedisPool = getPool()) {
            JedisHelper<Jedis> helper = JedisHelper.newBuilder(() -> jedisPool)
                    .addPipelineOpListener(new PipelineOpListener<JedisPool, Object>() {
                        @Override
                        public void onRequest(JedisPool pool, PipelineBase pipeline, @Nullable Object obj,
                                long requestTime, long requestNanoTime, Method method, Object[] args,
                                Object pipelineResponse) {
                        }

                        @Nullable
                        @Override
                        public Object onPipelineStarted(@Nullable JedisPool pool) throws Exception {
                            onStarted[0] = true;
                            logger.info("try re-throw new exception.1");
                            throw new RethrowException(new RuntimeException("test1"));
                        }
                    })
                    .addPipelineOpListener(new PipelineOpListener<JedisPool, Object>() {
                        @Override
                        public void onRequest(JedisPool pool, PipelineBase pipeline, @Nullable Object obj,
                                long requestTime, long requestNanoTime, Method method, Object[] args,
                                Object pipelineResponse) {
                        }

                        @Nullable
                        @Override
                        public Object onPipelineStarted(@Nullable JedisPool pool) throws Exception {
                            onStarted[1] = true;
                            logger.info("try re-throw new exception.2");
                            throw new RethrowException(new RuntimeException("test2"));
                        }
                    })
                    .build();
            RuntimeException e = assertThrows(RuntimeException.class, () -> {
                helper.pipeline(p -> {
                    p.hget("test", "test");
                });
            });
            assertEquals("test1", e.getMessage());
            assertTrue(onStarted[0]);
            assertTrue(onStarted[1]);
        }
    }
}