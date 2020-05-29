package com.github.phantomthief.jedis;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.github.fppt.jedismock.RedisServer;
import com.google.common.collect.ImmutableList;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author w.vela
 * Created on 2020-05-29.
 */
class JedisHelperPipelineTest {

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
    void testPipeline() {
        boolean[] pipelineAfterSync = {false, false};
        try (JedisPool jedisPool = new JedisPool(server.getHost(), server.getBindPort())) {
            JedisHelper<Jedis> helper = JedisHelper.newBuilder(() -> jedisPool)
                    .build();
            List<Integer> list = ImmutableList.of(1, 2, 3);
            Map<Integer, Long> setResult = helper.pipeline(list, (p, item) -> p.hset("test", "s" + item, item + ""));
            for (Integer item : list) {
                assertEquals(1, setResult.get(item));
            }
            Map<Integer, Integer> getResult =
                    helper.binaryPipeline(list, (p, item) -> p.hget("test".getBytes(), ("s" + item).getBytes()),
                            v -> Integer.parseInt(new String(v)), true);
            for (Integer item : list) {
                assertEquals(item, getResult.get(item));
            }
        }
    }
}