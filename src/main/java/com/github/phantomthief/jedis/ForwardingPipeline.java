package com.github.phantomthief.jedis;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.BitOP;
import redis.clients.jedis.BitPosParams;
import redis.clients.jedis.Client;
import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.GeoRadiusResponse;
import redis.clients.jedis.GeoUnit;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.ZParams;
import redis.clients.jedis.params.geo.GeoRadiusParam;
import redis.clients.jedis.params.sortedset.ZAddParams;
import redis.clients.jedis.params.sortedset.ZIncrByParams;

/**
 * @author w.vela
 * Created on 2017-03-02.
 */
public abstract class ForwardingPipeline<T extends Pipeline> extends Pipeline {

    private final T pipeline;

    public ForwardingPipeline(T pipeline) {
        this.pipeline = pipeline;
    }

    public Response<Long> append(String key, String value) {
        return pipeline.append(key, value);
    }

    public Response<Long> append(byte[] key, byte[] value) {
        return pipeline.append(key, value);
    }

    public Response<List<String>> blpop(String key) {
        return pipeline.blpop(key);
    }

    public Response<List<String>> brpop(String key) {
        return pipeline.brpop(key);
    }

    public Response<List<byte[]>> blpop(byte[] key) {
        return pipeline.blpop(key);
    }

    public Response<List<byte[]>> brpop(byte[] key) {
        return pipeline.brpop(key);
    }

    public Response<Long> decr(String key) {
        return pipeline.decr(key);
    }

    public Response<Long> decr(byte[] key) {
        return pipeline.decr(key);
    }

    public Response<Long> decrBy(String key, long integer) {
        return pipeline.decrBy(key, integer);
    }

    public Response<Long> decrBy(byte[] key, long integer) {
        return pipeline.decrBy(key, integer);
    }

    public Response<Long> del(String key) {
        return pipeline.del(key);
    }

    public Response<Long> del(byte[] key) {
        return pipeline.del(key);
    }

    public Response<String> echo(String string) {
        return pipeline.echo(string);
    }

    public Response<byte[]> echo(byte[] string) {
        return pipeline.echo(string);
    }

    public Response<Boolean> exists(String key) {
        return pipeline.exists(key);
    }

    public Response<Boolean> exists(byte[] key) {
        return pipeline.exists(key);
    }

    public Response<Long> expire(String key, int seconds) {
        return pipeline.expire(key, seconds);
    }

    public Response<Long> expire(byte[] key, int seconds) {
        return pipeline.expire(key, seconds);
    }

    public Response<Long> expireAt(String key, long unixTime) {
        return pipeline.expireAt(key, unixTime);
    }

    public Response<Long> expireAt(byte[] key, long unixTime) {
        return pipeline.expireAt(key, unixTime);
    }

    public Response<String> get(String key) {
        return pipeline.get(key);
    }

    public Response<byte[]> get(byte[] key) {
        return pipeline.get(key);
    }

    public Response<Boolean> getbit(String key, long offset) {
        return pipeline.getbit(key, offset);
    }

    public Response<Boolean> getbit(byte[] key, long offset) {
        return pipeline.getbit(key, offset);
    }

    public Response<Long> bitpos(String key, boolean value) {
        return pipeline.bitpos(key, value);
    }

    public Response<Long> bitpos(String key, boolean value, BitPosParams params) {
        return pipeline.bitpos(key, value, params);
    }

    public Response<Long> bitpos(byte[] key, boolean value) {
        return pipeline.bitpos(key, value);
    }

    public Response<Long> bitpos(byte[] key, boolean value, BitPosParams params) {
        return pipeline.bitpos(key, value, params);
    }

    public Response<String> getrange(String key, long startOffset, long endOffset) {
        return pipeline.getrange(key, startOffset, endOffset);
    }

    public Response<String> getSet(String key, String value) {
        return pipeline.getSet(key, value);
    }

    public Response<byte[]> getSet(byte[] key, byte[] value) {
        return pipeline.getSet(key, value);
    }

    public Response<Long> getrange(byte[] key, long startOffset, long endOffset) {
        return pipeline.getrange(key, startOffset, endOffset);
    }

    public Response<Long> hdel(String key, String... field) {
        return pipeline.hdel(key, field);
    }

    public Response<Long> hdel(byte[] key, byte[]... field) {
        return pipeline.hdel(key, field);
    }

    public Response<Boolean> hexists(String key, String field) {
        return pipeline.hexists(key, field);
    }

    public Response<Boolean> hexists(byte[] key, byte[] field) {
        return pipeline.hexists(key, field);
    }

    public Response<String> hget(String key, String field) {
        return pipeline.hget(key, field);
    }

    public Response<byte[]> hget(byte[] key, byte[] field) {
        return pipeline.hget(key, field);
    }

    public Response<Map<String, String>> hgetAll(String key) {
        return pipeline.hgetAll(key);
    }

    public Response<Map<byte[], byte[]>> hgetAll(byte[] key) {
        return pipeline.hgetAll(key);
    }

    public Response<Long> hincrBy(String key, String field, long value) {
        return pipeline.hincrBy(key, field, value);
    }

    public Response<Long> hincrBy(byte[] key, byte[] field, long value) {
        return pipeline.hincrBy(key, field, value);
    }

    public Response<Set<String>> hkeys(String key) {
        return pipeline.hkeys(key);
    }

    public Response<Set<byte[]>> hkeys(byte[] key) {
        return pipeline.hkeys(key);
    }

    public Response<Long> hlen(String key) {
        return pipeline.hlen(key);
    }

    public Response<Long> hlen(byte[] key) {
        return pipeline.hlen(key);
    }

    public Response<List<String>> hmget(String key, String... fields) {
        return pipeline.hmget(key, fields);
    }

    public Response<List<byte[]>> hmget(byte[] key, byte[]... fields) {
        return pipeline.hmget(key, fields);
    }

    public Response<String> hmset(String key, Map<String, String> hash) {
        return pipeline.hmset(key, hash);
    }

    public Response<String> hmset(byte[] key, Map<byte[], byte[]> hash) {
        return pipeline.hmset(key, hash);
    }

