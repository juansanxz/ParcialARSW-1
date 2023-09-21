package edu.eci.arsw.math;

public class PiThread extends Thread{
    public int start;
    public int count;
    public byte[] digits;
    public PiThread (int start, int count){
        this.start = start;
        this.count = count;
    }
    @Override
    public void run() {
        digits = PiDigits.getSomeDigits(start, start, count, digits);
    }


}
