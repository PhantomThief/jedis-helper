/**
 * 
 */
package com.github.phantomthief.jedis;

import java.util.BitSet;

import com.carrotsearch.hppc.Containers;
import com.carrotsearch.hppc.IntHashSet;
import com.carrotsearch.hppc.IntSet;

/**
 * @author w.vela
 */
public final class JedisUtils {

    private JedisUtils() {
        throw new UnsupportedOperationException();
    }

    public static IntSet toIntSet(final byte[] bytes) {
        IntSet bits = new IntHashSet(bytes == null ? 0 : Containers.DEFAULT_EXPECTED_ELEMENTS);
        if (bytes != null && bytes.length > 0) {
            for (int i = 0; i < (bytes.length * 8); i++) {
                if ((bytes[i / 8] & (1 << (7 - (i % 8)))) != 0) {
                    bits.add(i);
                }
            }
        }
        return bits;
    }

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
}
