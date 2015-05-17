/**
 * 
 */
package com.github.phantomthief.jedis;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import com.github.phantomthief.concurrent.AdaptiveExecutor;

import redis.clients.jedis.JedisCluster;

/**
 * @author w.vela
 */
public class JedisClusterHelper {

    private final Supplier<JedisCluster> clusterFactory;
    private final AdaptiveExecutor adaptiveExecutor;

    /**
     * @param clusterFactory
     * @param adaptiveExecutor
     */
    private JedisClusterHelper(Supplier<JedisCluster> clusterFactory,
            AdaptiveExecutor adaptiveExecutor) {
        this.clusterFactory = clusterFactory;
        this.adaptiveExecutor = adaptiveExecutor;
    }

    public <K, V> Map<K, V> pipeline(Collection<K> keys, BiFunction<JedisCluster, K, V> func) {
        return pipeline(keys, func, Function.identity());
    }

    public <K, T, V> Map<K, V> pipeline(Collection<K> keys, BiFunction<JedisCluster, K, T> func,
            Function<T, V> codec) {
        ConcurrentMap<K, V> result = new ConcurrentHashMap<>();
        adaptiveExecutor.invokeAll(keys,
                key -> result.put(key, codec.apply(func.apply(clusterFactory.get(), key))));
        return result;
    }

    public static final class Builder {

        private AdaptiveExecutor executor;

        public Builder withExecutor(AdaptiveExecutor executor) {
            this.executor = executor;
            return this;
        }

        public JedisClusterHelper build(JedisCluster cluster) {
            ensuer();
            return new JedisClusterHelper(() -> cluster, executor);
        }

        public JedisClusterHelper build(Supplier<JedisCluster> clusterFactory) {
            ensuer();
            return new JedisClusterHelper(clusterFactory, executor);
        }

        private void ensuer() {
            if (executor == null) {
                throw new NullPointerException("executor is null.");
            }
        }
    }

    public static final Builder newBuilder() {
        return new Builder();
    }
}
