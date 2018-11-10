import java.util.Arrays;

public class Timematrix {

    private int [] [] time;


    public Timematrix(int lengte, int breedte){
        this.time = new int [lengte] [breedte];
    }


    public Timematrix(int[][] time) {
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


