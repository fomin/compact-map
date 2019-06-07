package com.github.fomin;

import java.util.Arrays;
import java.util.Collection;

public final class CompactMap<K extends Comparable<K>, V extends Comparable<V>> {

    @SuppressWarnings("unchecked")
    public static final class Factory<K extends Comparable<K>, V extends Comparable<V>> {
        private final K[] possibleKeys;
        private final V[] possibleValues;

        private Factory(Collection<K> possibleKeys, Collection<V> possibleValues) {
            this.possibleKeys = (K[]) possibleKeys.toArray(new Comparable[possibleKeys.size()]);
            this.possibleValues = (V[]) possibleValues.toArray(new Comparable[possibleValues.size()]);

            for (int i = 1; i < this.possibleKeys.length; i++) {
                if (this.possibleKeys[i - 1].compareTo(this.possibleKeys[i]) >= 0) {
                    throw new IllegalArgumentException("Keys must be ordered and unique");
                }
            }

            for (int i = 1; i < this.possibleValues.length; i++) {
                if (this.possibleValues[i - 1].compareTo(this.possibleValues[i]) >= 0) {
                    throw new IllegalArgumentException("Values must be ordered and unique");
                }
            }
        }

        public CompactMap<K, V> create(Collection<Entry<K, V>> entries) {
            return new CompactMap<>(
                    possibleKeys,
                    possibleValues,
                    entries.toArray(new Entry[entries.size()])
            );
        }

    }

    public static final class Entry<K, V> {
        final K key;
        final V value;

        private Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private final K[] allKeys;
    private final V[] allValues;
    private final int keyShift;
    private final int valueShift;
    private final long[] keyWords;
    private final long[] valueWords;
    private int length;

    private CompactMap(K[] allKeys, V[] allValues, Entry<K, V>[] entries) {
        this.allKeys = allKeys;
        this.allValues = allValues;

        this.keyShift = shift(allKeys.length);
        this.valueShift = shift(allValues.length);
        this.keyWords = new long[-((-entries.length) >> keyShift)];
        this.valueWords = new long[-((-entries.length) >> valueShift)];
        this.length = entries.length;

        int bitsPerKey = 64 >>> keyShift;
        int bitsPerValue = 64 >>> valueShift;

        int keyIndex = -1;
        for (int i = 0; i < entries.length; i++) {
            if (i > 0 && entries[i - 1].key.compareTo(entries[i].key) >= 0) {
                throw new IllegalArgumentException("Keys must be ordered and unique");
            }

            keyIndex = Arrays.binarySearch(allKeys, keyIndex + 1, allKeys.length, entries[i].key);
            if (keyIndex < 0) {
                throw new IllegalArgumentException("There is no key '" + entries[i].key + "' in possible keys");
            }
            int keyWordIndex = i >>> keyShift;
            int keyWordSubIndex = i & ((1 << keyShift) - 1);
            this.keyWords[keyWordIndex] |= (long) keyIndex << (bitsPerKey * keyWordSubIndex);

            long valueIndex = Arrays.binarySearch(allValues, entries[i].value);
            if (valueIndex < 0) {
                throw new IllegalArgumentException("There is no value '" + entries[i].value + "' in possible values");
            }
            int valueWordIndex = i >>> valueShift;
            int valueWordSubIndex = i & ((1 << valueShift) - 1);
            this.valueWords[valueWordIndex] |= valueIndex << (bitsPerValue * valueWordSubIndex);
        }
    }

    public V get(K key) {
        int keyIndex = Arrays.binarySearch(allKeys, key);
        int bitsPerKey = 64 >>> keyShift;
        int bitsPerValue = 64 >>> valueShift;
        long keyMask = (1L << bitsPerKey) - 1L;
        long valueMask = (1L << bitsPerValue) - 1L;
        int keySubIndexMask = (1 << keyShift) - 1;
        int valueSubIndexMask = (1 << valueShift) - 1;

        int left = 0;
        int right = length - 1;
        while (left <= right) {
            int mid = (left + right) >>> 1;

            int keyWordIndex = mid >>> keyShift;
            int keyWordSubindex = mid & keySubIndexMask;
            long k = (this.keyWords[keyWordIndex] >>> (bitsPerKey * keyWordSubindex)) & keyMask;

            if (k < keyIndex) {
                left = mid + 1;
            } else if (k > keyIndex) {
                right = mid - 1;
            } else {
                int valueWordIndex = mid >>> valueShift;
                int valueWordSubIndex = mid & valueSubIndexMask;
                long v = (this.valueWords[valueWordIndex] >>> (bitsPerValue * valueWordSubIndex)) & valueMask;
                return allValues[(int) v];
            }
        }

        return null;
    }

    private static int shift(int keyCount) {
        if (keyCount <= 1 << 1) {
            return 6;
        } else if (keyCount <= 1 << 2) {
            return 5;
        } else if (keyCount <= 1 << 4) {
            return 4;
        } else if (keyCount <= 1 << 8) {
            return 3;
        } else if (keyCount <= 1 << 16) {
            return 2;
        } else {
            return 1;
        }
    }

    public static <K extends Comparable<K>, V extends Comparable<V>> Entry<K, V> entry(K key, V value) {
        return new Entry<>(key, value);
    }

    public static <K extends Comparable<K>, V extends Comparable<V>> Factory<K, V> factory(
            Collection<K> possibleKeys,
            Collection<V> possibleValues
    ) {
        return new Factory<>(possibleKeys, possibleValues);
    }

}
