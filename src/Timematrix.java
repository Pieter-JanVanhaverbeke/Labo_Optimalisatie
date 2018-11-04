import java.util.Arrays;

public class Timematrix {

    private int [] [] time;

    public Timematrix(int[][] time) {
        this.time = time;
    }

    public int[][] getTime() {
        return time;
    }

    public void setTime(int[][] time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Timematrix{" +
                "time=" + Arrays.toString(time) +
                '}';
    }
}


