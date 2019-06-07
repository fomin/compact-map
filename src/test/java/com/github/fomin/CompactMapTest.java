package com.github.fomin;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.github.fomin.CompactMap.Entry;
import static com.github.fomin.CompactMap.entry;
import static com.github.fomin.CompactMap.factory;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class CompactMapTest {

    @Test
    public void test2() {
        CompactMap.Factory<String, Integer> factory = factory(
                asList(
                        "0",
                        "1",
                        "2",
                        "3",
                        "4"
                ),
                asList(
                        0,
                        1
                )
        );

        CompactMap<String, Integer> map = factory.create(
                asList(
                        entry("0", 0),
                        entry("2", 0),
                        entry("3", 1)
                )
        );

        assertEquals(Integer.valueOf(0), map.get("0"));
        assertEquals(Integer.valueOf(0), map.get("2"));
        assertEquals(Integer.valueOf(1), map.get("3"));
    }

    @Test
    public void randomTest() {
        Random random = new Random(0);

        for (int testIteration = 0; testIteration < 100; testIteration++) {
            int possibleKeyCount = random.nextInt(0x10000);
            List<String> possibleKeys = new ArrayList<>(possibleKeyCount);
            for (int i = 0; i < possibleKeyCount; i++) {
                possibleKeys.add(String.format("%d", i));
            }

            int possibleValueCount = random.nextInt(0x10000);
            List<Integer> possibleValues = new ArrayList<>(possibleValueCount);
            for (int i = 0; i < possibleValueCount; i++) {
                possibleValues.add(i);
            }

            int entryCount = random.nextInt(possibleKeyCount);
            List<Entry<String, Integer>> entries = new ArrayList<>(entryCount);
            Map<String, Integer> referenceMap = new HashMap<>(entryCount, 1.0f);
            for (int i = 0; i < entryCount; i++) {
                String key = possibleKeys.get(random.nextInt(possibleKeyCount));
                Integer value = possibleValues.get(random.nextInt(possibleValueCount));
                referenceMap.put(key, value);
            }

            referenceMap.forEach((key, value) -> entries.add(entry(
                    key,
                    value
            )));

            Collections.sort(possibleKeys);
            Collections.sort(possibleValues);
            entries.sort(Comparator.comparing(o -> o.key));

            CompactMap<String, Integer> map = factory(possibleKeys, possibleValues).create(entries);

            for (Map.Entry<String, Integer> referenceEntry : referenceMap.entrySet()) {
                String key = referenceEntry.getKey();
                Integer referenceValue = referenceEntry.getValue();
                Integer value = map.get(key);
                assertEquals(referenceValue, value);
            }

        }
    }
}
