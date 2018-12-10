package dataclasses;

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

    public int getDistance(int locationid, int[][] distancematrix){
       int distance = distancematrix[id][locationid];
       return distance;
    }



    public Depot getDichtsteDepot(ArrayList<Depot> depotslijst,int machineTypeId,int[][] distancematrix){
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

    public Depot getDichtstedepot(ArrayList<Depot> depotslijst,int[][] distancematrix){
        Depot depot;
        Depot bestedepot = null;
        int bestedistance = 999999;

        for(int i=0; i<depotslijst.size();i++) {
            depot = depotslijst.get(i);
                int distance = this.getDistance(depot.getLocation().getId(), distancematrix);
                if (distance < bestedistance) {
                    bestedepot = depot;
                    bestedistance = distance;
                }
            }
        return bestedepot;
    }

    public Truck getDichtsteDummyTruck(ArrayList<Depot> depotslijst,int[][] distancematrix){
        Depot depot;
        Depot bestedepot = null;
        int bestedistance = 999999;

        for(int i=0; i<depotslijst.size();i++) {
            depot = depotslijst.get(i);
                int distance = this.getDistance(depot.getLocation().getId(), distancematrix);
                if (distance < bestedistance) {
                    bestedepot = depot;
                    bestedistance = distance;
            }
        }


        return bestedepot.getDummytrucklijst().get(0);
    }



    public Collect getDichtsteCollect(ArrayList<Collect> collectlijst, int machineTypeId, int[][] distancematrix){
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



    public Truck getDichtsteTruck(ArrayList<Truck> trucklijst,Machine machine, int[][] distancematrix, int[][] timeMatrix, int depotlocationid){
        Truck truck;
        Truck bestetruck = null;
        int bestedistance = 999999;


        for(int i=0; i<trucklijst.size();i++) {
            truck = trucklijst.get(i);
            if(truck.heeftcapacity(machine)&&truck.heefttijdNieuw(machine.getLocation().getId(),timeMatrix,machine.getServicetime(),depotlocationid)) {    //truck moet machine hebben
                int distance = this.getDistance(truck.getHuidigeLocatie(), distancematrix);     //huidige locatie
                if (distance < bestedistance) {
                    bestetruck = truck;
                    bestedistance = distance;
                }
            }
        }
        return bestetruck;
    }




    public Truck getDichtsteTruck(ArrayList<Truck> trucklijst,Machine machine, int droplocatie, int[][] distancematrix, int[][] timeMatrix, int depotlocationid){
        Truck truck;
        Truck bestetruck = null;
        int bestedistance = 999999;


        for(int i=0; i<trucklijst.size();i++) {
            truck = trucklijst.get(i);
                if(truck.heeftcapacity(machine)&&truck.heefttijdNieuw(machine.getLocation().getId(),droplocatie,timeMatrix,machine.getServicetime(),depotlocationid)) {    //truck moet machine hebben
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
        return "dataclasses.Location{" +
                "id=" + id +
                ", latitude=" + latitude +
                ", longtitude=" + longtitude +
                ", name='" + name + '\'' +
                '}';
    }
}
