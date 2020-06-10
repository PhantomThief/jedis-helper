package com.github.phantomthief.jedis;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableList;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author w.vela
 * Created on 2020-06-09.
 */
class JedisHelperShardBitTest extends BaseJedisTest{

    @Test
    void test() {
        try (JedisPool jedisPool = getPool()) {
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
            for (long i = 0; i < 10; i++) {
                if (i % 3 == 0) {
                    assertTrue(helper.getShardBit(i, "test", keyHashRange));
                } else {
                    assertFalse(helper.getShardBit(i, "test", keyHashRange));
                }
            }
        }
    }
}