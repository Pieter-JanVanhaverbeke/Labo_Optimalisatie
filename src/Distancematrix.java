import java.util.Arrays;

public class Distancematrix {
    private int [] [] distance;

    public Distancematrix(int[][] distance) {
        this.distance = distance;
    }

    public Distancematrix(int lengte, int breedte){
        this.distance = new int [lengte] [breedte];
    }

    public int[][] getDistance() {
        return distance;
    }

    public void setDistance(int[][] distance) {
        this.distance = distance;
    }

    public void addDistance(int x, int y, int distancewaarde){
        distance [x][y] = distancewaarde;
    }


    @Override
    public String toString() {
        return "Distancematrix{" +
                "distance=" + Arrays.toString(distance) +
                '}';
    }
}


