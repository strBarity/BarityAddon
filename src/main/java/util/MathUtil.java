package main.java.util;

import org.jetbrains.annotations.NotNull;

public class MathUtil {
    private MathUtil() {

    }

    public static @NotNull String integerToRoman(int i) {
        int[] a = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] r = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        StringBuilder s = new StringBuilder();
        for (int x = 0; x < a.length; x++) {
            while (i >= a[x]) {
                i = i - a[x];
                s.append(r[x]);
            }
        }
        return s.toString();
    }

    public static double roundAt(double value, int digit) {
        double digitValue = Math.pow(10, digit);
        return Math.round(value * digitValue) / digitValue;
    }
}