    public Response<Long> hset(String key, String field, String value) {
        return pipeline.hset(key, field, value);
    }

    public Response<Long> hset(byte[] key, byte[] field, byte[] value) {
        return pipeline.hset(key, field, value);
    }

    public Response<Long> hsetnx(String key, String field, String value) {
        return pipeline.hsetnx(key, field, value);
    }

    public Response<Long> hsetnx(byte[] key, byte[] field, byte[] value) {
        return pipeline.hsetnx(key, field, value);
    }

    public Response<List<String>> hvals(String key) {
        return pipeline.hvals(key);
    }

    public Response<List<byte[]>> hvals(byte[] key) {
        return pipeline.hvals(key);
    }

    public Response<Long> incr(String key) {
        return pipeline.incr(key);
    }

    public Response<Long> incr(byte[] key) {
        return pipeline.incr(key);
    }

    public Response<Long> incrBy(String key, long integer) {
        return pipeline.incrBy(key, integer);
    }

    public Response<Long> incrBy(byte[] key, long integer) {
        return pipeline.incrBy(key, integer);
    }

    public Response<String> lindex(String key, long index) {
        return pipeline.lindex(key, index);
    }

    public Response<byte[]> lindex(byte[] key, long index) {
        return pipeline.lindex(key, index);
    }

    public Response<Long> linsert(String key, BinaryClient.LIST_POSITION where, String pivot,
            String value) {
        return pipeline.linsert(key, where, pivot, value);
    }

    public Response<Long> linsert(byte[] key, BinaryClient.LIST_POSITION where, byte[] pivot,
            byte[] value) {
        return pipeline.linsert(key, where, pivot, value);
    }

    public Response<Long> llen(String key) {
        return pipeline.llen(key);
    }

    public Response<Long> llen(byte[] key) {
        return pipeline.llen(key);
    }

    public Response<String> lpop(String key) {
        return pipeline.lpop(key);
    }

    public Response<byte[]> lpop(byte[] key) {
        return pipeline.lpop(key);
    }

    public Response<Long> lpush(String key, String... string) {
        return pipeline.lpush(key, string);
    }

    public Response<Long> lpush(byte[] key, byte[]... string) {
        return pipeline.lpush(key, string);
    }

    public Response<Long> lpushx(String key, String... string) {
        return pipeline.lpushx(key, string);
    }

    public Response<Long> lpushx(byte[] key, byte[]... bytes) {
        return pipeline.lpushx(key, bytes);
    }

    public Response<List<String>> lrange(String key, long start, long end) {
        return pipeline.lrange(key, start, end);
    }

    public Response<List<byte[]>> lrange(byte[] key, long start, long end) {
        return pipeline.lrange(key, start, end);
    }

    public Response<Long> lrem(String key, long count, String value) {
        return pipeline.lrem(key, count, value);
    }

    public Response<Long> lrem(byte[] key, long count, byte[] value) {
        return pipeline.lrem(key, count, value);
    }

    public Response<String> lset(String key, long index, String value) {
        return pipeline.lset(key, index, value);
    }

    public Response<String> lset(byte[] key, long index, byte[] value) {
        return pipeline.lset(key, index, value);
    }

    public Response<String> ltrim(String key, long start, long end) {
        return pipeline.ltrim(key, start, end);
    }

    public Response<String> ltrim(byte[] key, long start, long end) {
        return pipeline.ltrim(key, start, end);
    }

    public Response<Long> move(String key, int dbIndex) {
        return pipeline.move(key, dbIndex);
    }

    public Response<Long> move(byte[] key, int dbIndex) {
        return pipeline.move(key, dbIndex);
    }

    public Response<Long> persist(String key) {
        return pipeline.persist(key);
    }

    public Response<Long> persist(byte[] key) {
        return pipeline.persist(key);
    }

    public Response<String> rpop(String key) {
        return pipeline.rpop(key);
    }

    public Response<byte[]> rpop(byte[] key) {
        return pipeline.rpop(key);
    }

    public Response<Long> rpush(String key, String... string) {
        return pipeline.rpush(key, string);
    }

    public Response<Long> rpush(byte[] key, byte[]... string) {
        return pipeline.rpush(key, string);
    }

    public Response<Long> rpushx(String key, String... string) {
        return pipeline.rpushx(key, string);
    }

    public Response<Long> rpushx(byte[] key, byte[]... string) {
        return pipeline.rpushx(key, string);
    }

    public Response<Long> sadd(String key, String... member) {
        return pipeline.sadd(key, member);
    }

    public Response<Long> sadd(byte[] key, byte[]... member) {
        return pipeline.sadd(key, member);
    }

    public Response<Long> scard(String key) {
        return pipeline.scard(key);
    }

    public Response<Long> scard(byte[] key) {
        return pipeline.scard(key);
    }

    public Response<String> set(String key, String value) {
        return pipeline.set(key, value);
    }

    public Response<String> set(byte[] key, byte[] value) {
        return pipeline.set(key, value);
    }

    public Response<Boolean> setbit(String key, long offset, boolean value) {
        return pipeline.setbit(key, offset, value);
    }

    public Response<Boolean> setbit(byte[] key, long offset, byte[] value) {
        return pipeline.setbit(key, offset, value);
    }

    public Response<String> setex(String key, int seconds, String value) {
        return pipeline.setex(key, seconds, value);
    }

    public Response<String> setex(byte[] key, int seconds, byte[] value) {
        return pipeline.setex(key, seconds, value);
    }

    public Response<Long> setnx(String key, String value) {
        return pipeline.setnx(key, value);
    }

    public Response<Long> setnx(byte[] key, byte[] value) {
        return pipeline.setnx(key, value);
    }

    public Response<Long> setrange(String key, long offset, String value) {
        return pipeline.setrange(key, offset, value);
    }

    public Response<Long> setrange(byte[] key, long offset, byte[] value) {
        return pipeline.setrange(key, offset, value);
    }

    public Response<Boolean> sismember(String key, String member) {
        return pipeline.sismember(key, member);
    }

    public Response<Boolean> sismember(byte[] key, byte[] member) {
        return pipeline.sismember(key, member);
    }

    public Response<Set<String>> smembers(String key) {
        return pipeline.smembers(key);
    }

