/**
 * 
 */
package com.github.phantomthief.jedis;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import redis.clients.jedis.JedisCluster;

/**
 * @author w.vela
 */
public class JedisClusterHelper {

    private static CallerRunsPolicy callerRunsPolicy = new CallerRunsPolicy();

    private final JedisCluster cluster;

    private final int globalMaxThread;
    private final int maxThreadPerRequest;

    private final AtomicInteger threadCounter = new AtomicInteger();

    /**
     * @param cluster
     * @param globalMaxThread
     * @param maxThreadPerRequest
     */
    private JedisClusterHelper(JedisCluster cluster, int globalMaxThread, int maxThreadPerRequest) {
        this.cluster = cluster;
        this.globalMaxThread = globalMaxThread;
        this.maxThreadPerRequest = maxThreadPerRequest;
    }

    public <K, V> Map<K, V> pipeline(Collection<K> keys, BiFunction<JedisCluster, K, V> func) {
        return pipeline(keys, func, Function.identity());
    }

    public <K, T, V> Map<K, V> pipeline(Collection<K> keys, BiFunction<JedisCluster, K, T> func,
            Function<T, V> codec) {
        ConcurrentMap<K, V> result = new ConcurrentHashMap<>();
        ExecutorService executorService = newExecutor(keys.size());
        for (K key : keys) {
            executorService.execute(() -> result.put(key, codec.apply(func.apply(cluster, key))));
        }
        if (MoreExecutors.shutdownAndAwaitTermination(executorService, 1, TimeUnit.MINUTES)) {
            decrCounter(executorService);
        }
        return result;
    }

    private final int leftThreadCount(int old, int need) {
        if (old >= globalMaxThread) {
            return 0;
        }
        return Math.min(globalMaxThread - old, need);
    }

    private final void decrCounter(ExecutorService executorService) {
        if (executorService instanceof ListeningExecutorService) {
            return;
        }
        if (executorService instanceof ThreadPoolExecutor) {
            ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executorService;
            threadCounter.addAndGet(-threadPoolExecutor.getCorePoolSize());
        }
    }

    private ExecutorService newExecutor(int keySize) {
        int needThread = Math.min(maxThreadPerRequest, keySize / maxThreadPerRequest);
        if (needThread <= 1) {
            return MoreExecutors.newDirectExecutorService();
        }
        int leftThread = threadCounter.updateAndGet(old -> leftThreadCount(old, needThread));
        if (leftThread <= 0) {
            return MoreExecutors.newDirectExecutorService();
        } else {
            ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(leftThread, leftThread,
                    0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(1));
            threadPoolExecutor.setRejectedExecutionHandler(callerRunsPolicy);
            return threadPoolExecutor;
        }
    }

    public static final class Builder {

        private int globalMaxThread;
        private int maxThreadPerRequest;

        public Builder withGlobalMaxThread(int globalMaxThread) {
            this.globalMaxThread = globalMaxThread;
            return this;
        }

        public Builder withMaxThreadPerRequest(int maxThreadPerRequest) {
            this.maxThreadPerRequest = maxThreadPerRequest;
            return this;
        }

        public JedisClusterHelper build(JedisCluster cluster) {
            return new JedisClusterHelper(cluster, globalMaxThread, maxThreadPerRequest);
        }

    }
}
