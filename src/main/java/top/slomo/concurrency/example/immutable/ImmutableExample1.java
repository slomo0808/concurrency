package top.slomo.concurrency.example.immutable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import top.slomo.concurrency.annotations.NotThreadSafe;

import java.util.Map;

@Slf4j
@NotThreadSafe
public class ImmutableExample1 {

    private final static Integer a = 1;
    private final static String b = "2";
    private final static Map<Integer, Integer> map = Maps.newHashMap();

    static {
        map.put(1, 2);
        map.put(3, 4);
        map.put(5, 6);
    }

    public static void main(String[] args) {
        // wrong
        // a = 1;
        // b = "3";
        // map = Maps.newHashMap();

        map.put(1, 3);
        log.info("map:{}", map);
    }

    private void test(final int a) {
        // wrong
        // a = 1;
        ImmutableList<Integer> list = ImmutableList.of(1, 2, 3, 4, 5, 6);

    }
}
