package com.github.phantomthief.jedis;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Stream.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import com.github.phantomthief.jedis.OpInterceptor.JedisOpCall;
import com.github.phantomthief.jedis.exception.NoAvailablePoolException;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * @author w.vela
 * Created on 2020-05-29.
 */
class JedisHelperBasicTest extends BaseJedisTest{

    @Test
    void testOpListener() {
        try (JedisPool jedisPool = getPool()) {
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

            assertTrue(helper.getBasic().info().contains("redis_version"));
        }
    }

    @Test
    void testShardedOpListener() {
        try (ShardedJedisPool jedisPool = getShardedPool()) {
            Deque<String> ops = new ArrayDeque<>();
            JedisHelper<ShardedJedis> helper = JedisHelper.newShardedBuilder(() -> jedisPool)
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
        try (JedisPool jedisPool = getPool()) {
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
        try (JedisPool jedisPool = getPool()) {
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

    @Test
    void testNoAvailableNode() {
        Throwable[] e = {null};
        JedisHelper<Jedis> helper = JedisHelper.newBuilder(() -> null)
                .addOpListener((pool, requestTime, requestNanoTime, method, args, costInNano, t) -> e[0] = t)
                .build();
        assertThrows(NoAvailablePoolException.class, () -> helper.get().get("1"));
        assertSame(NoAvailablePoolException.class, e[0].getClass());
    }

    @Test
    void testPoolFailed() throws InterruptedException {
        CountDownLatch latch1 = new CountDownLatch(1);
        Thread thread = new Thread(() -> {
            try (JedisPool jedisPool = getPool()) {
                latch1.countDown();
                jedisPool.getResource(); // borrow
            }
        });
        thread.start();
        latch1.await();
        Throwable[] e = {null};
        try (JedisPool jedisPool = getPool()) {
            JedisHelper<Jedis> helper = JedisHelper.newBuilder(() -> jedisPool)
                    .addPoolListener((pool, borrowTime, borrowNanoTime, t) -> e[0] = t)
                    .build();
            assertThrows(JedisConnectionException.class, () -> helper.get().get("test"));
            assertSame(JedisConnectionException.class, e[0].getClass());
        }
    }
}