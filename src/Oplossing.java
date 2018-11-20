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

        this.solution = new Solution(data.getTrucklijst().size(), distancematrix, timematrix, data);
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

 public void start(){
     int aantaldrops = data.getDroplijst().size();
     int aantalcollects = data.getCollectlijst().size();

     ArrayList<Collect> collectlijst = data.getCollectlijst();
     ArrayList<Drop> droplijst = data.getDroplijst();

    // ArrayList<Depot> depotlijst = new ArrayList<Depot>();

    // depotlijst.addAll(data.getDepotlijst());
    // for(int i=0; i<depotlijst.size();i++){

    // }


    // ArrayList<Truck> trucklijst = new ArrayList<Truck>();
     for(int i=0; i<data.getTrucklijst().size()/data.getDepotlijst().size();i++){
         for(int j=0; j<data.getDepotlijst().size();j++){

         }

     }
     int teller = 0;

     //for(int i=0; i<data.getTrucklijst().size();i++){
     while(aantalcollects!=0){
         Truck truck = data.getTrucklijst().get(teller);
         Depot einddepot = data.getDepotlijst().get(truck.getEndlocationid());
         boolean ok = true;
         while (ok){
             ok = truck.dichtsteDropPickup(droplijst,collectlijst,distancematrix,timematrix,einddepot);
         }

     /*    if(aantalcollects==0){
             i=data.getTrucklijst().size();
         }*/

         teller = (teller + 7) % data.getTrucklijst().size();


         aantalcollects=collectlijst.size();

     }

     ArrayList<Drop> onvoltooidedrops = new ArrayList<Drop>();
     for(int i=0;i<data.getDroplijst().size();i++){
         Drop drop = data.getDroplijst().get(i);
         if(drop.getMachine()==null){                    //als drop geen machine heeft
             onvoltooidedrops.add(drop);
         }
     }



     for(int i=0;i<onvoltooidedrops.size();i++){

         Drop drop = onvoltooidedrops.get(i);
         int machinetypeid = drop.getMachineTypeId();


         Depot depot = drop.getLocation().getDichtsteDepot(data.getDepotlijst(),machinetypeid,data.getDistancematrix());
         //System.out.println("depot: " + depot);
         int servicetime = data.getMachinetypelijst().get(machinetypeid).getServicetime();            //get servicetime


         Truck truck = depot.getGoedeTruck(drop.getLocation().getId(),timematrix,servicetime);          //haal goede truck

         Machine machine = depot.getMachine(machinetypeid);
         System.out.println(truck.getId());
         truck.pickUp(machine);


         if(truck.getStoplijst().size()==0){            //zetten startlocatie als nog geen stops had
             Stop stop = new Stop(truck.getHuidigeLocatie());
             stop.addMachine(machine);
             truck.addStop(stop);
         }
         else{
             truck.getHuidigestop().addMachine(machine);
         }

         truck.verplaats(drop.getLocation().getId(),timematrix,distancematrix);


         truck.dropOf(machine);
         Stop stop = new Stop(truck.getHuidigeLocatie());
         stop.addMachine(machine);
         truck.setHuidigestop(stop);
         truck.addStop(stop);
         truck.keerTerug(timematrix,distancematrix);
         stop = new Stop(truck.getHuidigeLocatie());
         truck.addStop(stop);
         truck.setHuidigestop(stop);



     }

     int totaledistance=0;
     for(int i=0; i<data.getTrucklijst().size();i++){
         Truck truck = data.getTrucklijst().get(i);
      //   System.out.println("distance truck " + i + ": " + truck.getDistance());
         totaledistance = totaledistance + truck.getDistance();
     }

     //System.out.println("totale distance: " + totaledistance);


     for(int i=0; i<data.getTrucklijst().size();i++){
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



 }






  /*  public void zetommatrixopl(){
        int [] [] matrix;
        //Zoeken naar langste route
        int langste=0;
        for(int i=0; i<data.getTrucklijst().size();i++){
            Truck truck = data.getTrucklijst().get(i);

           if(truck.getStoplijst().size()>langste){
                langste=truck.getStoplijst().size();
            }
        }

        //matrix initilialiseren
        matrix = new int[data.getTrucklijst().size()][langste];
        for(int i=0;i<data.getTrucklijst().size();i++){
            for(int j=0; j<langste;j++){
                matrix [i][j] = -1;
            }
        }

        Truck truck;
        for(int i=0; i<data.getTrucklijst().size();i++){
            truck = data.getTrucklijst().get(i);
            for(int j=0; j<truck.getStoplijst().size();j++){
                matrix[i][j] = truck.getStoplijst().get(j).getId();              //locationid meegeven
            }
        }

        oplossingsmatrix.setOplossing(matrix);

        for(int i=0; i<data.getTrucklijst().size();i++){
            for(int j=0; j<langste;j++){
                System.out.print(oplossingsmatrix.getOplossing()[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println("totale distance: " + totalDistance());

    }
*/
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