    public Response<Set<byte[]>> smembers(byte[] key) {
        return pipeline.smembers(key);
    }

    public Response<List<String>> sort(String key) {
        return pipeline.sort(key);
    }

    public Response<List<byte[]>> sort(byte[] key) {
        return pipeline.sort(key);
    }

    public Response<List<String>> sort(String key, SortingParams sortingParameters) {
        return pipeline.sort(key, sortingParameters);
    }

    public Response<List<byte[]>> sort(byte[] key, SortingParams sortingParameters) {
        return pipeline.sort(key, sortingParameters);
    }

    public Response<String> spop(String key) {
        return pipeline.spop(key);
    }

    public Response<Set<String>> spop(String key, long count) {
        return pipeline.spop(key, count);
    }

    public Response<byte[]> spop(byte[] key) {
        return pipeline.spop(key);
    }

    public Response<Set<byte[]>> spop(byte[] key, long count) {
        return pipeline.spop(key, count);
    }

    public Response<String> srandmember(String key) {
        return pipeline.srandmember(key);
    }

    public Response<List<String>> srandmember(String key, int count) {
        return pipeline.srandmember(key, count);
    }

    public Response<byte[]> srandmember(byte[] key) {
        return pipeline.srandmember(key);
    }

    public Response<List<byte[]>> srandmember(byte[] key, int count) {
        return pipeline.srandmember(key, count);
    }

    public Response<Long> srem(String key, String... member) {
        return pipeline.srem(key, member);
    }

    public Response<Long> srem(byte[] key, byte[]... member) {
        return pipeline.srem(key, member);
    }

    public Response<Long> strlen(String key) {
        return pipeline.strlen(key);
    }

    public Response<Long> strlen(byte[] key) {
        return pipeline.strlen(key);
    }

    public Response<String> substr(String key, int start, int end) {
        return pipeline.substr(key, start, end);
    }

    public Response<String> substr(byte[] key, int start, int end) {
        return pipeline.substr(key, start, end);
    }

    public Response<Long> ttl(String key) {
        return pipeline.ttl(key);
    }

    public Response<Long> ttl(byte[] key) {
        return pipeline.ttl(key);
    }

    public Response<String> type(String key) {
        return pipeline.type(key);
    }

    public Response<String> type(byte[] key) {
        return pipeline.type(key);
    }

    public Response<Long> zadd(String key, double score, String member) {
        return pipeline.zadd(key, score, member);
    }

    public Response<Long> zadd(String key, double score, String member, ZAddParams params) {
        return pipeline.zadd(key, score, member, params);
    }

    public Response<Long> zadd(String key, Map<String, Double> scoreMembers) {
        return pipeline.zadd(key, scoreMembers);
    }

    public Response<Long> zadd(String key, Map<String, Double> scoreMembers, ZAddParams params) {
        return pipeline.zadd(key, scoreMembers, params);
    }

    public Response<Long> zadd(byte[] key, double score, byte[] member) {
        return pipeline.zadd(key, score, member);
    }

    public Response<Long> zadd(byte[] key, double score, byte[] member, ZAddParams params) {
        return pipeline.zadd(key, score, member, params);
    }

    public Response<Long> zadd(byte[] key, Map<byte[], Double> scoreMembers) {
        return pipeline.zadd(key, scoreMembers);
    }

    public Response<Long> zadd(byte[] key, Map<byte[], Double> scoreMembers, ZAddParams params) {
        return pipeline.zadd(key, scoreMembers, params);
    }

    public Response<Long> zcard(String key) {
        return pipeline.zcard(key);
    }

    public Response<Long> zcard(byte[] key) {
        return pipeline.zcard(key);
    }

    public Response<Long> zcount(String key, double min, double max) {
        return pipeline.zcount(key, min, max);
    }

    public Response<Long> zcount(String key, String min, String max) {
        return pipeline.zcount(key, min, max);
    }

    public Response<Long> zcount(byte[] key, double min, double max) {
        return pipeline.zcount(key, min, max);
    }

    public Response<Long> zcount(byte[] key, byte[] min, byte[] max) {
        return pipeline.zcount(key, min, max);
    }

    public Response<Double> zincrby(String key, double score, String member) {
        return pipeline.zincrby(key, score, member);
    }

    public Response<Double> zincrby(String key, double score, String member, ZIncrByParams params) {
        return pipeline.zincrby(key, score, member, params);
    }

    public Response<Double> zincrby(byte[] key, double score, byte[] member) {
        return pipeline.zincrby(key, score, member);
    }

    public Response<Double> zincrby(byte[] key, double score, byte[] member, ZIncrByParams params) {
        return pipeline.zincrby(key, score, member, params);
    }

    public Response<Set<String>> zrange(String key, long start, long end) {
        return pipeline.zrange(key, start, end);
    }

    public Response<Set<byte[]>> zrange(byte[] key, long start, long end) {
        return pipeline.zrange(key, start, end);
    }

    public Response<Set<String>> zrangeByScore(String key, double min, double max) {
        return pipeline.zrangeByScore(key, min, max);
    }

    public Response<Set<byte[]>> zrangeByScore(byte[] key, double min, double max) {
        return pipeline.zrangeByScore(key, min, max);
    }

    public Response<Set<String>> zrangeByScore(String key, String min, String max) {
        return pipeline.zrangeByScore(key, min, max);
    }

    public Response<Set<byte[]>> zrangeByScore(byte[] key, byte[] min, byte[] max) {
        return pipeline.zrangeByScore(key, min, max);
    }

    public Response<Set<String>> zrangeByScore(String key, double min, double max, int offset,
            int count) {
        return pipeline.zrangeByScore(key, min, max, offset, count);
    }

    public Response<Set<String>> zrangeByScore(String key, String min, String max, int offset,
            int count) {
        return pipeline.zrangeByScore(key, min, max, offset, count);
    }

    public Response<Set<byte[]>> zrangeByScore(byte[] key, double min, double max, int offset,
            int count) {
        return pipeline.zrangeByScore(key, min, max, offset, count);
    }

    public Response<Set<byte[]>> zrangeByScore(byte[] key, byte[] min, byte[] max, int offset,
            int count) {
        return pipeline.zrangeByScore(key, min, max, offset, count);
    }

