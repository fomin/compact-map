package com.github.fomin;

import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

@State(Scope.Benchmark)
public class BenchmarkState {
    private static final int KEY_COUNT = 1000;
    private static final int VALUE_COUNT = 10;
    private static final int ENTRY_COUNT = 100;

    final CompactMap.Factory<String, Integer> factory;
    final List<CompactMap.Entry<String, Integer>> entries;
    final CompactMap<String, Integer> compactMap;
    final HashMap<String, Integer> hashMap;
    final TreeMap<String, Integer> treeMap;
    final String testKey0;
    final String testKey1;
    final String testKey2;
    final String testKey3;

    public BenchmarkState() {
        Random random = new Random(0);

        Set<String> possibleKeys = new HashSet<>(KEY_COUNT);
        do {
            possibleKeys.add(String.valueOf(random.nextInt()));
        } while (possibleKeys.size() < KEY_COUNT);
        List<String> possibleKeyList = new ArrayList<>(possibleKeys);
        Collections.sort(possibleKeyList);

        Set<Integer> possibleValues = new HashSet<>(VALUE_COUNT);
        do {
            possibleValues.add(random.nextInt());
        } while (possibleValues.size() < VALUE_COUNT);
        List<Integer> possibleValueList = new ArrayList<>(possibleValues);
        Collections.sort(possibleValueList);

        hashMap = new HashMap<>(ENTRY_COUNT);
        treeMap = new TreeMap<>();
        do {
            String key = possibleKeyList.get(random.nextInt(possibleKeyList.size()));
            Integer value = possibleValueList.get(random.nextInt(possibleValueList.size()));
            hashMap.put(key, value);
            treeMap.put(key, value);
        } while (hashMap.size() < ENTRY_COUNT);

        entries = new ArrayList<>(KEY_COUNT);

        for (Map.Entry<String, Integer> entry : hashMap.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            entries.add(CompactMap.entry(key, value));
        }
        entries.sort(Comparator.comparing(o -> o.key));

        factory = CompactMap.factory(possibleKeyList, possibleValueList);
        compactMap = factory.create(entries);

        testKey0 = possibleKeyList.get(random.nextInt(possibleKeyList.size()));
        testKey1 = possibleKeyList.get(random.nextInt(possibleKeyList.size()));
        testKey2 = possibleKeyList.get(random.nextInt(possibleKeyList.size()));
        testKey3 = possibleKeyList.get(random.nextInt(possibleKeyList.size()));
    }
}
