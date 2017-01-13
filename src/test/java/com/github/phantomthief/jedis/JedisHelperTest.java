package com.github.phantomthief.jedis;

import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

/**
 * @author w.vela <wangtianzhou@kuaishou.com>
 * Created on 2017-01-13.
 */
public class JedisHelperTest {

    @Test
    public void test() {
        JedisHelper<Pipeline, Jedis> jedis = JedisHelper.newBuilder(TestJedisPool::new) //
                .withExceptionHandler(this::handler) //
                .build();
    }

    private void handler(TestJedisPool t, Throwable e) {
        //
    }
}