    public Response<Set<Tuple>> zrangeByScoreWithScores(String key, double min, double max) {
        return pipeline.zrangeByScoreWithScores(key, min, max);
    }

    public Response<Set<Tuple>> zrangeByScoreWithScores(String key, String min, String max) {
        return pipeline.zrangeByScoreWithScores(key, min, max);
    }

    public Response<Set<Tuple>> zrangeByScoreWithScores(byte[] key, double min, double max) {
        return pipeline.zrangeByScoreWithScores(key, min, max);
    }

    public Response<Set<Tuple>> zrangeByScoreWithScores(byte[] key, byte[] min, byte[] max) {
        return pipeline.zrangeByScoreWithScores(key, min, max);
    }

    public Response<Set<Tuple>> zrangeByScoreWithScores(String key, double min, double max,
            int offset, int count) {
        return pipeline.zrangeByScoreWithScores(key, min, max, offset, count);
    }

    public Response<Set<Tuple>> zrangeByScoreWithScores(String key, String min, String max,
            int offset, int count) {
        return pipeline.zrangeByScoreWithScores(key, min, max, offset, count);
    }

    public Response<Set<Tuple>> zrangeByScoreWithScores(byte[] key, double min, double max,
            int offset, int count) {
        return pipeline.zrangeByScoreWithScores(key, min, max, offset, count);
    }

    public Response<Set<Tuple>> zrangeByScoreWithScores(byte[] key, byte[] min, byte[] max,
            int offset, int count) {
        return pipeline.zrangeByScoreWithScores(key, min, max, offset, count);
    }

    public Response<Set<String>> zrevrangeByScore(String key, double max, double min) {
        return pipeline.zrevrangeByScore(key, max, min);
    }

    public Response<Set<byte[]>> zrevrangeByScore(byte[] key, double max, double min) {
        return pipeline.zrevrangeByScore(key, max, min);
    }

    public Response<Set<String>> zrevrangeByScore(String key, String max, String min) {
        return pipeline.zrevrangeByScore(key, max, min);
    }

    public Response<Set<byte[]>> zrevrangeByScore(byte[] key, byte[] max, byte[] min) {
        return pipeline.zrevrangeByScore(key, max, min);
    }

    public Response<Set<String>> zrevrangeByScore(String key, double max, double min, int offset,
            int count) {
        return pipeline.zrevrangeByScore(key, max, min, offset, count);
    }

    public Response<Set<String>> zrevrangeByScore(String key, String max, String min, int offset,
            int count) {
        return pipeline.zrevrangeByScore(key, max, min, offset, count);
    }

    public Response<Set<byte[]>> zrevrangeByScore(byte[] key, double max, double min, int offset,
            int count) {
        return pipeline.zrevrangeByScore(key, max, min, offset, count);
    }

    public Response<Set<byte[]>> zrevrangeByScore(byte[] key, byte[] max, byte[] min, int offset,
            int count) {
        return pipeline.zrevrangeByScore(key, max, min, offset, count);
    }

    public Response<Set<Tuple>> zrevrangeByScoreWithScores(String key, double max, double min) {
        return pipeline.zrevrangeByScoreWithScores(key, max, min);
    }

    public Response<Set<Tuple>> zrevrangeByScoreWithScores(String key, String max, String min) {
        return pipeline.zrevrangeByScoreWithScores(key, max, min);
    }

    public Response<Set<Tuple>> zrevrangeByScoreWithScores(byte[] key, double max, double min) {
        return pipeline.zrevrangeByScoreWithScores(key, max, min);
    }

    public Response<Set<Tuple>> zrevrangeByScoreWithScores(byte[] key, byte[] max, byte[] min) {
        return pipeline.zrevrangeByScoreWithScores(key, max, min);
    }

    public Response<Set<Tuple>> zrevrangeByScoreWithScores(String key, double max, double min,
            int offset, int count) {
        return pipeline.zrevrangeByScoreWithScores(key, max, min, offset, count);
    }

    public Response<Set<Tuple>> zrevrangeByScoreWithScores(String key, String max, String min,
            int offset, int count) {
        return pipeline.zrevrangeByScoreWithScores(key, max, min, offset, count);
    }

    public Response<Set<Tuple>> zrevrangeByScoreWithScores(byte[] key, double max, double min,
            int offset, int count) {
        return pipeline.zrevrangeByScoreWithScores(key, max, min, offset, count);
    }

    public Response<Set<Tuple>> zrevrangeByScoreWithScores(byte[] key, byte[] max, byte[] min,
            int offset, int count) {
        return pipeline.zrevrangeByScoreWithScores(key, max, min, offset, count);
    }

    public Response<Set<Tuple>> zrangeWithScores(String key, long start, long end) {
        return pipeline.zrangeWithScores(key, start, end);
    }

    public Response<Set<Tuple>> zrangeWithScores(byte[] key, long start, long end) {
        return pipeline.zrangeWithScores(key, start, end);
    }

    public Response<Long> zrank(String key, String member) {
        return pipeline.zrank(key, member);
    }

    public Response<Long> zrank(byte[] key, byte[] member) {
        return pipeline.zrank(key, member);
    }

    public Response<Long> zrem(String key, String... member) {
        return pipeline.zrem(key, member);
    }

    public Response<Long> zrem(byte[] key, byte[]... member) {
        return pipeline.zrem(key, member);
    }

    public Response<Long> zremrangeByRank(String key, long start, long end) {
        return pipeline.zremrangeByRank(key, start, end);
    }

    public Response<Long> zremrangeByRank(byte[] key, long start, long end) {
        return pipeline.zremrangeByRank(key, start, end);
    }

    public Response<Long> zremrangeByScore(String key, double start, double end) {
        return pipeline.zremrangeByScore(key, start, end);
    }

    public Response<Long> zremrangeByScore(String key, String start, String end) {
        return pipeline.zremrangeByScore(key, start, end);
    }

    public Response<Long> zremrangeByScore(byte[] key, double start, double end) {
        return pipeline.zremrangeByScore(key, start, end);
    }

