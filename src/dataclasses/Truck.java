package dataclasses;

import java.util.ArrayList;
import java.util.LinkedList;

public class Truck {
    //Constanten
    private static final int TRUCK_CAPACITY = 100;
    private static final int TRUCK_WORKING_TIME = 600;

    private int huidigeLocatie;            //begin pos meegeven bij constructor
    private int endlocationid;

    private int id;
    private String name;
    private int geredenminuten;
    private int volume;
    private int distance;
    private boolean endlocationdepot;
    private boolean beginlocatiedepot;


    private ArrayList<Machine> machinelijst;        //huidige lijst van machines dat truck meedraagt
    private ArrayList<Stop> stoplijst;
    private Stop huidigestop;


    public Truck( int id, int huidigeLocatie, int endlocationid, String name) {
        this.huidigeLocatie = huidigeLocatie;
        this.endlocationid = endlocationid;
        this.id = id;
        this.name = name;
        this.geredenminuten = 0;
        this.volume = 0;
        this.distance = 0;
        machinelijst = new ArrayList<Machine>();
        stoplijst = new ArrayList<Stop>();
        huidigestop = new Stop(huidigeLocatie);
        endlocationdepot = true;
        beginlocatiedepot = true;
    }


    public void pickUp(Machine machine){
        machinelijst.add(machine);
        volume = volume + machine.getVolume();
        geredenminuten = geredenminuten + 2*machine.getServicetime();   //tijd al meerekenen voor het afzetten, daarom 2X servicetime

    }

    public void dropOf(Machine machine){
        volume = volume - machine.getVolume();
        machinelijst.remove(machine);
    }

    public boolean verplaats(int locationid, int[][] timematrix, int[][] distancematrix){              //truck gaat naar locationid
        int tijdnodig = timematrix[huidigeLocatie][locationid];
        int distancenodig = distancematrix[huidigeLocatie][locationid];

        geredenminuten = geredenminuten + tijdnodig;                    //updaten tijd
        distance = distance + distancenodig;                            //updaten distance

        huidigeLocatie = locationid;                                             //aanpassen huidige locatie

        if(distancenodig==0){
            return false;
        }
        else return true;


    }

    public boolean heefttijd(int locationid,int[][] timematrix, int servicetime){
        int tijdnodig = timematrix[huidigeLocatie][locationid];

        int nodigeminuten = geredenminuten + tijdnodig;                             //tijd voor verplaatsing + meerekenen huidige tijd
        int terugkeertijd = timematrix[locationid][endlocationid];        //tijd voor naar eindlocatie te gaan

        int totaletijdnodig = nodigeminuten + terugkeertijd + 2*servicetime;     //2 keer servicetime, want ook nog eens afzetten


        if(totaletijdnodig>TRUCK_WORKING_TIME){
            return false;
        }

        else return true;
    }


    public boolean heefttijd(int locationid,int locationid2,int[][] timematrix, int servicetime){
        int tijdnodig = timematrix[huidigeLocatie][locationid];
        int tijdnodig2 = timematrix[locationid][locationid2];

        int nodigeminuten = geredenminuten + tijdnodig + tijdnodig2;                             //tijd voor verplaatsing + meerekenen huidige tijd
        int terugkeertijd = timematrix[locationid2][endlocationid];        //tijd voor naar eindlocatie te gaan


        int totaletijdnodig = nodigeminuten + terugkeertijd + 2*servicetime;     //2 keer servicetime, want ook nog eens afzetten

        if(totaletijdnodig>TRUCK_WORKING_TIME){
            return false;
        }

        else return true;
    }

    public boolean heefttijdNieuw(int locationid,int[][] timematrix, int servicetime,int depotlocationid) {
        if (!endlocationdepot) {
            int tijdnodig = timematrix[huidigeLocatie][locationid];
            int terugkeertijd = timematrix[locationid][depotlocationid] + timematrix[depotlocationid][endlocationid];           //TODO DEPOTID

            int totaletijdnodig = geredenminuten + tijdnodig + terugkeertijd + 2 * servicetime;

            if (totaletijdnodig > TRUCK_WORKING_TIME) {
                return false;
            }
            else return true;
        }

        else{
           return heefttijd(locationid,timematrix,servicetime);
        }
    }


