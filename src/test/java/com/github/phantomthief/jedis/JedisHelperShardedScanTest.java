package com.github.phantomthief.jedis;

import static java.util.stream.Collectors.toSet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.Tuple;

/**
 * @author w.vela
 * Created on 2020-05-29.
 */
class JedisHelperShardedScanTest extends BaseJedisTest{

    @Test
    void testSscan() {
        try (ShardedJedisPool jedisPool = getShardedPool()) {
            JedisHelper<ShardedJedis> helper = JedisHelper.newShardedBuilder(() -> jedisPool)
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

    @Test
    void testHscan() {
        try (ShardedJedisPool jedisPool = getShardedPool()) {
            JedisHelper<ShardedJedis> helper = JedisHelper.newShardedBuilder(() -> jedisPool)
                    .build();
            String hscanKey = "hscan_key";
            for (int i = 0; i < 100; i++) {
                helper.get().hset(hscanKey, i + "", i + "");
            }
            Stream<Entry<String, String>> scan = helper.hscan(hscanKey, new ScanParams());
            Set<Entry<String, String>> set = scan.collect(toSet());
            assertEquals(100, set.size());
            for (int i = 0; i < 100; i++) {
                assertTrue(set.contains(new SimpleEntry<>(i + "", i + "")));
            }
        }
    }

    @Test
    void testZscan() {
        try (ShardedJedisPool jedisPool = getShardedPool()) {
            JedisHelper<ShardedJedis> helper = JedisHelper.newShardedBuilder(() -> jedisPool)
                    .build();
            String zscanKey = "zscan_key";
            for (int i = 0; i < 100; i++) {
                helper.get().zadd(zscanKey, i, i + "");
            }
            Stream<Tuple> scan = helper.zscan(zscanKey, new ScanParams());
            Set<Tuple> set = scan.collect(toSet());
            assertEquals(100, set.size());
            for (int i = 0; i < 100; i++) {
                assertTrue(set.contains(new Tuple(i + "", (double) i)));
            }
        }
    }
}