    public Response<Long> zremrangeByScore(byte[] key, byte[] start, byte[] end) {
        return pipeline.zremrangeByScore(key, start, end);
    }

    public Response<Set<String>> zrevrange(String key, long start, long end) {
        return pipeline.zrevrange(key, start, end);
    }

    public Response<Set<byte[]>> zrevrange(byte[] key, long start, long end) {
        return pipeline.zrevrange(key, start, end);
    }

    public Response<Set<Tuple>> zrevrangeWithScores(String key, long start, long end) {
        return pipeline.zrevrangeWithScores(key, start, end);
    }

    public Response<Set<Tuple>> zrevrangeWithScores(byte[] key, long start, long end) {
        return pipeline.zrevrangeWithScores(key, start, end);
    }

    public Response<Long> zrevrank(String key, String member) {
        return pipeline.zrevrank(key, member);
    }

    public Response<Long> zrevrank(byte[] key, byte[] member) {
        return pipeline.zrevrank(key, member);
    }

    public Response<Double> zscore(String key, String member) {
        return pipeline.zscore(key, member);
    }

    public Response<Double> zscore(byte[] key, byte[] member) {
        return pipeline.zscore(key, member);
    }

    public Response<Long> zlexcount(byte[] key, byte[] min, byte[] max) {
        return pipeline.zlexcount(key, min, max);
    }

    public Response<Long> zlexcount(String key, String min, String max) {
        return pipeline.zlexcount(key, min, max);
    }

    public Response<Set<byte[]>> zrangeByLex(byte[] key, byte[] min, byte[] max) {
        return pipeline.zrangeByLex(key, min, max);
    }

    public Response<Set<String>> zrangeByLex(String key, String min, String max) {
        return pipeline.zrangeByLex(key, min, max);
    }

    public Response<Set<byte[]>> zrangeByLex(byte[] key, byte[] min, byte[] max, int offset,
            int count) {
        return pipeline.zrangeByLex(key, min, max, offset, count);
    }

    public Response<Set<String>> zrangeByLex(String key, String min, String max, int offset,
            int count) {
        return pipeline.zrangeByLex(key, min, max, offset, count);
    }

    public Response<Set<byte[]>> zrevrangeByLex(byte[] key, byte[] max, byte[] min) {
        return pipeline.zrevrangeByLex(key, max, min);
    }

    public Response<Set<String>> zrevrangeByLex(String key, String max, String min) {
        return pipeline.zrevrangeByLex(key, max, min);
    }

    public Response<Set<byte[]>> zrevrangeByLex(byte[] key, byte[] max, byte[] min, int offset,
            int count) {
        return pipeline.zrevrangeByLex(key, max, min, offset, count);
    }

    public Response<Set<String>> zrevrangeByLex(String key, String max, String min, int offset,
            int count) {
        return pipeline.zrevrangeByLex(key, max, min, offset, count);
    }

    public Response<Long> zremrangeByLex(byte[] key, byte[] min, byte[] max) {
        return pipeline.zremrangeByLex(key, min, max);
    }

    public Response<Long> zremrangeByLex(String key, String min, String max) {
        return pipeline.zremrangeByLex(key, min, max);
    }

    public Response<Long> bitcount(String key) {
        return pipeline.bitcount(key);
    }

    public Response<Long> bitcount(String key, long start, long end) {
        return pipeline.bitcount(key, start, end);
    }

    public Response<Long> bitcount(byte[] key) {
        return pipeline.bitcount(key);
    }

    public Response<Long> bitcount(byte[] key, long start, long end) {
        return pipeline.bitcount(key, start, end);
    }

    public Response<byte[]> dump(String key) {
        return pipeline.dump(key);
    }

    public Response<byte[]> dump(byte[] key) {
        return pipeline.dump(key);
    }

    public Response<String> migrate(String host, int port, String key, int destinationDb,
            int timeout) {
        return pipeline.migrate(host, port, key, destinationDb, timeout);
    }

    public Response<String> migrate(byte[] host, int port, byte[] key, int destinationDb,
            int timeout) {
        return pipeline.migrate(host, port, key, destinationDb, timeout);
    }

    public Response<Long> objectRefcount(String key) {
        return pipeline.objectRefcount(key);
    }

    public Response<Long> objectRefcount(byte[] key) {
        return pipeline.objectRefcount(key);
    }

    public Response<String> objectEncoding(String key) {
        return pipeline.objectEncoding(key);
    }

    public Response<byte[]> objectEncoding(byte[] key) {
        return pipeline.objectEncoding(key);
    }

    public Response<Long> objectIdletime(String key) {
        return pipeline.objectIdletime(key);
    }

    public Response<Long> objectIdletime(byte[] key) {
        return pipeline.objectIdletime(key);
    }

    @Deprecated
    public Response<Long> pexpire(String key, int milliseconds) {
        return pipeline.pexpire(key, milliseconds);
    }

    @Deprecated
    public Response<Long> pexpire(byte[] key, int milliseconds) {
        return pipeline.pexpire(key, milliseconds);
    }

    public Response<Long> pexpire(String key, long milliseconds) {
        return pipeline.pexpire(key, milliseconds);
    }

    public Response<Long> pexpire(byte[] key, long milliseconds) {
        return pipeline.pexpire(key, milliseconds);
    }

    public Response<Long> pexpireAt(String key, long millisecondsTimestamp) {
        return pipeline.pexpireAt(key, millisecondsTimestamp);
    }

    public Response<Long> pexpireAt(byte[] key, long millisecondsTimestamp) {
        return pipeline.pexpireAt(key, millisecondsTimestamp);
    }

    public Response<Long> pttl(String key) {
        return pipeline.pttl(key);
    }

    public Response<Long> pttl(byte[] key) {
        return pipeline.pttl(key);
    }

    public Response<String> restore(String key, int ttl, byte[] serializedValue) {
        return pipeline.restore(key, ttl, serializedValue);
    }

    public Response<String> restore(byte[] key, int ttl, byte[] serializedValue) {
        return pipeline.restore(key, ttl, serializedValue);
    }

    public Response<Double> incrByFloat(String key, double increment) {
        return pipeline.incrByFloat(key, increment);
    }

