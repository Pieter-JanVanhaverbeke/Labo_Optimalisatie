import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Oplossing {

    private Data data;
    private Oplossingsmatrix oplossingsmatrix;
    private Solution solution;

    private TimeMatrix timematrix;
    private DistanceMatrix distancematrix;


    public Oplossing(Data data) {
        this.data = data;
        oplossingsmatrix = new Oplossingsmatrix(); //Na oplossing toevoegen

        timematrix = data.getTimematrix();
        distancematrix = data.getDistancematrix();

        this.solution = new Solution(data);
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Oplossingsmatrix getOplossingsmatrix() {
        return oplossingsmatrix;
    }

    public void setOplossingsmatrix(Oplossingsmatrix oplossingsmatrix) {
        this.oplossingsmatrix = oplossingsmatrix;
    }


    public Solution start() {
        ArrayList<Drop> droplijst = data.getDroplijst();
        ArrayList<Collect> collectlijst = data.getCollectlijst();
        ArrayList<Depot> depotlijst = data.getDepotlijst();
        ArrayList<Truck> trucklijst = data.getTrucklijst();
        Collect dichtstecollect;
        Truck bestetruck;
        Machine machine = null;
        Stop stop = null;
        boolean verplaats = false;



        for (int i = 0; i < droplijst.size(); i++) {
            Drop drop = droplijst.get(i);
            dichtstecollect = drop.getLocation().getDichtsteCollect(collectlijst, drop.getMachineTypeId(), distancematrix);       //dichtste collect
            Depot dichtsteDepot = drop.getLocation().getDichtsteDepot(depotlijst, drop.getMachineTypeId(), distancematrix);
            int dichtstecollectdistance = 999999;
            int dichtstedepotdistance = 999999;

            if(dichtstecollect!=null){
                 dichtstecollectdistance = drop.getLocation().getDistance(dichtstecollect.getMachine().getLocation().getId(),distancematrix);
            }

            if(dichtsteDepot!=null){
                dichtstedepotdistance = drop.getLocation().getDistance(dichtsteDepot.getLocation().getId(),distancematrix);
            }



            if (dichtstecollectdistance<2*dichtstedepotdistance) {
                machine = dichtstecollect.getMachine();  //machine van collect
                collectlijst.remove(dichtstecollect);   //removing collect uit lijst
            } else {                                       //geen machine gevonden van collect
                machine = dichtsteDepot.getMachine(drop.getMachineTypeId());
                dichtsteDepot.removeMachine(machine);           //verwijderen uit depotlijst
            }

            bestetruck = machine.getLocation().getDichtsteTruck(trucklijst, machine, drop.getLocation().getId(), distancematrix, timematrix);  //dichtste depot

            if(bestetruck==null){
                bestetruck = machine.getLocation().getDichtsteDummyTruck(depotlijst,distancematrix);
            }

            int beginlocatie = bestetruck.getHuidigeLocatie();
            verplaats = bestetruck.verplaats(machine.getLocation().getId(), timematrix, distancematrix);
            bestetruck.pickUp(machine);

            if (verplaats && bestetruck.getEndlocationid() == beginlocatie) {
                stop = new Stop(beginlocatie);   //solve bug
                bestetruck.addStop(stop);
            }

            stop = new Stop(machine.getLocation().getId());
            stop.addMachine(machine);
            bestetruck.setHuidigestop(stop);
            bestetruck.addStop(stop);


            bestetruck.verplaats(drop.getLocation().getId(), timematrix, distancematrix);
            bestetruck.dropOf(machine);

            stop = new Stop(drop.getLocation().getId());
            stop.addMachine(machine);
            bestetruck.setHuidigestop(stop);
            bestetruck.addStop(stop);
        }


        //OVERIGE COLLECTS AFHANDELEN

        for (int i = 0; i < collectlijst.size(); i++) {
            Collect collect = collectlijst.get(i);
            machine = collect.getMachine();
            bestetruck = collect.getMachine().getLocation().getDichtsteTruck(trucklijst, machine, distancematrix, timematrix);

            if(bestetruck==null){
                bestetruck = machine.getLocation().getDichtsteDummyTruck(depotlijst,distancematrix);
            }

            if (bestetruck.getStoplijst().size() == 0) {
                stop = new Stop(bestetruck.getHuidigeLocatie());
                bestetruck.addStop(stop);
            }

            bestetruck.verplaats(machine.getLocation().getId(), timematrix, distancematrix);
            bestetruck.pickUp(machine);
            stop = new Stop(bestetruck.getHuidigeLocatie());
            stop.addMachine(machine);
            bestetruck.addStop(stop);
        }


        //IEDERE TRUCK NAAR EINDLOCATIE VERPLAATSEN
        for (int i = 0; i < trucklijst.size(); i++) {
            Truck truck = trucklijst.get(i);
            if (truck.getHuidigeLocatie() != truck.getEndlocationid()) {
                truck.keerTerug(timematrix, distancematrix);
                stop = new Stop(truck.getHuidigeLocatie());
                stop.addMachinelijst(truck.getMachinelijst());
                truck.addStop(stop);
            }
            truck.truckLegen();
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



        //PRINTEN
   /*  for(int i=0; i<data.getTrucklijst().size();i++){
         Truck truck = data.getTrucklijst().get(i);
         if(truck.getStoplijst().size()!=0) {

             System.out.println();
             System.out.print(truck.getId() + " " + truck.getDistance() + " " + truck.getGeredenminuten() + " ");
             //   System.out.print(truck.getHuidigeLocatie() + " "); //pakt in begin depot nooit iets op
             for (int j = 0; j < truck.getStoplijst().size(); j++) {
                 System.out.print(truck.getStoplijst().get(j).toString() + " ");
             }
         }

     }
        for(int i=0; i<data.getReservetrucklijst().size();i++){
            Truck truck = data.getReservetrucklijst().get(i);
            if(truck.getStoplijst().size()!=0) {
                System.out.println();
                System.out.print(truck.getId() + " " + truck.getDistance() + " " + truck.getGeredenminuten() + " ");
                //   System.out.print(truck.getHuidigeLocatie() + " "); //pakt in begin depot nooit iets op
                for (int j = 0; j < truck.getStoplijst().size(); j++) {
                    System.out.print(truck.getStoplijst().get(j).toString() + " ");
                }
            }

        }



     System.out.println();




        System.out.println("totale distance: " + totalDistance());
*/
        solution.load();
        /*if (solution.checkFeasibility()) {
            System.out.println(solution.toString());
        }*/

        return solution;

    }

    private int totalDistance(){
        int totaledistance = 0;
        for(int i=0; i<data.getTrucklijst().size();i++) {
            totaledistance = totaledistance + data.getTrucklijst().get(i).getDistance();
        }
        return totaledistance;
    }

    private int neededTrucks(){
        int neededTrucks = 0;
        for(int i=0; i<oplossingsmatrix.getOplossing().length;i++) {
            if(oplossingsmatrix.getOplossing()[i][0] != -1) neededTrucks++;
        }
        return neededTrucks;
    }

    private boolean isFeasible(){
        return true;
    }

    public void writeSolution(File original){

        solution.writeSolution(original);
    }

}
