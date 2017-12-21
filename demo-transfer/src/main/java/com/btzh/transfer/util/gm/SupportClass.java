package com.btzh.transfer.util.gm;

/**
 *
 * @author huangganquan
 * @date 2017/9/25
 */
public class SupportClass {

    public static int urShift(int number, int bits) {
        if (number >= 0) {
            return number >> bits;
        }

        return (number >> bits) + (2 << ~bits);
    }

    public static int urShift(int number, long bits) {
        return urShift(number, (int) bits);
    }

    public static long urShift(long number, int bits) {
        if (number >= 0) {
            return number >> bits;
        }

        return (number >> bits) + (2L << ~bits);
    }

    public static long urShift(long number, long bits) {
        return urShift(number, (int) bits);
    }
}