    public Response<Double> incrByFloat(byte[] key, double increment) {
        return pipeline.incrByFloat(key, increment);
    }

    @Deprecated
    public Response<String> psetex(String key, int milliseconds, String value) {
        return pipeline.psetex(key, milliseconds, value);
    }

    @Deprecated
    public Response<String> psetex(byte[] key, int milliseconds, byte[] value) {
        return pipeline.psetex(key, milliseconds, value);
    }

    public Response<String> psetex(String key, long milliseconds, String value) {
        return pipeline.psetex(key, milliseconds, value);
    }

    public Response<String> psetex(byte[] key, long milliseconds, byte[] value) {
        return pipeline.psetex(key, milliseconds, value);
    }

    public Response<String> set(String key, String value, String nxxx) {
        return pipeline.set(key, value, nxxx);
    }

    public Response<String> set(byte[] key, byte[] value, byte[] nxxx) {
        return pipeline.set(key, value, nxxx);
    }

    public Response<String> set(String key, String value, String nxxx, String expx, int time) {
        return pipeline.set(key, value, nxxx, expx, time);
    }

    public Response<String> set(byte[] key, byte[] value, byte[] nxxx, byte[] expx, int time) {
        return pipeline.set(key, value, nxxx, expx, time);
    }

    public Response<Double> hincrByFloat(String key, String field, double increment) {
        return pipeline.hincrByFloat(key, field, increment);
    }

    public Response<Double> hincrByFloat(byte[] key, byte[] field, double increment) {
        return pipeline.hincrByFloat(key, field, increment);
    }

    public Response<String> eval(String script) {
        return pipeline.eval(script);
    }

    public Response<String> eval(String script, List<String> keys, List<String> args) {
        return pipeline.eval(script, keys, args);
    }

    public Response<String> eval(String script, int numKeys, String... args) {
        return pipeline.eval(script, numKeys, args);
    }

    public Response<String> evalsha(String script) {
        return pipeline.evalsha(script);
    }

    public Response<String> evalsha(String sha1, List<String> keys, List<String> args) {
        return pipeline.evalsha(sha1, keys, args);
    }

    public Response<String> evalsha(String sha1, int numKeys, String... args) {
        return pipeline.evalsha(sha1, numKeys, args);
    }

    public Response<Long> pfadd(byte[] key, byte[]... elements) {
        return pipeline.pfadd(key, elements);
    }

    public Response<Long> pfcount(byte[] key) {
        return pipeline.pfcount(key);
    }

    public Response<Long> pfadd(String key, String... elements) {
        return pipeline.pfadd(key, elements);
    }

    public Response<Long> pfcount(String key) {
        return pipeline.pfcount(key);
    }

    public Response<Long> geoadd(byte[] key, double longitude, double latitude, byte[] member) {
        return pipeline.geoadd(key, longitude, latitude, member);
    }

    public Response<Long> geoadd(byte[] key, Map<byte[], GeoCoordinate> memberCoordinateMap) {
        return pipeline.geoadd(key, memberCoordinateMap);
    }

    public Response<Long> geoadd(String key, double longitude, double latitude, String member) {
        return pipeline.geoadd(key, longitude, latitude, member);
    }

    public Response<Long> geoadd(String key, Map<String, GeoCoordinate> memberCoordinateMap) {
        return pipeline.geoadd(key, memberCoordinateMap);
    }

    public Response<Double> geodist(byte[] key, byte[] member1, byte[] member2) {
        return pipeline.geodist(key, member1, member2);
    }

    public Response<Double> geodist(byte[] key, byte[] member1, byte[] member2, GeoUnit unit) {
        return pipeline.geodist(key, member1, member2, unit);
    }

    public Response<Double> geodist(String key, String member1, String member2) {
        return pipeline.geodist(key, member1, member2);
    }

    public Response<Double> geodist(String key, String member1, String member2, GeoUnit unit) {
        return pipeline.geodist(key, member1, member2, unit);
    }

    public Response<List<byte[]>> geohash(byte[] key, byte[]... members) {
        return pipeline.geohash(key, members);
    }

    public Response<List<String>> geohash(String key, String... members) {
        return pipeline.geohash(key, members);
    }

    public Response<List<GeoCoordinate>> geopos(byte[] key, byte[]... members) {
        return pipeline.geopos(key, members);
    }

    public Response<List<GeoCoordinate>> geopos(String key, String... members) {
        return pipeline.geopos(key, members);
    }

    public Response<List<GeoRadiusResponse>> georadius(byte[] key, double longitude,
            double latitude, double radius, GeoUnit unit) {
        return pipeline.georadius(key, longitude, latitude, radius, unit);
    }

    public Response<List<GeoRadiusResponse>> georadius(byte[] key, double longitude,
            double latitude, double radius, GeoUnit unit, GeoRadiusParam param) {
        return pipeline.georadius(key, longitude, latitude, radius, unit, param);
    }

    public Response<List<GeoRadiusResponse>> georadius(String key, double longitude,
            double latitude, double radius, GeoUnit unit) {
        return pipeline.georadius(key, longitude, latitude, radius, unit);
    }

    public Response<List<GeoRadiusResponse>> georadius(String key, double longitude,
            double latitude, double radius, GeoUnit unit, GeoRadiusParam param) {
        return pipeline.georadius(key, longitude, latitude, radius, unit, param);
    }

    public Response<List<GeoRadiusResponse>> georadiusByMember(byte[] key, byte[] member,
            double radius, GeoUnit unit) {
        return pipeline.georadiusByMember(key, member, radius, unit);
    }

    public Response<List<GeoRadiusResponse>> georadiusByMember(byte[] key, byte[] member,
            double radius, GeoUnit unit, GeoRadiusParam param) {
        return pipeline.georadiusByMember(key, member, radius, unit, param);
    }

    public Response<List<GeoRadiusResponse>> georadiusByMember(String key, String member,
            double radius, GeoUnit unit) {
        return pipeline.georadiusByMember(key, member, radius, unit);
    }

    public Response<List<GeoRadiusResponse>> georadiusByMember(String key, String member,
            double radius, GeoUnit unit, GeoRadiusParam param) {
        return pipeline.georadiusByMember(key, member, radius, unit, param);
    }

