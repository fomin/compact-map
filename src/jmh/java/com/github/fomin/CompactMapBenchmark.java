package com.github.fomin;

import org.openjdk.jmh.annotations.Benchmark;

import java.util.HashMap;
import java.util.TreeMap;

@SuppressWarnings("unused")
public class CompactMapBenchmark {

    @Benchmark
    public CompactMap<String, Integer> createCompactMap(BenchmarkState state) {
        return state.factory.create(state.entries);
    }

    @Benchmark
    public HashMap<String, Integer> createHashMap(BenchmarkState state) {
        HashMap<String, Integer> hashMap = new HashMap<>(500);
        for (CompactMap.Entry<String, Integer> entry : state.entries) {
            hashMap.put(entry.key, entry.value);
        }
        return hashMap;
    }

    @Benchmark
    public TreeMap<String, Integer> createTreeMap(BenchmarkState state) {
        TreeMap<String, Integer> treeMap = new TreeMap<>();
        for (CompactMap.Entry<String, Integer> entry : state.entries) {
            treeMap.put(entry.key, entry.value);
        }
        return treeMap;
    }

    @Benchmark
    public Integer getTestKey0CompactMap(BenchmarkState state) {
        return state.compactMap.get(state.testKey0);
    }

    @Benchmark
    public Integer getTestKey0HashMap(BenchmarkState state) {
        return state.hashMap.get(state.testKey0);
    }

    @Benchmark
    public Integer getTestKey0TreeMap(BenchmarkState state) {
        return state.treeMap.get(state.testKey0);
    }

    @Benchmark
    public Integer getTestKey1CompactMap(BenchmarkState state) {
        return state.compactMap.get(state.testKey1);
    }

    @Benchmark
    public Integer getTestKey1HashMap(BenchmarkState state) {
        return state.hashMap.get(state.testKey1);
    }

    @Benchmark
    public Integer getTestKey1TreeMap(BenchmarkState state) {
        return state.treeMap.get(state.testKey1);
    }

    @Benchmark
    public Integer getTestKey2CompactMap(BenchmarkState state) {
        return state.compactMap.get(state.testKey2);
    }

    @Benchmark
    public Integer getTestKey2HashMap(BenchmarkState state) {
        return state.hashMap.get(state.testKey2);
    }

    @Benchmark
    public Integer getTestKey2TreeMap(BenchmarkState state) {
        return state.treeMap.get(state.testKey2);
    }

    @Benchmark
    public Integer getTestKey3CompactMap(BenchmarkState state) {
        return state.compactMap.get(state.testKey3);
    }

    @Benchmark
    public Integer getTestKey3HashMap(BenchmarkState state) {
        return state.hashMap.get(state.testKey3);
    }

    @Benchmark
    public Integer getTestKey3TreeMap(BenchmarkState state) {
        return state.treeMap.get(state.testKey3);
    }

    @Benchmark
    public Integer getNonExistentCompactMap(BenchmarkState state) {
        return state.compactMap.get("nonExistent");
    }

    @Benchmark
    public Integer getNonExistentHashMap(BenchmarkState state) {
        return state.hashMap.get("nonExistent");
    }

    @Benchmark
    public Integer getNonExistentTreeMap(BenchmarkState state) {
        return state.treeMap.get("nonExistent");
    }

}
