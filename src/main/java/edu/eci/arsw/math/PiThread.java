package edu.eci.arsw.math;

import java.util.ArrayList;
import java.util.Arrays;

public class PiThread extends Thread{
    public int start;
    public int count;
    public byte[] digits;
    public Object lock;
    public String hexaDigits;

    public PiThread (int start, int count, Object lock){
        this.start = start;                 // Primer valor que hallará el hilo (inicio del intervalo)
        this.count = count;                 // Cantidad de dígitos que hallará el hilo
        this.digits = new byte[count];      // array de bytes que contendrá todos los dígitos que halle el hilo
        this.lock = lock;                   // lock o monitor para dormir hilo

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
            if (currentTime - startingTime >= 5000) {               // Cada cinco segundos, si aún no ha finalizado el hilo su ejecución, se duerme y muestra
                System.out.println(Main.bytesToHex(digits));        // en pantalla su avance
                synchronized (lock) {
                    try {
                        lock.wait();
                        startingTime = System.currentTimeMillis();  // Se vuelve a iniciar conteo una vez se despierten los hilos desde el main
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            if (i % PiDigits.DigitsPerSum == 0) {
                sum = 4 * PiDigits.sum(1, start)
                        - 2 * PiDigits.sum(4, start)
                        - PiDigits.sum(5, start)
                        - PiDigits.sum(6, start);

                start += PiDigits.DigitsPerSum;
            }


            sum = 16 * (sum - Math.floor(sum));
            digits[i] = (byte) sum;                                 // Cada dígito se va añadiendo al array de bytes
        }
        hexaDigits = Main.bytesToHex(digits);                       // Los bytes se pasan a hexadecimal, y se almacenan en un string
    }



    public byte[] getDigits() {
        return digits;
    }

    public String getHexaDigits() {
        return hexaDigits;
    }
}