    public void setClient(Client client) {
        pipeline.setClient(client);
    }

    public void clear() {
        pipeline.clear();
    }

    public boolean isInMulti() {
        return pipeline.isInMulti();
    }

    public void sync() {
        pipeline.sync();
    }

    public List<Object> syncAndReturnAll() {
        return pipeline.syncAndReturnAll();
    }

    public Response<String> discard() {
        return pipeline.discard();
    }

    public Response<List<Object>> exec() {
        return pipeline.exec();
    }

    public Response<String> multi() {
        return pipeline.multi();
    }

    public void close() throws IOException {
        pipeline.close();
    }

    public Response<List<String>> brpop(String... args) {
        return pipeline.brpop(args);
    }

    public Response<List<String>> brpop(int timeout, String... keys) {
        return pipeline.brpop(timeout, keys);
    }

    public Response<List<String>> blpop(String... args) {
        return pipeline.blpop(args);
    }

    public Response<List<String>> blpop(int timeout, String... keys) {
        return pipeline.blpop(timeout, keys);
    }

    public Response<Map<String, String>> blpopMap(int timeout, String... keys) {
        return pipeline.blpopMap(timeout, keys);
    }

    public Response<List<byte[]>> brpop(byte[]... args) {
        return pipeline.brpop(args);
    }

    public Response<List<String>> brpop(int timeout, byte[]... keys) {
        return pipeline.brpop(timeout, keys);
    }

    public Response<Map<String, String>> brpopMap(int timeout, String... keys) {
        return pipeline.brpopMap(timeout, keys);
    }

    public Response<List<byte[]>> blpop(byte[]... args) {
        return pipeline.blpop(args);
    }

    public Response<List<String>> blpop(int timeout, byte[]... keys) {
        return pipeline.blpop(timeout, keys);
    }

    public Response<Long> del(String... keys) {
        return pipeline.del(keys);
    }

    public Response<Long> del(byte[]... keys) {
        return pipeline.del(keys);
    }

    public Response<Long> exists(String... keys) {
        return pipeline.exists(keys);
    }

    public Response<Long> exists(byte[]... keys) {
        return pipeline.exists(keys);
    }

    public Response<Set<String>> keys(String pattern) {
        return pipeline.keys(pattern);
    }

    public Response<Set<byte[]>> keys(byte[] pattern) {
        return pipeline.keys(pattern);
    }

    public Response<List<String>> mget(String... keys) {
        return pipeline.mget(keys);
    }

    public Response<List<byte[]>> mget(byte[]... keys) {
        return pipeline.mget(keys);
    }

    public Response<String> mset(String... keysvalues) {
        return pipeline.mset(keysvalues);
    }

    public Response<String> mset(byte[]... keysvalues) {
        return pipeline.mset(keysvalues);
    }

    public Response<Long> msetnx(String... keysvalues) {
        return pipeline.msetnx(keysvalues);
    }

    public Response<Long> msetnx(byte[]... keysvalues) {
        return pipeline.msetnx(keysvalues);
    }

    public Response<String> rename(String oldkey, String newkey) {
        return pipeline.rename(oldkey, newkey);
    }

    public Response<String> rename(byte[] oldkey, byte[] newkey) {
        return pipeline.rename(oldkey, newkey);
    }

    public Response<Long> renamenx(String oldkey, String newkey) {
        return pipeline.renamenx(oldkey, newkey);
    }

    public Response<Long> renamenx(byte[] oldkey, byte[] newkey) {
        return pipeline.renamenx(oldkey, newkey);
    }

    public Response<String> rpoplpush(String srckey, String dstkey) {
        return pipeline.rpoplpush(srckey, dstkey);
    }

    public Response<byte[]> rpoplpush(byte[] srckey, byte[] dstkey) {
        return pipeline.rpoplpush(srckey, dstkey);
    }

    public Response<Set<String>> sdiff(String... keys) {
        return pipeline.sdiff(keys);
    }

    public Response<Set<byte[]>> sdiff(byte[]... keys) {
        return pipeline.sdiff(keys);
    }

    public Response<Long> sdiffstore(String dstkey, String... keys) {
        return pipeline.sdiffstore(dstkey, keys);
    }

    public Response<Long> sdiffstore(byte[] dstkey, byte[]... keys) {
        return pipeline.sdiffstore(dstkey, keys);
    }

    public Response<Set<String>> sinter(String... keys) {
        return pipeline.sinter(keys);
    }

    public Response<Set<byte[]>> sinter(byte[]... keys) {
        return pipeline.sinter(keys);
    }

    public Response<Long> sinterstore(String dstkey, String... keys) {
        return pipeline.sinterstore(dstkey, keys);
    }

    public Response<Long> sinterstore(byte[] dstkey, byte[]... keys) {
        return pipeline.sinterstore(dstkey, keys);
    }

    public Response<Long> smove(String srckey, String dstkey, String member) {
        return pipeline.smove(srckey, dstkey, member);
    }

    public Response<Long> smove(byte[] srckey, byte[] dstkey, byte[] member) {
        return pipeline.smove(srckey, dstkey, member);
    }

    public Response<Long> sort(String key, SortingParams sortingParameters, String dstkey) {
        return pipeline.sort(key, sortingParameters, dstkey);
    }

    public Response<Long> sort(byte[] key, SortingParams sortingParameters, byte[] dstkey) {
        return pipeline.sort(key, sortingParameters, dstkey);
    }

    public Response<Long> sort(String key, String dstkey) {
        return pipeline.sort(key, dstkey);
    }

    public Response<Long> sort(byte[] key, byte[] dstkey) {
        return pipeline.sort(key, dstkey);
    }

    public Response<Set<String>> sunion(String... keys) {
        return pipeline.sunion(keys);
    }

    public Response<Set<byte[]>> sunion(byte[]... keys) {
        return pipeline.sunion(keys);
    }

    public Response<Long> sunionstore(String dstkey, String... keys) {
        return pipeline.sunionstore(dstkey, keys);
    }

    public Response<Long> sunionstore(byte[] dstkey, byte[]... keys) {
        return pipeline.sunionstore(dstkey, keys);
    }

