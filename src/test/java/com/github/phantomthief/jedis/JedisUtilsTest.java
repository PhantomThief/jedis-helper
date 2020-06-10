package com.github.phantomthief.jedis;

import static com.github.phantomthief.jedis.JedisUtils.syncSortedSetKey;
import static com.github.phantomthief.jedis.JedisUtils.toBitSet;
import static com.github.phantomthief.jedis.JedisUtils.toIntSet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.carrotsearch.hppc.IntSet;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.embedded.RedisServer;

/**
 * @author w.vela
 * Created on 2020-06-09.
 */
class JedisUtilsTest {

    private static final int PORT = ThreadLocalRandom.current().nextInt(1024, 2048);
    private static RedisServer server = null;

    @BeforeAll
    static void beforeAll() {
        server = new RedisServer(PORT);
        server.start();
    }

    @AfterAll
    static void afterAll() {
        server.stop();
    }

    @Test
    void test() {
        // 这个测试用例只有真实redis-server能通过
        try (JedisPool jedisPool = new JedisPool("localhost", PORT)) {
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
    void testSync() {
        try (JedisPool jedisPool = new JedisPool("localhost", PORT)) {
            JedisHelper<Jedis> helper = JedisHelper.newBuilder(() -> jedisPool)
                    .build();
            Map<String, Double> map = new HashMap<>();
            for (int i = 0; i < 10; i++) {
                map.put(i + "", (double) i);
            }
            assertFalse(syncSortedSetKey(helper.get(), "testzset", it -> it, it -> map));
            helper.get().zadd("testzset", 1, "a");
            assertTrue(syncSortedSetKey(helper.get(), "testzset", it -> it, it -> map));
            assertFalse(syncSortedSetKey(helper.get(), "testzset", it -> it, it -> map));
            Set<String> test = helper.get().zrevrange("testzset", 0, 100);
            assertEquals(10, test.size());
        }
    }
}