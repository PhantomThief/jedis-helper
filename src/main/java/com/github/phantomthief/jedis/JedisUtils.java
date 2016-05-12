/**
 * 
 */
package com.github.phantomthief.jedis;

import static com.carrotsearch.hppc.Containers.DEFAULT_EXPECTED_ELEMENTS;
import static com.google.common.collect.Iterables.partition;
import static com.google.common.collect.Maps.difference;
import static java.lang.Integer.MAX_VALUE;
import static java.util.stream.Collectors.toMap;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.BitSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import org.slf4j.Logger;

import com.carrotsearch.hppc.IntHashSet;
import com.carrotsearch.hppc.IntSet;
import com.google.common.collect.MapDifference;
import com.google.common.collect.MapDifference.ValueDifference;

import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.Tuple;

/**
 * @author w.vela
 */
public final class JedisUtils {

    private static final int MAX_ZSET_COUNT = MAX_VALUE / 2;
    private static final int ZADD_MAX_SPLIT_COUNT = 1000;

    private static Logger logger = getLogger(JedisUtils.class);

    private JedisUtils() {
        throw new UnsupportedOperationException();
    }

    public static IntSet toIntSet(final byte[] bytes) {
        IntSet bits = new IntHashSet(bytes == null ? 0 : DEFAULT_EXPECTED_ELEMENTS);
        if (bytes != null && bytes.length > 0) {
            for (int i = 0; i < (bytes.length * 8); i++) {
                if ((bytes[i / 8] & (1 << (7 - (i % 8)))) != 0) {
                    bits.add(i);
                }
            }
        }
        return bits;
    }

    /*public static <T> boolean syncHashKey(JedisCommands jedis, T key,
            Function<T, String> keyGenerator, Function<T, Map<String, String>> dataBuilder,
            boolean appendOnly) {
        String realKey = keyGenerator.apply(key);
        boolean changed = false;
        if (jedis.exists(realKey)) {
            Map<String, String> raw = dataBuilder.apply(key);
            Map<String, String> exists = jedis.hgetAll(realKey);
        }
    }*/

    public static BitSet toBitSet(final byte[] bytes) {
        BitSet bits = new BitSet();
        if (bytes != null && bytes.length > 0) {
            for (int i = 0; i < (bytes.length * 8); i++) {
                if ((bytes[i / 8] & (1 << (7 - (i % 8)))) != 0) {
                    bits.set(i);
                }
            }
        }
        return bits;
    }

    public static <T> boolean syncSortedSetKey(JedisCommands jedis, T key,
            Function<T, String> keyGenerator, Function<T, Map<String, Double>> dataBuilder) {
        String realKey = keyGenerator.apply(key);
        boolean changed = false;
        if (jedis.exists(realKey)) {
            Map<String, Double> raw = dataBuilder.apply(key);
            Map<String, Double> exists = jedis.zrevrangeWithScores(realKey, 0, MAX_ZSET_COUNT)
                    .stream().collect(toMap(Tuple::getElement, Tuple::getScore));
            MapDifference<String, Double> difference = difference(raw, exists);
            if (!difference.areEqual()) {
                if (!difference.entriesOnlyOnLeft().isEmpty()) {
                    for (List<Entry<String, Double>> list : partition(
                            difference.entriesOnlyOnLeft().entrySet(), ZADD_MAX_SPLIT_COUNT)) {
                        jedis.zadd(realKey,
                                list.stream().collect(toMap(Entry::getKey, Entry::getValue)));
                        changed = true;
                    }
                    logger.info("found {} real key:{} has data to set cache:{}", jedis, realKey,
                            difference.entriesOnlyOnLeft());
                }
                if (!difference.entriesOnlyOnRight().isEmpty()) {
                    for (List<String> list : partition(difference.entriesOnlyOnRight().keySet(),
                            ZADD_MAX_SPLIT_COUNT)) {
                        jedis.zrem(realKey, list.stream().toArray(String[]::new));
                        changed = true;
                    }
                    logger.info("found {} real key:{} has data to remove from cache:{}", jedis,
                            realKey, difference.entriesOnlyOnRight().keySet());
                }
                if (!difference.entriesDiffering().isEmpty()) {
                    for (List<Entry<String, ValueDifference<Double>>> list : partition(
                            difference.entriesDiffering().entrySet(), ZADD_MAX_SPLIT_COUNT)) {
                        jedis.zadd(realKey, list.stream() //
                                .filter(e -> e.getValue().leftValue() != null) //
                                .collect(toMap(Entry::getKey, e -> e.getValue().leftValue())));
                        changed = true;
                    }
                    logger.info("found {} real key:{} has data to set cache:{}", jedis, realKey,
                            difference.entriesOnlyOnLeft());
                }
            }
        }
        return changed;
    }
}