    public Response<String> watch(String... keys) {
        return pipeline.watch(keys);
    }

    public Response<String> watch(byte[]... keys) {
        return pipeline.watch(keys);
    }

    public Response<Long> zinterstore(String dstkey, String... sets) {
        return pipeline.zinterstore(dstkey, sets);
    }

    public Response<Long> zinterstore(byte[] dstkey, byte[]... sets) {
        return pipeline.zinterstore(dstkey, sets);
    }

    public Response<Long> zinterstore(String dstkey, ZParams params, String... sets) {
        return pipeline.zinterstore(dstkey, params, sets);
    }

    public Response<Long> zinterstore(byte[] dstkey, ZParams params, byte[]... sets) {
        return pipeline.zinterstore(dstkey, params, sets);
    }

    public Response<Long> zunionstore(String dstkey, String... sets) {
        return pipeline.zunionstore(dstkey, sets);
    }

    public Response<Long> zunionstore(byte[] dstkey, byte[]... sets) {
        return pipeline.zunionstore(dstkey, sets);
    }

    public Response<Long> zunionstore(String dstkey, ZParams params, String... sets) {
        return pipeline.zunionstore(dstkey, params, sets);
    }

    public Response<Long> zunionstore(byte[] dstkey, ZParams params, byte[]... sets) {
        return pipeline.zunionstore(dstkey, params, sets);
    }

    public Response<String> bgrewriteaof() {
        return pipeline.bgrewriteaof();
    }

    public Response<String> bgsave() {
        return pipeline.bgsave();
    }

    public Response<List<String>> configGet(String pattern) {
        return pipeline.configGet(pattern);
    }

    public Response<String> configSet(String parameter, String value) {
        return pipeline.configSet(parameter, value);
    }

    public Response<String> brpoplpush(String source, String destination, int timeout) {
        return pipeline.brpoplpush(source, destination, timeout);
    }

    public Response<byte[]> brpoplpush(byte[] source, byte[] destination, int timeout) {
        return pipeline.brpoplpush(source, destination, timeout);
    }

    public Response<String> configResetStat() {
        return pipeline.configResetStat();
    }

    public Response<String> save() {
        return pipeline.save();
    }

    public Response<Long> lastsave() {
        return pipeline.lastsave();
    }

    public Response<Long> publish(String channel, String message) {
        return pipeline.publish(channel, message);
    }

    public Response<Long> publish(byte[] channel, byte[] message) {
        return pipeline.publish(channel, message);
    }

    public Response<String> randomKey() {
        return pipeline.randomKey();
    }

    public Response<byte[]> randomKeyBinary() {
        return pipeline.randomKeyBinary();
    }

    public Response<String> flushDB() {
        return pipeline.flushDB();
    }

    public Response<String> flushAll() {
        return pipeline.flushAll();
    }

    public Response<String> info() {
        return pipeline.info();
    }

    public Response<String> info(String section) {
        return pipeline.info(section);
    }

    public Response<List<String>> time() {
        return pipeline.time();
    }

    public Response<Long> dbSize() {
        return pipeline.dbSize();
    }

    public Response<String> shutdown() {
        return pipeline.shutdown();
    }

    public Response<String> ping() {
        return pipeline.ping();
    }

    public Response<String> select(int index) {
        return pipeline.select(index);
    }

    public Response<Long> bitop(BitOP op, byte[] destKey, byte[]... srcKeys) {
        return pipeline.bitop(op, destKey, srcKeys);
    }

    public Response<Long> bitop(BitOP op, String destKey, String... srcKeys) {
        return pipeline.bitop(op, destKey, srcKeys);
    }

    public Response<String> clusterNodes() {
        return pipeline.clusterNodes();
    }

    public Response<String> clusterMeet(String ip, int port) {
        return pipeline.clusterMeet(ip, port);
    }

    public Response<String> clusterAddSlots(int... slots) {
        return pipeline.clusterAddSlots(slots);
    }

    public Response<String> clusterDelSlots(int... slots) {
        return pipeline.clusterDelSlots(slots);
    }

    public Response<String> clusterInfo() {
        return pipeline.clusterInfo();
    }

    public Response<List<String>> clusterGetKeysInSlot(int slot, int count) {
        return pipeline.clusterGetKeysInSlot(slot, count);
    }

    public Response<String> clusterSetSlotNode(int slot, String nodeId) {
        return pipeline.clusterSetSlotNode(slot, nodeId);
    }

    public Response<String> clusterSetSlotMigrating(int slot, String nodeId) {
        return pipeline.clusterSetSlotMigrating(slot, nodeId);
    }

    public Response<String> clusterSetSlotImporting(int slot, String nodeId) {
        return pipeline.clusterSetSlotImporting(slot, nodeId);
    }

    public Response<Object> eval(byte[] script) {
        return pipeline.eval(script);
    }

    public Response<Object> eval(byte[] script, byte[] keyCount, byte[]... params) {
        return pipeline.eval(script, keyCount, params);
    }

    public Response<Object> eval(byte[] script, List<byte[]> keys, List<byte[]> args) {
        return pipeline.eval(script, keys, args);
    }

    public Response<Object> eval(byte[] script, int keyCount, byte[]... params) {
        return pipeline.eval(script, keyCount, params);
    }

    public Response<Object> evalsha(byte[] sha1) {
        return pipeline.evalsha(sha1);
    }

    public Response<Object> evalsha(byte[] sha1, List<byte[]> keys, List<byte[]> args) {
        return pipeline.evalsha(sha1, keys, args);
    }

    public Response<Object> evalsha(byte[] sha1, int keyCount, byte[]... params) {
        return pipeline.evalsha(sha1, keyCount, params);
    }

    public Response<String> pfmerge(byte[] destkey, byte[]... sourcekeys) {
        return pipeline.pfmerge(destkey, sourcekeys);
    }

    public Response<String> pfmerge(String destkey, String... sourcekeys) {
        return pipeline.pfmerge(destkey, sourcekeys);
    }

    public Response<Long> pfcount(String... keys) {
        return pipeline.pfcount(keys);
    }

    public Response<Long> pfcount(byte[]... keys) {
        return pipeline.pfcount(keys);
    }
}
