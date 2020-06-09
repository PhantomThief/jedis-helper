package com.github.phantomthief.jedis;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.fppt.jedismock.RedisServer;
import com.google.common.collect.ImmutableList;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author w.vela
 * Created on 2020-06-09.
 */
class JedisHelperShardBitTest {

    private static final Logger logger = LoggerFactory.getLogger(JedisHelperShardBitTest.class);
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
            List<Integer> list = ImmutableList.of(100, 120);
            helper.pipeline(list, (p, i) -> p.setbit("test", i, true));
            list.forEach(it -> {
                assertTrue(helper.get().getbit("test", it));
            });

            int keyHashRange = 5;
            for (long i = 0; i < 10; i++) {
                if (i % 3 == 0) {
                    assertFalse(helper.setShardBit(i, "test", keyHashRange, true));
                    assertTrue(helper.getShardBit(i, "test", keyHashRange));
                } else {
                    assertFalse(helper.getShardBit(i, "test", keyHashRange));
                }
            }
            // following testing should be use in real server, not mock server.
            /*for (long i = 0; i < 10; i++) {
                if (i % 3 == 0) {
                    //helper.setShardBit(i, "test", keyHashRange, true);
                    assertTrue(helper.getShardBit(i, "test", keyHashRange));
                } else {
                    assertFalse(helper.getShardBit(i, "test", keyHashRange));
                }
            }*/
        }
    }
}