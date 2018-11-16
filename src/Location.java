import java.util.ArrayList;

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

    public int getDistance(int locationid, DistanceMatrix distancematrix){
       int distance = distancematrix.getDistance()[id][locationid];
       return distance;
    }

    public int getTime(int locationid, TimeMatrix timematrix){
        int time = timematrix.getTime()[id][locationid];
        return time;
    }

    public Depot getDichtsteDepot(ArrayList<Depot> depotslijst,int machineTypeId,DistanceMatrix distancematrix){
        Depot depot;
        Depot bestedepot = null;
        int bestedistance = 999999;

       /* System.out.println("size: " + depotslijst.size());
        System.out.println("type nodig: " + machineTypeId);
        System.out.println("depot2: " + depotslijst.get(1).getMachinelijst().size());*/
        for(int i=0; i<depotslijst.size();i++) {
        //    System.out.println("depot: " + i);
            depot = depotslijst.get(i);
     /*       for(int j=0; j<depot.getMachinelijst().size();j++){
                System.out.println("depot: " + depot.getId() + " machine: " + depot.getMachinelijst().get(j).getName());
            }
*/
            if (depot.hasMachine(machineTypeId)) {
                int distance = this.getDistance(depot.getLocation().getId(), distancematrix);
                if (distance < bestedistance) {
                    bestedepot = depot;
                    bestedistance = distance;
                }
            }
        }


        return bestedepot;
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
