import javax.crypto.Mac;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import com.google.common.collect.HashMultimap;

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




    private ArrayList<Machine> machinelijst;        //huidige lijst van machines dat truck meedraagt
  //  private ArrayList<Location> stoplijst;
 //   private HashMultimap<Integer,Integer> stopsendrops;
    private ArrayList<Stop> stoplijst;

    private LinkedList<String> pickUpsDropOffs;


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
        pickUpsDropOffs = new LinkedList<>();
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

    public ArrayList<Stop> getStoplijst() {
        return stoplijst;
    }

    public void setStoplijst(ArrayList<Stop> stoplijst) {
        this.stoplijst = stoplijst;
    }

    public void pickUp(Machine machine){
        System.out.println("Truck " + id + " neemt " + machine.getName() + " op");
        machinelijst.add(machine);
        volume = volume + machine.getVolume();
        geredenminuten = geredenminuten + machine.getServicetime();

      //  Stop stop = new Stop(huidigeLocatie);

    }

    public void dropOf(Machine machine){
        System.out.println("Truck " + id + " dropt " + machine.getName() + " af");
        volume = volume - machine.getVolume();
        geredenminuten = geredenminuten + machine.getServicetime();
        machinelijst.remove(machine);

    }

    public void verplaats(int locationid, TimeMatrix timematrix, DistanceMatrix distancematrix){              //truck gaat naar locationid
        int tijdnodig = timematrix.getTime()[huidigeLocatie][locationid];
        int distancenodig = distancematrix.getDistance()[huidigeLocatie][locationid];

        geredenminuten = geredenminuten + tijdnodig;                    //updaten tijd
        distance = distance + distancenodig;                            //updaten distance

        huidigeLocatie = locationid;                                             //aanpassen huidige locatie




    }

    public boolean heefttijd(int locationid,TimeMatrix timematrix, int servicetime){
        int tijdnodig = timematrix.getTime()[huidigeLocatie][locationid];

        int nodigeminuten = geredenminuten + tijdnodig;                             //tijd voor verplaatsing + meerekenen huidige tijd
        int terugkeertijd = timematrix.getTime()[locationid][endlocationid];        //tijd voor naar eindlocatie te gaan

        int machinesafzettentijd = 0;                                                       //tijd voor alle machines af te zetten

        for(int i=0; i<machinelijst.size();i++){
            machinesafzettentijd = machinesafzettentijd + machinelijst.get(i).getServicetime();
        }

        int totaletijdnodig = nodigeminuten + terugkeertijd + machinesafzettentijd + 2*servicetime;     //2 keer servicetime, want ook nog eens afzetten


        // if(geredenminuten+nodigeminuten+terugkeertijd+servicetime>TRUCK_WORKING_TIME){
        if(totaletijdnodig>TRUCK_WORKING_TIME){
            return false;
        }

        else return true;
    }
    //als tijd heeft + capaciteit heeft --> opnemen
    public boolean kanOpnemen(Collect collect, TimeMatrix timematrix){
        if(collect==null){
            return false;
        }
        int servicetime = collect.getMachine().getServicetime();
        Machine machine = collect.getMachine();
        if(heefttijd(collect.getMachine().getLocation().getId(),timematrix,servicetime) && heeftcapacity(machine) ){
            return true;
        }

        return false;
    }


    public boolean kanAfzetten(ArrayList<Machine> machines, TimeMatrix timematrix){
        if(machines.size()!=0){
            int servicetime = machines.get(0).getServicetime();
            if(heefttijd(machines.get(0).getLocation().getId(),timematrix,servicetime)){
                return true;
            }
        }
        return false;
    }

    public void keerTerug(TimeMatrix timematrix, DistanceMatrix distancematrix){
        verplaats(endlocationid, timematrix, distancematrix);
    }

    public void truckLegen(Depot depot, Stop stop){
        for(int i=0; i<machinelijst.size();i++){            //alles terug afzetten.
            Machine machine = machinelijst.get(i);
            dropOf(machine);
            depot.addMachine(machine);
            stop.addMachine(machine);
            addStop(stop);
          //  depot.addTruck(this);                                                //als leeg is, bij depot plaatsen
        }

    }

    public int getDistance(Location location, DistanceMatrix distancematrix){
        int locatieid = location.getId();

        return distancematrix.getDistance()[huidigeLocatie][locatieid];

    }

    public boolean heeftcapacity(Machine machine){
            if(volume+machine.getVolume()>TRUCK_CAPACITY){
                return  false;
            }
            else return true;
    }


    public boolean dichtsteDropPickup(ArrayList<Drop> droplijst,ArrayList<Collect> collectlijst,DistanceMatrix distancematrix, TimeMatrix timematrix, Depot depot){
        Stop stop = null;
        Drop drop = dichtsteDrop(droplijst,distancematrix);
        Collect collect = dichtstePickup(collectlijst,distancematrix);


           ArrayList<Machine> goedemachines = getAlleMachinesVanType(drop.getMachineTypeId());
        if(kanAfzetten(goedemachines,timematrix)){                  //als machine kan afzetten, afzetten


            this.verplaats(drop.getLocation().getId(),timematrix,distancematrix);
            stop = new Stop(huidigeLocatie);
                   Machine machine = goedemachines.get(0);
                   dropOf(machine);
                   stop.addMachine(machine);
                   addStop(stop);
                   return true;
               }
        //   }

     //   System.out.println("id: " + collect.getId());
        if(kanOpnemen(collect,timematrix)){              //kijkt of mogelijk is om machine op te nemen en later weer af te zetten
                this.pickUp(collect.getMachine());                                                      //collect machine
                this.verplaats(collect.getMachine().getLocation().getId(),timematrix,distancematrix);   //verplaatsen naar collect
                collectlijst.remove(collect);

                //ADDING TO STOPLIJST
                stop = new Stop(huidigeLocatie);
                stop.addMachine(collect.getMachine());
                addStop(stop);
             //   collect.setMachine(null);
               return true;
            }
            else {
                keerTerug(timematrix, distancematrix);                                                  //terugkeren
                 stop = new Stop(huidigeLocatie);
                truckLegen(depot,stop);//truck legen van voorwerpen


               return false;
            }

    }






    public Drop dichtsteDrop(ArrayList<Drop> droplijst,  DistanceMatrix distancematrix){
        int mindistance = 999999;
        Drop dichtstedrop = null;
        for(int i=0; i<droplijst.size();i++){
           Location droplocatie = droplijst.get(i).getLocation();
           int distance = this.getDistance(droplocatie, distancematrix);
           if(distance<mindistance){
               mindistance = distance;                              //min afstand
               dichtstedrop = droplijst.get(i);                     //dichtste drop
           }
        }

        return dichtstedrop;
    }

    public Collect dichtstePickup(ArrayList<Collect> collectlijst,  DistanceMatrix distancematrix){
        int mindistance = 999999;
        Collect dichtstecollect = null;
        for(int i=0; i<collectlijst.size();i++){
            Location collectloc = collectlijst.get(i).getMachine().getLocation();
            int distance = this.getDistance(collectloc, distancematrix);
            if(distance<mindistance){
                mindistance = distance;                              //min afstand
                dichtstecollect = collectlijst.get(i);                     //dichtste drop
            }
        }

        return dichtstecollect;

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



    @Override
    public String toString() {
        return "Truck{" +
                "huidigeLocatie=" + huidigeLocatie +
                ", endlocationid=" + endlocationid +
                ", id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
