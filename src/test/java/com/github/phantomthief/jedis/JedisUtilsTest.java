package com.github.phantomthief.jedis;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.BitSet;

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
@Disabled
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

    @Test
    void test() {
        // 这个测试用例只有真实redis-server能通过
        try (JedisPool jedisPool = new JedisPool(server.getHost(), server.getBindPort())) {
            JedisHelper<Jedis> helper = JedisHelper.newBuilder(() -> jedisPool)
                    .build();
            helper.get().setbit("test", 100, true);
            byte[] bytes = helper.getBinary().get("test".getBytes());
            System.out.println(Arrays.toString(bytes));
            BitSet bitSet = JedisUtils.toBitSet(bytes);
            assertFalse(bitSet.get(0));
            assertTrue(bitSet.get(100));

            IntSet intSet = JedisUtils.toIntSet(bytes);
            assertFalse(intSet.contains(0));
            assertTrue(intSet.contains(100));
        }
    }
}