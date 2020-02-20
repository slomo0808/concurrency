package top.slomo.concurrency.example.publish;

import lombok.extern.slf4j.Slf4j;
import top.slomo.concurrency.annotations.NotThreadSafe;
import top.slomo.concurrency.annotations.UnRecommend;

@Slf4j
@NotThreadSafe
@UnRecommend
public class Escape {

    private int thisCanBeEscape = 0;

    public Escape() {
        new InnerClass();
    }

    private class InnerClass {

        public InnerClass() {
            log.info("{}", Escape.this.thisCanBeEscape);
        }
    }

    public static void main(String[] args) {
        new Escape();
    }
}
