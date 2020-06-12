package com.github.phantomthief.jedis;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableList;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.PipelineBase;

/**
 * @author w.vela
 * Created on 2020-05-29.
 */
class JedisHelperPipelineTest extends BaseJedisTest{

    @Test
    void testPipeline() {
        try (JedisPool jedisPool = getPool()) {
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

    @Test
    void testPipelineListener() {
        try (JedisPool jedisPool = getPool()) {
            JedisHelper<Jedis> helper = JedisHelper.newBuilder(() -> jedisPool)
                    .withPipelinePartitionSize(2)
                    .addPipelineOpListener(new PipelineOpListener<JedisPool, Object>() {
                        @Override
                        public void onRequest(JedisPool pool, PipelineBase pipeline, @Nullable Object obj,
                                long requestTime, long requestNanoTime, Method method, Object[] args,
                                Object pipelineResponse) {
                        }

                        @Nullable
                        @Override
                        public Object onPipelineStarted(@Nullable JedisPool pool) {
                            throw new RuntimeException();
                        }
                    })
                    .build();
            List<Integer> list = ImmutableList.of(1, 2, 3);
            helper.pipeline(list, (p, item) -> p.zrem("test" + item));
        }
    }
}