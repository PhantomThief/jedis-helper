package com.github.phantomthief.jedis;

import static java.util.stream.Collectors.toSet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.fppt.jedismock.RedisServer;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ScanParams;

/**
 * @author w.vela
 * Created on 2020-05-29.
 */
class JedisHelperScanTest {

    private static RedisServer server = null;

    @BeforeEach
    void setUp() throws IOException {
        server = RedisServer.newRedisServer();  // bind to a random port
        server.start();
    }

    @AfterEach
    void tearDown() {
        server.stop();
    }

    @Test
    void test() {
        try (JedisPool jedisPool = new JedisPool(server.getHost(), server.getBindPort())) {
            JedisHelper<Jedis> helper = JedisHelper.newBuilder(() -> jedisPool)
                    .build();
            for (int i = 0; i < 100; i++) {
                helper.get().set(i + "", i + "");
            }
            Stream<String> scan = helper.scan(new ScanParams());
            Set<Integer> set = new HashSet<>();
            assertEquals(100L,
                    scan.map(it -> { // force map, prevent peek optimize by jdk9+
                        set.add(Integer.parseInt(it));
                        assertEquals(helper.get().get(it + ""), it + "");
                        return it;
                    }).count());
            assertEquals(100, set.size());
            for (int i = 0; i < 100; i++) {
                assertTrue(set.contains(i));
            }
        }
    }

    @Test
    void testSscan() {
        try (JedisPool jedisPool = new JedisPool(server.getHost(), server.getBindPort())) {
            JedisHelper<Jedis> helper = JedisHelper.newBuilder(() -> jedisPool)
                    .build();
            String sscanKey = "sscan_key";
            for (int i = 0; i < 100; i++) {
                helper.get().sadd(sscanKey, i + "");
            }
            Stream<String> scan = helper.sscan(sscanKey, new ScanParams());
            Set<String> set = scan.collect(toSet());
            assertEquals(100, set.size());
            for (int i = 0; i < 100; i++) {
                assertTrue(set.contains(i + ""));
            }
        }
    }
}
