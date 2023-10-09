package main.java.util;

import org.jetbrains.annotations.Range;

public class RandomUtil {
    private RandomUtil() {

    }
    @Range(from = 0, to = 100)
    public boolean ofChance(double chance) {
        if (chance == 100) {
            return true;
        }
        if (chance == 0) {
            return false;
        }
        return Math.random() <= chance / 100;
    }
}
