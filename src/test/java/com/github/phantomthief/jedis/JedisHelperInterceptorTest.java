package com.github.phantomthief.jedis;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.github.phantomthief.jedis.OpInterceptor.JedisOpCall;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author w.vela
 * Created on 2020-06-12.
 */
class JedisHelperInterceptorTest extends BaseJedisTest {

    @Test
    void test() {
        try (JedisPool jedisPool = getPool()) {
            JedisHelper<Jedis> helper = JedisHelper.newBuilder(() -> jedisPool)
                    .addOpInterceptor((pool, method, jedis, args) ->
                            new JedisOpCall<>(method, jedis, args).setFinalObject("test"))
                    .build();
            assertEquals("test", helper.get().get("MY_TEST"));
        }
    }
}