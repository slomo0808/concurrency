package top.slomo.concurrency.example.immutable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import top.slomo.concurrency.annotations.ThreadSafe;

import java.util.Collections;
import java.util.Map;

@Slf4j
@ThreadSafe
public class ImmutableExample3 {

    private final static ImmutableList list = ImmutableList.of(1, 2, 3);

    private final static ImmutableList set = ImmutableList.copyOf(list);

    private final static ImmutableMap<Integer, Integer> map =
            ImmutableMap.of(1, 2, 3, 4, 5, 6);

    private final static ImmutableMap<Integer, Integer> map2 =
            ImmutableMap.<Integer, Integer>builder()
                    .put(1, 2).put(3, 4).put(5, 6).build();

    public static void main(String[] args) {
        // UnsupportedOperationException
        // list.add(4);
        // set.add(4);
        // map.put(1, 4);
        // map2.put(1, 4);
    }
}
