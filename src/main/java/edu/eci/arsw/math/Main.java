/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author hcadavid
 */
public class Main {

    public static void main(String a[]) {
        System.out.println(bytesToHex(PiDigits.getDigits(6, 1000))); //Para probar
        int n = 2;                      // Número de Hilos
        int start = 6;                  // Posición desde conde se desean calcular una cantidad digitos
        int count = 30000;               //
        int lastCount;
        int interval = count/n;
        Object lock = new Object();

        int startAt;

        ArrayList<PiThread> threads = new ArrayList<PiThread>();
        for (int i = 0; i < n; i++) {

            if (start > 0) {
                startAt = start + interval*i;

            } else {
                startAt = i*interval;
            }

            if(i == n-1){
                lastCount = count + start - startAt;
                threads.add(new PiThread(startAt, lastCount, lock));

            } else {
                threads.add(new PiThread(startAt, interval, lock));
            }
        }

        for (Thread thread : threads){
            thread.start();
        }

        boolean stop = false;
        while(!stop) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (Thread.activeCount() == 2) {
                stop = true;
            } else {
                Scanner enter = new Scanner(System.in);
                System.out.println("Presione la tecla Enter para continuar calculando los dígitos.");
                String enterString = enter.nextLine();
                if (!enterString.equals("")) {
                    stop = true;
                } else {
                    synchronized (lock) {
                        lock.notifyAll();
                    }
                }
            }
        }

        for (PiThread piThread : threads){
            try {
                piThread.join();
                System.out.print(bytesToHex(piThread.getDigits()));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        //System.out.println(bytesToHex(PiDigits.getDigits(1, 10)));
        //System.out.println(bytesToHex(PiDigits.getDigits(1, 100)));
        //System.out.println(bytesToHex(PiDigits.getDigits(1, 1000000)));
    }

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        StringBuilder sb=new StringBuilder();
        for (int i=0;i<hexChars.length;i=i+2){
            //sb.append(hexChars[i]);
            sb.append(hexChars[i+1]);            
        }
        return sb.toString();
    }

}
