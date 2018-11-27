import java.util.ArrayList;

public class Depot {
    private int id;
    private Location location;

    private ArrayList<Machine> machinelijst;
    private ArrayList<Truck> trucklijst;
    private ArrayList<Truck> dummytrucklijst;

    public Depot(int id,Location location) {
        this.id = id;
        this.location = location;
        trucklijst = new ArrayList<Truck>();
        machinelijst = new ArrayList<Machine>();
        dummytrucklijst = new ArrayList<Truck>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public ArrayList<Machine> getMachinelijst() {
        return machinelijst;
    }

    public void setMachinelijst(ArrayList<Machine> machinelijst) {
        this.machinelijst = machinelijst;
    }

    public ArrayList<Truck> getTrucklijst() {
        return trucklijst;
    }

    public void setTrucklijst(ArrayList<Truck> trucklijst) {
        this.trucklijst = trucklijst;
    }

    public ArrayList<Truck> getDummytrucklijst() {
        return dummytrucklijst;
    }

    public void setDummytrucklijst(ArrayList<Truck> dummytrucklijst) {
        this.dummytrucklijst = dummytrucklijst;
    }

    @Override
    public String toString() {
        return "Depot{" +
                "id=" + id +
                ", location=" + location +
                '}';
    }

    public boolean hasMachine(int machinetypeid){
        for(int i=0; i<machinelijst.size();i++){
            if(machinelijst.get(i).getMachineTypeId()==machinetypeid){
                return  true;
            }
        }
        return false;
    }

    public Machine getMachine(int machinetypeid){
        Machine machine = null;
        for(int i=0; i<machinelijst.size();i++){
            if(machinelijst.get(i).getMachineTypeId()==machinetypeid){
                machine = machinelijst.get(i);
            }
        }
        return  machine;
    }


    public Truck getGoedeTruck(int locationid,TimeMatrix timeMatrix, int servicetime){

        Truck goedetruck=null;
        for(int i=0; i<trucklijst.size();i++){
            Truck truck = trucklijst.get(i);
            if(truck.heefttijd(locationid,timeMatrix,servicetime)){
                goedetruck = truck;
            }
        }

        return goedetruck;


    }

    public void addMachine(Machine machine){
        machinelijst.add(machine);
    }

    public void removeMachine(Machine machine){
        machinelijst.remove(machine);
    }

    public void addTruck(Truck truck) {trucklijst.add(truck);}

    public void addDummyTruck(Truck truck){dummytrucklijst.add(truck);}



}
