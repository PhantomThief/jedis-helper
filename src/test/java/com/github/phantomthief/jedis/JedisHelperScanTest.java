package com.github.phantomthief.jedis;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.fppt.jedismock.RedisServer;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ScanParams;

/**
 * @author w.vela
 * Created on 2020-05-29.
 */
class JedisHelperScanTest {

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
}
