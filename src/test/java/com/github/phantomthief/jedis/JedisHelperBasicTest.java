package com.github.phantomthief.jedis;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Stream.of;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.fppt.jedismock.RedisServer;
import com.github.phantomthief.jedis.OpInterceptor.JedisOpCall;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author w.vela
 * Created on 2020-05-29.
 */
class JedisHelperBasicTest {

    private static final Logger logger = LoggerFactory.getLogger(JedisHelperBasicTest.class);
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
    void testOpListener() {
        try (JedisPool jedisPool = new JedisPool(server.getHost(), server.getBindPort())) {
            Deque<String> ops = new ArrayDeque<>();
            JedisHelper<Jedis> helper = JedisHelper.newBuilder(() -> jedisPool)
                    .addOpListener((pool, requestTime, requestNanoTime, method, args, costInNano, t) -> {
                        ops.add(method.getName() + ":" + of(args).map(Object::toString).collect(joining(":")));
                    })
                    .build();
            helper.get().set("test", "test1");
            assertEquals("set:test:test1", ops.poll());
            assertEquals("test1", helper.get().get("test"));
            assertEquals("get:test", ops.poll());
        }
    }

    @Test
    void testOpInterceptor() {
        try (JedisPool jedisPool = new JedisPool(server.getHost(), server.getBindPort())) {
            Deque<String> ops = new ArrayDeque<>();
            JedisHelper<Jedis> helper = JedisHelper.newBuilder(() -> jedisPool)
                    .addOpInterceptor((pool, method, jedis, args) -> {
                        ops.add(method.getName() + ":" + of(args).map(Object::toString).collect(joining(":")));
                        return new JedisOpCall<>(method, jedis, args);
                    })
                    .build();
            helper.get().set("test", "test1");
            assertEquals("set:test:test1", ops.poll());
            assertEquals("test1", helper.get().get("test"));
            assertEquals("get:test", ops.poll());
        }
    }

    @Test
    void testPoolListener() {
        try (JedisPool jedisPool = new JedisPool(server.getHost(), server.getBindPort())) {
            AtomicInteger counter = new AtomicInteger();
            JedisHelper<Jedis> helper = JedisHelper.newBuilder(() -> jedisPool)
                    .addPoolListener((pool, borrowTime, borrowNanoTime, t) -> counter.incrementAndGet())
                    .build();
            assertEquals(0, counter.get());
            helper.get().set("test", "test1");
            assertEquals(1, counter.get());
            assertEquals("test1", helper.get().get("test"));
            assertEquals(2, counter.get());
        }
    }
}