        public boolean heefttijdNieuw(int locationid,int locationid2, int[][] timematrix, int servicetime, int depotlocationid){
        if(!endlocationdepot){
            int tijdnodig = timematrix[huidigeLocatie][locationid];
            int tijdnodig2 = timematrix[locationid][locationid2];
            int terugkeertijd = timematrix[locationid2][depotlocationid] + timematrix[depotlocationid][endlocationid];           //TODO DEPOTID

            int totaletijdnodig = geredenminuten + tijdnodig + tijdnodig2 + terugkeertijd + 2*servicetime;

            if(totaletijdnodig>TRUCK_WORKING_TIME){
                return false;
            }

            else return true;
        }



        //TODO GEEN OUDE METHODE WERKT
        else{
           return heefttijd(locationid,locationid2,timematrix,servicetime);
        }

    }




    public void keerTerug(int[][] timematrix, int[][] distancematrix){
        verplaats(endlocationid, timematrix, distancematrix);
        huidigeLocatie = endlocationid;
    }

    public void truckLegen(Stop stop){
        for(int i=0; i<machinelijst.size();i++){            //alles terug afzetten.
            Machine machine = machinelijst.get(i);
          //  System.out.println("dataclasses.Truck " + id + " dropt " + machine.getId() + " af op locatie: " + huidigeLocatie);

          //  geredenminuten = geredenminuten + machine.getServicetime();
            stop.addMachine(machine);
        }
        stoplijst.add(stop);
        machinelijst.clear();
        this.setVolume(0);  //alles legen, volume is 0
      //  addStop(stop);

    }


    public void truckLegen(){
        for(int i=0; i<machinelijst.size();i++){            //alles terug afzetten.
            Machine machine = machinelijst.get(i);
            //  System.out.println("dataclasses.Truck " + id + " dropt " + machine.getId() + " af op locatie: " + huidigeLocatie);

         //   geredenminuten = geredenminuten + machine.getServicetime();
        }
        machinelijst.clear();
        this.setVolume(0);  //alles legen, volume is 0

    }



    public boolean heeftcapacity(Machine machine){
            if(volume+machine.getVolume()>TRUCK_CAPACITY){
                return  false;
            }
            else return true;
    }



    public int getDistance(Location location, DistanceMatrix distancematrix){
        int locatieid = location.getId();

        return distancematrix.getDistance()[huidigeLocatie][locatieid];

    }

    public boolean heeftcapacity(Machine machine1 , Machine machine2){
        if(volume+machine1.getVolume()+machine2.getVolume()>TRUCK_CAPACITY){
            return  false;
        }
        else return true;
    }




    public ArrayList<Machine> getAlleMachinesVanType(int type){
        ArrayList<Machine> lijst = new ArrayList<Machine>();
        for(int i=0; i<machinelijst.size();i++){
            Machine machine = machinelijst.get(i);
            if(machine.getMachineTypeId()==type){
                lijst.add(machine);
            }
        }
        return lijst;
    }

    public static int getTruckCapacity() {
        return TRUCK_CAPACITY;
    }

    public static int getTruckWorkingTime() {
        return TRUCK_WORKING_TIME;
    }

    public int getHuidigeLocatie() {
        return huidigeLocatie;
    }

    public void setHuidigeLocatie(int huidigeLocatie) {
        this.huidigeLocatie = huidigeLocatie;
    }

    public int getEndlocationid() {
        return endlocationid;
    }

    public void setEndlocationid(int endlocationid) {
        this.endlocationid = endlocationid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGeredenminuten() {
        return geredenminuten;
    }

    public void setGeredenminuten(int geredenminuten) {
        this.geredenminuten = geredenminuten;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public ArrayList<Machine> getMachinelijst() {
        return machinelijst;
    }

    public void setMachinelijst(ArrayList<Machine> machinelijst) {
        this.machinelijst = machinelijst;
    }

    public void addStop(Stop stop){
        stoplijst.add(stop);
    }

    public Stop getHuidigestop() {
        return huidigestop;
    }

    public void setHuidigestop(Stop huidigestop) {
        this.huidigestop = huidigestop;
    }

    public ArrayList<Stop> getStoplijst() {
        return stoplijst;
    }

    public void setStoplijst(ArrayList<Stop> stoplijst) {
        this.stoplijst = stoplijst;
    }

    public boolean isEndlocationdepot() {
        return endlocationdepot;
    }

    public void setEndlocationdepot(boolean endlocationdepot) {
        this.endlocationdepot = endlocationdepot;
    }

    public boolean isBeginlocatiedepot() {
        return beginlocatiedepot;
    }

    public void setBeginlocatiedepot(boolean beginlocatiedepot) {
        this.beginlocatiedepot = beginlocatiedepot;
    }

    @Override
    public String toString() {
        return "dataclasses.Truck{" +
                "huidigeLocatie=" + huidigeLocatie +
                ", endlocationid=" + endlocationid +
                ", id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
