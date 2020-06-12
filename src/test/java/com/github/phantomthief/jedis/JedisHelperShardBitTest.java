package com.github.phantomthief.jedis;

import static java.util.stream.Collectors.toSet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

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
            List<Long> keys = new ArrayList<>();
            for (long i = 0; i < 10; i++) {
                if (i % 3 == 0) {
                    assertTrue(helper.getShardBit(i, "test", keyHashRange));
                } else {
                    assertFalse(helper.getShardBit(i, "test", keyHashRange));
                }
                keys.add(i);
            }
            Map<Long, Boolean> test = helper.getShardBit(keys, "test", keyHashRange);
            int booleanCount = 0;
            for (long i = 0; i < 10; i++) {
                if (i % 3 == 0) {
                    booleanCount++;
                    assertTrue(test.get(i));
                } else {
                    assertFalse(test.get(i));
                }
                keys.add(i);
            }
            assertEquals(booleanCount, helper.getShardBitCount("test", keyHashRange, 0, 10));
            Map<Long, String> test1 = JedisHelper.getShardBitKeys(keys, "test", keyHashRange);
            assertEquals(keys.stream().distinct().count(), test1.size());

            Stream<Long> stream = helper.iterateShardBit("test", keyHashRange, 0, 10);
            Set<Long> collect = stream.collect(toSet());
            for (long i = 0; i < 10; i++) {
                if (i % 3 == 0) {
                    assertTrue(collect.contains(i));
                } else {
                    assertFalse(collect.contains(i));
                }
            }
            helper.delShardBit("test", keyHashRange, 0, 10);

            stream = helper.iterateShardBit("test", keyHashRange, 0, 10);
            collect = stream.collect(toSet());
            for (long i = 0; i < 10; i++) {
                assertFalse(collect.contains(i));
            }
        }
    }
}