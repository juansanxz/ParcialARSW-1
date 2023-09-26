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
        System.out.println(bytesToHex(PiDigits.getDigits(1, 100))); //Para probar
        int n = 5;                       // Número de Hilos
        int start = 1;                   // Posición desde donde se desean calcular una cantidad de digitos determinada
        int count = 100;                // Cantidad de dígitos que se desean calcular
        int lastCount;                   // Número de dígitos que va a calcular el último hilo que se cree
        int interval = count/n;          // Cantidad de dígitos que van a calcular todos los hilos
        Object lock = new Object();      // Lock o monitor para que los hilos se detengan por su cuenta cada cinco segundos

        int startAt;                     // Posicion desde la que cada hilo empezará a buscar dígitos

        ArrayList<PiThread> threads = new ArrayList<PiThread>();      // Lista de hilos para posteriormente concatenar resultados
        for (int i = 0; i < n; i++) {                          // Ciclo para a creación de hilos, teniendo en cuenta si se desea empezar la
                                                               // búsqueda de dígitos desde un valor igual o mayor a cero, para saber con exactitud el valor inicial
            if (start > 0) {                                   // que se le enviará a cada hilo
                startAt = start + interval*i;

            } else {
                startAt = i*interval;
            }

            if(i == n-1){                                       // Al último hilo se le envía el número restante de dígitos a calcular
                lastCount = count + start - startAt;
                threads.add(new PiThread(startAt, lastCount, lock));

            } else {
                threads.add(new PiThread(startAt, interval, lock));
            }
        }

        for (Thread thread : threads){                           // Se inicia la ejecución de los hilos
            thread.start();
        }

        boolean stop = false;
        while(!stop) {                                           // El ciclo while finaliza cuando los hilos hayan finalizado su ejecución
            try {                                                // de lo contrario, cada cinco segundos se muestra en pantalla el avance de cada hilo
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
                } else {                                           // Si el usuario da enter, se continúa con la búsqueda de los dígitos
                    synchronized (lock) {
                        lock.notifyAll();
                    }
                }
            }
        }

        for (PiThread piThread : threads){
            try {
                piThread.join();                                    // Se espera a que termine cada hilo
                System.out.print(piThread.getHexaDigits());         // Imprimiendo en una sola línea ordenada, los dígitos que cada uno buscó
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
