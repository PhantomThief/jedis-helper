/**
 * 
 */
package com.github.phantomthief.jedis;

import static java.util.function.Function.identity;

import java.io.Closeable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.github.phantomthief.util.CursorIteratorEx;
import com.google.common.base.Throwables;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;

import redis.clients.jedis.BasicCommands;
import redis.clients.jedis.BinaryJedis;
import redis.clients.jedis.BinaryJedisCommands;
import redis.clients.jedis.BinaryShardedJedis;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.PipelineBase;
import redis.clients.jedis.Response;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPipeline;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.Tuple;
import redis.clients.util.Pool;

/**
 * @author w.vela
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class JedisHelper<P extends PipelineBase, J extends Closeable> {

    private final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());

    private final static int PARTITION_SIZE = 100;

    private final Supplier<Object> poolFactory;
    private final BiConsumer<Object, Throwable> exceptionHandler;
    private final int pipelinePartitonSize;

    private final Class<?> jedisType;
    private final Class<?> binaryJedisType;

    /**
     * @param poolFactory
     * @param exceptionHandler
     * @param pipelinePartitonSize
     * @param jedisType
     * @param binaryJedisType
     */
    private JedisHelper(Supplier<Object> poolFactory, //
            BiConsumer<Object, Throwable> exceptionHandler, //
            int pipelinePartitonSize, //
            Class<?> jedisType, //
            Class<?> binaryJedisType) {
        this.poolFactory = poolFactory;
        this.exceptionHandler = exceptionHandler;
        this.pipelinePartitonSize = pipelinePartitonSize;
        this.jedisType = jedisType;
        this.binaryJedisType = binaryJedisType;
    }

    public <K, V> Map<K, V> pipeline(Iterable<K> keys, BiFunction<P, K, Response<V>> function) {
        return pipeline(keys, function, identity());
    }

    public <K, V, T> Map<K, T> pipeline(Iterable<K> keys, BiFunction<P, K, Response<V>> function,
            Function<V, T> decoder) {
        int size;
        if (keys != null && keys instanceof Collection) {
            size = ((Collection<K>) keys).size();
        } else {
            size = 16;
        }
        Map<K, T> result = Maps.newHashMapWithExpectedSize(size);
        if (keys != null) {
            Iterable<List<K>> partition = Iterables.partition(keys, pipelinePartitonSize);
            for (List<K> list : partition) {
                Object pool = poolFactory.get();
                String jedisInfo = null;
                try (J jedis = getJedis(pool)) {
                    jedisInfo = getJedisInfo(jedis);
                    P pipeline = pipeline(jedis);
                    Map<K, Response<V>> thisMap = new HashMap<>(list.size());
                    for (K key : list) {
                        Response<V> apply = function.apply(pipeline, key);
                        thisMap.put(key, apply);
                    }
                    syncPipeline(pipeline);
                    for (Entry<K, Response<V>> entry : thisMap.entrySet()) {
                        if (entry.getValue() != null) {
                            result.put(entry.getKey(), decoder.apply(entry.getValue().get()));
                        }
                    }
                } catch (Throwable e) {
                    if (exceptionHandler != null) {
                        exceptionHandler.accept(pool, e);
                    }
                    logger.error("fail to exec jedis pipeline command, pool:{}", jedisInfo, e);
                }
            }
        }
        return result;
    }

    public BasicCommands getBasic() {
        return (BasicCommands) Proxy.newProxyInstance(jedisType.getClassLoader(),
                jedisType.getInterfaces(), new PoolableJedisCommands());
    }

    public JedisCommands get() {
        return (JedisCommands) Proxy.newProxyInstance(jedisType.getClassLoader(),
                jedisType.getInterfaces(), new PoolableJedisCommands());
    }

    public BinaryJedisCommands getBinary() {
        return (BinaryJedisCommands) Proxy.newProxyInstance(binaryJedisType.getClassLoader(),
                binaryJedisType.getInterfaces(), new PoolableJedisCommands());
    }

    private final class PoolableJedisCommands implements InvocationHandler {

        /* (non-Javadoc)
         * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
         */
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String jedisInfo = null;
            Object pool = poolFactory.get();
            try (J jedis = getJedis(pool)) {
                jedisInfo = getJedisInfo(jedis);
                Object invoke = method.invoke(jedis, args);
                return invoke;
            } catch (Throwable e) {
                e = Throwables.getRootCause(e);
                if (exceptionHandler != null) {
                    exceptionHandler.accept(pool, e);
                }
                logger.error("fail to exec jedis command, pool:{}, cmd:{}, args:{}", jedisInfo,
                        method, Arrays.toString(args), e);
                throw e;
            }
        }
    }

    private void syncPipeline(P pipeline) {
        if (pipeline instanceof Pipeline) {
            ((Pipeline) pipeline).sync();
        } else if (pipeline instanceof ShardedJedisPipeline) {
            ((ShardedJedisPipeline) pipeline).sync();
        }
    }

    private String getJedisInfo(Object obj) {
        if (obj instanceof Jedis) {
            Jedis jedis = (Jedis) obj;
            return jedis.getClient().getHost() + ":" + jedis.getClient().getPort();
        }
        return null;
    }

    private J getJedis(Object pool) {
        if (pool instanceof Pool) {
            return ((Pool<J>) pool).getResource();
        } else {
            throw new IllegalArgumentException("invalid pool:" + pool);
        }
    }

    private P pipeline(J jedis) {
        if (jedis instanceof Jedis) {
            return (P) ((Jedis) jedis).pipelined();
        } else if (jedis instanceof ShardedJedis) {
            return (P) ((ShardedJedis) jedis).pipelined();
        } else {
            throw new IllegalArgumentException("invalid jedis:" + jedis);
        }
    }

    public boolean getShardBit(long bit, String keyPrefix, int keyHashRange) {
        return getShardBit(Collections.singleton(bit), keyPrefix, keyHashRange).getOrDefault(bit,
                false);
    }

    public Map<Long, Boolean> getShardBit(Collection<Long> bits, String keyPrefix,
            int keyHashRange) {
        return pipeline(bits, (p, bit) -> p.getbit(getShardBitKey(bit, keyPrefix, keyHashRange),
                bit % keyHashRange));
    }

    public static String getShardBitKey(long bit, String keyPrefix, int keyHashRange) {
        return keyPrefix + "_" + (bit / keyHashRange);
    }

    public static Map<Long, String> getShardBitKeys(Collection<Long> bits, String keyPrefix,
            int keyHashRange) {
        Map<Long, String> result = new HashMap<>();
        for (Long bit : bits) {
            result.put(bit, getShardBitKey(bit, keyPrefix, keyHashRange));
        }
        return result;
    }

    public long getShardBitCount(String keyPrefix, int keyHashRange, long start, long end) {
        return generateKeys(keyPrefix, keyHashRange, start, end).values().stream()
                .mapToLong(get()::bitcount).sum();
    }

    public boolean setShardBit(long bit, String keyPrefix, int keyHashRange) {
        return setShardBit(Collections.singleton(bit), keyPrefix, keyHashRange).get(bit);
    }

    public boolean setShardBit(long bit, String keyPrefix, int keyHashRange, boolean value) {
        return setShardBitSet(Collections.singleton(bit), keyPrefix, keyHashRange, value).get(bit);
    }

    public Map<Long, Boolean> setShardBitSet(Collection<Long> bits, String keyPrefix,
            int keyHashRange, boolean value) {
        return pipeline(bits, (p, bit) -> p.setbit(getShardBitKey(bit, keyPrefix, keyHashRange),
                bit % keyHashRange, value));
    }

    public Map<Long, Boolean> setShardBit(Collection<Long> bits, String keyPrefix,
            int keyHashRange) {
        return setShardBitSet(bits, keyPrefix, keyHashRange, true);
    }

    public void delShardBit(String keyPrefix, int keyHashRange, long start, long end) {
        Map<Long, String> allKeys = generateKeys(keyPrefix, keyHashRange, start, end);
        allKeys.values().stream().forEach(get()::del);
    }

    public Stream<Long> iterateShardBit(String keyPrefix, int keyHashRange, long start, long end) {
        Map<Long, String> allKeys = generateKeys(keyPrefix, keyHashRange, start, end);
        return allKeys.entrySet().stream().flatMap(this::mapToLong);
    }

    private Map<Long, String> generateKeys(String keyPrefix, int keyHashRange, long start,
            long end) {
        Map<Long, String> result = new LinkedHashMap<>();
        for (long i = start; i <= end; i += keyHashRange) {
            result.put((i / keyHashRange) * keyHashRange, keyPrefix + "_" + (i / keyHashRange));
        }
        return result;
    }

    private Stream<Long> mapToLong(Entry<Long, String> entry) {
        byte[] bytes = getBinary().get(entry.getValue().getBytes());
        List<Long> result = new ArrayList<>();
        if (bytes != null && bytes.length > 0) {
            for (int i = 0; i < (bytes.length * 8); i++) {
                if ((bytes[i / 8] & (1 << (7 - (i % 8)))) != 0) {
                    result.add(entry.getKey() + i);
                }
            }
        }
        return result.stream();
    }

    public static final class Builder<P extends PipelineBase, J extends Closeable, O> {

        private Supplier<Object> poolFactory;
        private BiConsumer<O, Throwable> exceptionHandler;
        private int pipelinePartitonSize;

        private Class<?> jedisType;
        private Class<?> binaryJedisType;

        public Builder<P, J, O> withExceptionHandler(BiConsumer<O, Throwable> exceptionHandler) {
            this.exceptionHandler = exceptionHandler;
            return this;
        }

        public Builder<P, J, O> withPipelinePartitionSize(int size) {
            this.pipelinePartitonSize = size;
            return this;
        }

        public JedisHelper<P, J> build() {
            ensure();
            return new JedisHelper<>(poolFactory, (BiConsumer<Object, Throwable>) exceptionHandler,
                    pipelinePartitonSize, jedisType, binaryJedisType);
        }

        private void ensure() {
            if (pipelinePartitonSize <= 0) {
                pipelinePartitonSize = PARTITION_SIZE;
            }
        }
    }

    public Stream<String> scan(ScanParams params) {
        return this.<String, String> scan((j, c) -> {
            if (j instanceof Jedis) {
                return ((Jedis) j).scan(c, params);
            } else if (j instanceof ShardedJedis) {
                throw new UnsupportedOperationException();
            } else {
                throw new UnsupportedOperationException();
            }
        } , ScanResult::getStringCursor, "0").stream();
    }

    public Stream<Entry<String, String>> hscan(String key) {
        return this.<String, Entry<String, String>> scan((j, c) -> {
            if (j instanceof Jedis) {
                return ((Jedis) j).hscan(key, c);
            } else if (j instanceof ShardedJedis) {
                return ((ShardedJedis) j).hscan(key, c);
            } else {
                throw new UnsupportedOperationException();
            }
        } , ScanResult::getStringCursor, "0").stream();
    }

    public Stream<Tuple> zscan(String key) {
        return this.<String, Tuple> scan((j, c) -> {
            if (j instanceof Jedis) {
                return ((Jedis) j).zscan(key, c);
            } else if (j instanceof ShardedJedis) {
                return ((ShardedJedis) j).zscan(key, c);
            } else {
                throw new UnsupportedOperationException();
            }
        } , ScanResult::getStringCursor, "0").stream();
    }

    public Stream<String> sscan(String key) {
        return this.<String, String> scan((j, c) -> {
            if (j instanceof Jedis) {
                return ((Jedis) j).sscan(key, c);
            } else if (j instanceof ShardedJedis) {
                return ((ShardedJedis) j).sscan(key, c);
            } else {
                throw new UnsupportedOperationException();
            }
        } , ScanResult::getStringCursor, "0").stream();
    }

    private <K, R> CursorIteratorEx<R, K, ScanResult<R>> scan(
            BiFunction<J, K, ScanResult<R>> scanFunction,
            Function<ScanResult<R>, K> cursorExtractor, K initCursor) {
        CursorIteratorEx<R, K, ScanResult<R>> cursorIteratorEx = CursorIteratorEx
                .<R, K, ScanResult<R>> newBuilder() //
                .withDataRetriever(cursor -> {
                    Object pool = poolFactory.get();
                    try (J jedis = getJedis(pool)) {
                        ScanResult<R> result = scanFunction.apply(jedis, cursor);
                        return result;
                    } catch (Throwable e) {
                        if (exceptionHandler != null) {
                            exceptionHandler.accept(pool, e);
                        }
                        if (e instanceof RuntimeException) {
                            throw (RuntimeException) e;
                        } else {
                            throw new RuntimeException(e);
                        }
                    }
                }) //
                .withCursorExtractor(cursorExtractor) //
                .withDataExtractor(s -> s.getResult().iterator()) //
                .withEndChecker(s -> "0".equals(s) || s == null) //
                .withInitCursor(initCursor) //
                .build();
        return cursorIteratorEx;
    }

    public static final Builder<ShardedJedisPipeline, ShardedJedis, ShardedJedisPool>
            newShardedBuilder(Supplier<ShardedJedisPool> poolFactory) {
        Builder<ShardedJedisPipeline, ShardedJedis, ShardedJedisPool> builder = new Builder<>();
        builder.poolFactory = (Supplier) poolFactory;
        builder.jedisType = ShardedJedis.class;
        builder.binaryJedisType = BinaryShardedJedis.class;
        return builder;
    }

    public static final Builder<Pipeline, Jedis, JedisPool>
            newBuilder(Supplier<JedisPool> poolFactory) {
        Builder<Pipeline, Jedis, JedisPool> builder = new Builder<>();
        builder.poolFactory = (Supplier) poolFactory;
        builder.jedisType = Jedis.class;
        builder.binaryJedisType = BinaryJedis.class;
        return builder;
    }
}
