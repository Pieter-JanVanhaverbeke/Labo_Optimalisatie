public class Location {
    private int id;
    private double latitude;
    private double longtitude;
    private String name;


    public Location(int id, double latitude, double longtitude, String name) {
        this.id = id;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDistance(int locationid, Distancematrix distancematrix){
       int distance = distancematrix.getDistance()[id][locationid];
       return distance;
    }

    public int getTime(int locationid, Timematrix timematrix){
        int time = timematrix.getTime()[id][locationid];
        return time;
    }


    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", latitude=" + latitude +
                ", longtitude=" + longtitude +
                ", name='" + name + '\'' +
                '}';
    }
}
