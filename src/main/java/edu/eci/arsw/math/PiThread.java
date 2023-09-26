package edu.eci.arsw.math;

import java.util.ArrayList;
import java.util.Arrays;

public class PiThread extends Thread{
    public int start;
    public int count;
    public byte[] digits;
    public Object lock;

    public PiThread (int start, int count, Object lock){
        this.start = start;
        this.count = count;
        this.digits = new byte[count];
        this.lock = lock;

    }
    @Override
    public void run() {
        if (start < 0) {
            throw new RuntimeException("Invalid Interval");
        }

        if (count < 0) {
            throw new RuntimeException("Invalid Interval");
        }

        long startingTime = System.currentTimeMillis();
        byte[] digits = new byte[count];
        double sum = 0;
        long currentTime;
        for (int i = 0; i < count; i++) {
            currentTime = System.currentTimeMillis();
            if (currentTime - startingTime >= 5000) {
                System.out.println(Main.bytesToHex(digits));
                synchronized (lock) {
                    try {
                        lock.wait();
                        startingTime = System.currentTimeMillis();

                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else {
                if (i % PiDigits.DigitsPerSum == 0) {
                    sum = 4 * PiDigits.sum(1, start)
                            - 2 * PiDigits.sum(4, start)
                            - PiDigits.sum(5, start)
                            - PiDigits.sum(6, start);

                    start += PiDigits.DigitsPerSum;
                }
            }

            sum = 16 * (sum - Math.floor(sum));
            digits[i] = (byte) sum;
        }
    }



    public byte[] getDigits() {
        return digits;
    }
}
