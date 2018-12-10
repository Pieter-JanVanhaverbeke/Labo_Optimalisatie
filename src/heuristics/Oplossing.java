package heuristics;

import dataclasses.*;
import solution.Solution;

import java.io.File;
import java.util.ArrayList;

public class Oplossing {

    private Data data;
    private Solution solution;




    public Oplossing(Data data) {
        this.data = data;
        this.data.buildLastArrays();
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Solution start(long seed) {
        ArrayList<Drop> droplijst = data.getDroplijst();
        ArrayList<Collect> collectlijst = data.getCollectlijst();
        ArrayList<Depot> depotlijst = data.getDepotlijst();
        ArrayList<Truck> trucklijst = data.getTrucklijst();
        ArrayList<Location> locationlijst = data.getLocationlijst();
        int [] [] timematrix = data.getTimeMatrix();
        int [] [] distancematrix = data.getDistancematrix();
        Collect dichtstecollect;
        Truck bestetruck;
        Machine machine = null;
        Stop stop = null;
        boolean verplaats = false;
        int collectId;


        for (int i = 0; i < droplijst.size(); i++) {

            Drop drop = droplijst.get(i);
            int machinetypeId = drop.getMachineTypeId();
            dichtstecollect = drop.getLocation().getDichtsteCollect(collectlijst, machinetypeId, distancematrix);       //dichtste collect
            Depot dichtsteDepot = drop.getLocation().getDichtsteDepot(depotlijst, machinetypeId, distancematrix);
            int dichtstecollectdistance = 999999;
            int dichtstedepotdistance = 999999;

            if (dichtstecollect != null) {
                dichtstecollectdistance = drop.getLocation().getDistance(dichtstecollect.getMachine().getLocation().getId(), distancematrix);
            }

            if (dichtsteDepot != null) {
                dichtstedepotdistance = drop.getLocation().getDistance(dichtsteDepot.getLocation().getId(), distancematrix);
            }


            if (dichtstecollectdistance < 2 * dichtstedepotdistance) {              //2 maal dichtere depot, depot nemen, anders machine van pickup
                machine = dichtstecollect.getMachine();  //machine van collect
                collectlijst.remove(dichtstecollect);   //removing collect uit lijst
                collectId = dichtstecollect.getId();
            } else {                                       //geen machine gevonden van collect
                machine = dichtsteDepot.getMachine(drop.getMachineTypeId());
                dichtsteDepot.removeMachine(machine);           //verwijderen uit depotlijst
                collectId = -1;
            }

            Depot dichtsteDepot2 = drop.getLocation().getDichtstedepot(depotlijst, distancematrix);
            bestetruck = machine.getLocation().getDichtsteTruck(trucklijst, machine, drop.getLocation().getId(), distancematrix, timematrix,dichtsteDepot2.getLocation().getId());  //dichtste depot

            //ALS NIET COLLECT OPNEMEN, DUMMYTRUCK
            if (bestetruck == null) {
              //  bestetruck = machine.getLocation().getDichtsteDummyTruck(depotlijst, distancematrix);
                bestetruck  = dichtsteDepot.getDummytrucklijst().get(0);
                bestetruck.verplaats(machine.getLocation().getId(), timematrix, distancematrix);

                //ADDEN STOP DUMMYTRUCK
                stop = new Stop(machine.getLocation().getId());
                stop.addMachine(machine, collectId);
                bestetruck.setHuidigestop(stop);
                bestetruck.addStop(stop);

                //VERPLAATSEN NAAR COLLECT
                bestetruck.verplaats(drop.getLocation().getId(), timematrix, distancematrix);
                stop = new Stop(drop.getLocation().getId());
                stop.addMachine(machine, drop.getId());
                bestetruck.addStop(stop);
            } else {


                int beginlocatie = bestetruck.getHuidigeLocatie();
                verplaats = bestetruck.verplaats(machine.getLocation().getId(), timematrix, distancematrix);
                bestetruck.pickUp(machine, collectId);

                if (verplaats) {
                    stop = new Stop(beginlocatie);   //stop toekennen als truck verplaatst
                    bestetruck.addStop(stop);
                }

                stop = new Stop(machine.getLocation().getId());
                stop.addMachine(machine, collectId);
                bestetruck.setHuidigestop(stop);
                bestetruck.addStop(stop);


                bestetruck.verplaats(drop.getLocation().getId(), timematrix, distancematrix);
                bestetruck.dropOf(machine);

                stop = new Stop(drop.getLocation().getId());
                stop.addMachine(machine, drop.getId());
                bestetruck.setHuidigestop(stop);
                bestetruck.addStop(stop);
            }

        }



        //OVERIGE COLLECTS AFHANDELEN

        for (int i = 0; i < collectlijst.size(); i++) {
            Collect collect = collectlijst.get(i);
            machine = collect.getMachine();
            Location dichstedepot = collect.getMachine().getLocation().getDichtstedepot(depotlijst,distancematrix).getLocation();
            bestetruck = collect.getMachine().getLocation().getDichtsteTruck(trucklijst, machine, distancematrix, timematrix,dichstedepot.getId());

            if(bestetruck==null){
                Truck vollgeladentruck;
                ArrayList<Truck> vollgeladentrucks = new ArrayList<Truck>();
                Depot afzetdepot = collect.getMachine().getLocation().getDichtstedepot(depotlijst,distancematrix);
                for(int j=0; j<trucklijst.size();j++) {
                    vollgeladentruck = trucklijst.get(j);
                    if (vollgeladentruck.getVolume() > machine.getVolume()) {
                        vollgeladentrucks.add(vollgeladentruck);
                    }
                }
                //KIJK OF GEWICHT GROTER IS DAN MACHINELIJST.SIZE EN DEPOT LIGT IN DE BUURT
                for(int j=0; j<vollgeladentrucks.size();j++) {
                    Truck truck = vollgeladentrucks.get(j);

                    if (truck.heefttijdNieuw(afzetdepot.getLocation().getId(), machine.getLocation().getId(), timematrix, machine.getServicetime(),afzetdepot.getId())) {

                        truck.verplaats(afzetdepot.getLocation().getId(), timematrix, distancematrix);
                        stop = new Stop(afzetdepot.getLocation().getId());

                        truck.truckLegen(stop);
                        bestetruck = truck;
                    }
                }


                //ABSOLUUT GEEN MOGELIJKHEID, DUMMY TRUCKS
                if(bestetruck==null){
                    bestetruck = machine.getLocation().getDichtsteDummyTruck(depotlijst,distancematrix);
                 }

            }

            if (bestetruck.getStoplijst().size() == 0) {
                stop = new Stop(bestetruck.getHuidigeLocatie());
                bestetruck.addStop(stop);
            }

            bestetruck.verplaats(machine.getLocation().getId(), timematrix, distancematrix);
            bestetruck.pickUp(machine, collect.getId());
            stop = new Stop(bestetruck.getHuidigeLocatie());
            stop.addMachine(machine, collect.getId());
            bestetruck.addStop(stop);
        }


        //IEDERE TRUCK NAAR EINDLOCATIE VERPLAATSEN
        for (int i = 0; i < trucklijst.size(); i++) {
            Truck truck = trucklijst.get(i);
            if (truck.isEndlocationdepot()) {
                truck.keerTerug(timematrix, distancematrix);
                stop = new Stop(truck.getHuidigeLocatie());
                stop.addMachinelijst(truck.getMachinelijst());
                truck.addStop(stop);
                truck.truckLegen();
            }

            else{
                Location location = locationlijst.get(truck.getHuidigeLocatie());
                Depot depot = location.getDichtstedepot(depotlijst,distancematrix);
                truck.verplaats(depot.getLocation().getId(),distancematrix,timematrix);
                stop = new Stop(truck.getHuidigeLocatie());
                stop.addMachinelijst(truck.getMachinelijst());
                truck.addStop(stop);
                truck.truckLegen();
                truck.verplaats(truck.getEndlocationid(),timematrix,distancematrix);
                stop = new Stop(truck.getHuidigeLocatie());
                truck.addStop(stop);
            }

        }



        for (int i = 0; i < data.getReservetrucklijst().size(); i++) {
            Truck truck = data.getReservetrucklijst().get(i);
            if (truck.getHuidigeLocatie() != truck.getEndlocationid()) {
                truck.keerTerug(timematrix, distancematrix);
                stop = new Stop(truck.getHuidigeLocatie());
                stop.addMachinelijst(truck.getMachinelijst());
                truck.addStop(stop);
            }
            truck.truckLegen();
        }

        //DUMMY TRUCKS ADDEN AAN TRUCKLIJST
        ArrayList<Truck> trucks = data.getTrucklijst();
        for(int i=0; i<depotlijst.size();i++){
            Depot depot = depotlijst.get(i);
            trucks.addAll(depot.getDummytrucklijst());
        }







        this.solution = new Solution(data, seed);
        solution.load();
        return solution;

    }

    private int totalDistance(){
        int totaledistance = 0;
        for(int i=0; i<data.getTrucklijst().size();i++) {
            totaledistance = totaledistance + data.getTrucklijst().get(i).getDistance();
        }
        return totaledistance;
    }

    private boolean isFeasible(){
        return true;
    }

    public void writeSolution(File original, File output){

        solution.writeSolution(original, output);
    }

}
