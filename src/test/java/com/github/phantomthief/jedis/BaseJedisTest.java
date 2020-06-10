package com.github.phantomthief.jedis;

import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import redis.clients.jedis.JedisPool;
import redis.embedded.RedisServer;

/**
 * @author w.vela <wangtianzhou@kuaishou.com>
 * Created on 2020-06-10.
 */
abstract class BaseJedisTest {

    private static final int PORT = ThreadLocalRandom.current().nextInt(1024, 2048);
    private RedisServer server = null;
    private JedisPool jedisPool;

    @BeforeEach
    void setUp() {
        server = new RedisServer(PORT);
        server.start();
        jedisPool = new JedisPool("localhost", PORT);
    }

    @AfterEach
    void tearDown() {
        jedisPool.destroy();
        server.stop();
    }

    protected JedisPool getPool() {
        return jedisPool;
    }
}
