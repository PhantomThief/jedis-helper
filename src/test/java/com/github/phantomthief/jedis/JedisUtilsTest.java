package com.github.phantomthief.jedis;

import static com.github.phantomthief.jedis.JedisUtils.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.carrotsearch.hppc.IntSet;
import com.github.fppt.jedismock.RedisServer;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author w.vela
 * Created on 2020-06-09.
 */
class JedisUtilsTest {

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

    @Disabled
    @Test
    void test() {
        // 这个测试用例只有真实redis-server能通过
        try (JedisPool jedisPool = new JedisPool(server.getHost(), server.getBindPort())) {
            JedisHelper<Jedis> helper = JedisHelper.newBuilder(() -> jedisPool)
                    .build();
            helper.get().setbit("test", 100, true);
            byte[] bytes = helper.getBinary().get("test".getBytes());
            BitSet bitSet = toBitSet(bytes);
            assertFalse(bitSet.get(0));
            assertTrue(bitSet.get(100));

            IntSet intSet = toIntSet(bytes);
            assertFalse(intSet.contains(0));
            assertTrue(intSet.contains(100));
        }
    }

    @Test
    void testTemp() {
        // 这个测试用例只有真实redis-server能通过
        try (JedisPool jedisPool = new JedisPool(server.getHost(), server.getBindPort())) {
            JedisHelper<Jedis> helper = JedisHelper.newBuilder(() -> jedisPool)
                    .build();
            helper.get().setbit("test", 100, true);
            byte[] bytes = helper.getBinary().get("test".getBytes());
            toBitSet(bytes);
            toIntSet(bytes);
        }
    }

    @Disabled
    @Test
    void testSync() {
        try (JedisPool jedisPool = new JedisPool(server.getHost(), server.getBindPort())) {
            JedisHelper<Jedis> helper = JedisHelper.newBuilder(() -> jedisPool)
                    .build();
            Map<String, Double> map = new HashMap<>();
            for (int i = 0; i < 10; i++) {
                map.put(i + "", (double) i);
            }
            assertFalse(syncSortedSetKey(helper.get(), "test", it -> it, it -> map));
            helper.get().zadd("test", 1, "a");
            assertTrue(syncSortedSetKey(helper.get(), "test", it -> it, it -> map));
        }
    }
}