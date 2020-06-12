package com.github.phantomthief.jedis;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableList;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * @author w.vela
 * Created on 2020-06-12.
 */
class JedisHelperShardedPipelineTest extends BaseJedisTest{

    @Test
    void testPipeline() {
        try (ShardedJedisPool jedisPool = getShardedPool()) {
            JedisHelper<ShardedJedis> helper = JedisHelper.newShardedBuilder(() -> jedisPool)
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