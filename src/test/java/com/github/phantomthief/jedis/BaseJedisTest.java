package com.github.phantomthief.jedis;

import static java.util.Collections.singletonList;

import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;
import redis.embedded.RedisServer;

/**
 * @author w.vela <wangtianzhou@kuaishou.com>
 * Created on 2020-06-10.
 */
abstract class BaseJedisTest {

    private static final int PORT = ThreadLocalRandom.current().nextInt(1024, 2048);
    private RedisServer server = null;
    private JedisPool jedisPool;
    private ShardedJedisPool shardedJedisPool;

    @BeforeEach
    void setUp() {
        server = new RedisServer(PORT);
        server.start();
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(1);
        jedisPool = new JedisPool(config, "localhost", PORT);
        shardedJedisPool = new ShardedJedisPool(new GenericObjectPoolConfig(), singletonList(new JedisShardInfo("localhost", PORT)));
    }

    @AfterEach
    void tearDown() {
        jedisPool.destroy();
        shardedJedisPool.destroy();
        server.stop();
    }

    protected JedisPool getPool() {
        return jedisPool;
    }

    protected ShardedJedisPool getShardedPool() {
        return shardedJedisPool;
    }
}
