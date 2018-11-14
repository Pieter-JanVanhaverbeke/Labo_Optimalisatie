import javax.crypto.Mac;
import java.util.ArrayList;
import java.util.HashMap;
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

    ArrayList<Machine> machinelijst;        //huidige lijst van machines dat truck meedraagt
    ArrayList<Location> stoplijst;
    LinkedList<String> pickUpsDropOffs;


    public Truck( int id, int huidigeLocatie, int endlocationid, String name) {
        this.huidigeLocatie = huidigeLocatie;
        this.endlocationid = endlocationid;
        this.id = id;
        this.name = name;
        this.geredenminuten = 0;
        this.volume = 0;
        this.distance = 0;
        machinelijst = new ArrayList<Machine>();
        stoplijst = new ArrayList<Location>();
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

    public ArrayList<Location> getStoplijst() {
        return stoplijst;
    }

    public void setStoplijst(ArrayList<Location> stoplijst) {
        this.stoplijst = stoplijst;
    }

    public void addStop(Location location){
        stoplijst.add(location);
    }

    public void pickUp(Machine machine){
        machinelijst.add(machine);
        volume = volume + machine.getMachineType().getVolume();
        geredenminuten = geredenminuten + machine.getMachineType().getServicetime();
    }

    public void dropOf(Machine machine){
        volume = volume - machine.getMachineType().getVolume();
        geredenminuten = geredenminuten + machine.getMachineType().getServicetime();
        machinelijst.remove(machine);
    }

    public void verplaats(int locationid, Timematrix timematrix, Distancematrix distancematrix){              //truck gaat naar locationid
        int tijdnodig = timematrix.getTime()[huidigeLocatie][locationid];
        int distancenodig = distancematrix.getDistance()[huidigeLocatie][locationid];

        geredenminuten = geredenminuten + tijdnodig;                    //updaten tijd
        distance = distance + distancenodig;                            //updaten distance

        huidigeLocatie = locationid;                                             //aanpassen huidige locatie

    }

    public boolean heefttijd(int locationid,Timematrix timematrix){
        int tijdnodig = timematrix.getTime()[huidigeLocatie][locationid];

        int nodigeminuten = geredenminuten + tijdnodig;
        int terugkeertijd = timematrix.getTime()[locationid][endlocationid];

        if(geredenminuten+nodigeminuten+terugkeertijd>TRUCK_WORKING_TIME){
            return false;
        }

        else return true;
    }

    public boolean heeftcapacity(Machine machine){
            if(volume+machine.getMachineType().getVolume()>TRUCK_CAPACITY){
                return  false;
            }
            else return true;
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
