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

        for(int i=0; i<depotslijst.size();i++) {
            depot = depotslijst.get(i);
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

    public Collect getDichtsteCollect(ArrayList<Collect> collectlijst, int machineTypeId,DistanceMatrix distancematrix){
        Collect collect;
        Collect bestecollect = null;
        int bestedistance = 999999;


        for(int i=0; i<collectlijst.size();i++) {
            collect = collectlijst.get(i);

            if (collect.getMachine().getMachineTypeId()==machineTypeId) {
                int distance = this.getDistance(collect.getMachine().getLocation().getId(), distancematrix);
                if (distance < bestedistance) {
                    bestecollect = collect;
                    bestedistance = distance;
                }
            }
        }

        return bestecollect;
    }



    public Truck getDichtsteTruck(ArrayList<Truck> trucklijst,Machine machine, DistanceMatrix distancematrix, TimeMatrix timeMatrix){
        Truck truck;
        Truck bestetruck = null;
        int bestedistance = 999999;


        for(int i=0; i<trucklijst.size();i++) {
            truck = trucklijst.get(i);
            if(truck.heeftcapacity(machine)&&truck.heefttijd(machine.getLocation().getId(),timeMatrix,machine.getServicetime())) {    //truck moet machine hebben
                int distance = this.getDistance(truck.getHuidigeLocatie(), distancematrix);     //huidige locatie
                if (distance < bestedistance) {
                    bestetruck = truck;
                    bestedistance = distance;
                }
            }
        }
        return bestetruck;
    }




    public Truck getDichtsteTruck(ArrayList<Truck> trucklijst,Machine machine, int droplocatie, DistanceMatrix distancematrix, TimeMatrix timeMatrix){
        Truck truck;
        Truck bestetruck = null;
        int bestedistance = 999999;


        for(int i=0; i<trucklijst.size();i++) {
            truck = trucklijst.get(i);
                if(truck.heeftcapacity(machine)&&truck.heefttijd(machine.getLocation().getId(),droplocatie,timeMatrix,machine.getServicetime())) {    //truck moet machine hebben
                    int distance = this.getDistance(truck.getHuidigeLocatie(), distancematrix);     //huidige locatie
                    if (distance < bestedistance) {
                        bestetruck = truck;
                        bestedistance = distance;
                    }
                }
            }
            return bestetruck;
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
