package com.github.phantomthief.jedis;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.annotation.Nullable;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.fppt.jedismock.RedisServer;
import com.github.phantomthief.jedis.exception.RethrowException;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.PipelineBase;

/**
 * @author w.vela
 * Created on 2020-05-29.
 */
class JedisHelperTest {

    private static final Logger logger = LoggerFactory.getLogger(JedisHelperTest.class);
    private static RedisServer server = null;

    @BeforeAll
    static void beforeAll() throws IOException {
        server = RedisServer.newRedisServer();  // bind to a random port
        server.start();
    }

    @AfterAll
    static void afterAll() {
        server.stop();
    }

    @Test
    void testAfterSyncRethrow() {
        boolean[] pipelineAfterSync = {false, false};
        try (JedisPool jedisPool = new JedisPool(server.getHost(), server.getBindPort())) {
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
        }
    }

    @Test
    void testOnStartedRethrow() {
        boolean[] onStarted = {false, false};
        try (JedisPool jedisPool = new JedisPool(server.getHost(), server.getBindPort())) {
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