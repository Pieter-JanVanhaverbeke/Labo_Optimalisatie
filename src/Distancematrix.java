import java.util.Arrays;

public class Distancematrix {
    private int [] [] distance;

    public Distancematrix(int[][] distance) {
        this.distance = distance;
    }

    public int[][] getDistance() {
        return distance;
    }

    public void setDistance(int[][] distance) {
        this.distance = distance;
    }


    @Override
    public String toString() {
        return "Distancematrix{" +
                "distance=" + Arrays.toString(distance) +
                '}';
    }
}


