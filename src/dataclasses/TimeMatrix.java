package dataclasses;

import java.util.Arrays;

public class TimeMatrix {

    private int [] [] time;


    public TimeMatrix(int lengte, int breedte){
        this.time = new int [lengte] [breedte];
    }


    public TimeMatrix(int[][] time) {
        this.time = time;
    }

    public int[][] getTime() {
        return time;
    }

    public void setTime(int[][] time) {
        this.time = time;
    }

    public void addTime(int x, int y, int timewaarde){
        time [x][y] = timewaarde;
    }



    @Override
    public String toString() {
        return "Timematrix{" +
                "time=" + Arrays.toString(time) +
                '}';
    }
}


