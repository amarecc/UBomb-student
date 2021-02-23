package fr.ubx.poo.utils;

public class Timer {
    private int ms;
    private int sec;

    public Timer(double sec){
        this.sec = (int) Math.ceil(sec); // Round up
        if(sec%1 != 0) this.ms = (int) (sec%1 * 60); // sec%1 to get the numbers after the point
        else this.ms = 60;
    }

    public int getSec() {
        return this.sec;
    }

	public void decreaseTimer() {
        this.ms--;
        if(this.ms == 0){
            this.ms = 60;
            this.sec --;
        }
	}
}