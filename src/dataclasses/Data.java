package dataclasses;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Data {
    private ArrayList<Depot> depotlijst;
    private ArrayList<Drop> droplijst;
    private ArrayList<Collect> collectlijst;
    private ArrayList<Location> locationlijst;
    private ArrayList<Machine> machinelijst;
    private ArrayList<MachineType> machinetypelijst;
    private ArrayList<Truck> trucklijst;
    private ArrayList<Truck> reservetrucklijst;

    private ArrayList<Integer> endLocations;
    private ArrayList<Integer> startLocations;

    private int[][] timematrix;
    private int[][] distancematrix;
    private int[][] machineStats;
    private int[] depotArray;
    private int truckCapacity;
    private int truckWorkingTime;
    private HashMap<Integer, Integer> serviceTimes;
    private int truckCount;

    public Data(){
        depotlijst = new ArrayList<Depot>();
        droplijst = new ArrayList<Drop>() ;
        collectlijst = new ArrayList<Collect>() ;
        locationlijst = new ArrayList<Location>() ;
        machinelijst  = new ArrayList<Machine>() ;
        machinetypelijst = new ArrayList<MachineType>() ;
        trucklijst = new ArrayList<Truck>() ;
        reservetrucklijst = new ArrayList<Truck>();

        startLocations = new ArrayList<Integer>();
        endLocations = new ArrayList<Integer>();

    }

    public void buildLastArrays() {
        this.machineStats = new int[this.getMachinelijst().size()][];
        this.serviceTimes = new HashMap<>();
        for (Machine machine: this.getMachinelijst()) {
            this.serviceTimes.put(machine.getMachineTypeId(), machine.getServicetime());
            this.machineStats[machine.getId()] = new int[]{
                    machine.getMachineTypeId(),
                    machine.getVolume(),
                    machine.getServicetime()
            };
        }
        this.depotArray = new int[depotlijst.size()];
        for (Depot depot: depotlijst) this.depotArray[depot.getId()] = depot.getLocation().getId();
        this.truckCount = trucklijst.size();
    }

    public int[][] getMachineStats() {
        return machineStats;
    }

    public void setMachineStats(int[][] machineStats) {
        this.machineStats = machineStats;
    }

    public HashMap<Integer, Integer> getServiceTimes() {
        return serviceTimes;
    }

    public void setServiceTimes(HashMap<Integer, Integer> serviceTimes) {
        this.serviceTimes = serviceTimes;
    }

    public ArrayList<Depot> getDepotlijst() {
        return depotlijst;
    }

    public void setDepotlijst(ArrayList<Depot> depotlijst) {
        this.depotlijst = depotlijst;
    }

    public ArrayList<Drop> getDroplijst() {
        return droplijst;
    }

    public void setDroplijst(ArrayList<Drop> droplijst) {
        this.droplijst = droplijst;
    }

    public ArrayList<Collect> getCollectlijst() {
        return collectlijst;
    }

    public void setCollectlijst(ArrayList<Collect> collectlijst) {
        this.collectlijst = collectlijst;
    }

    public ArrayList<Location> getLocationlijst() {
        return locationlijst;
    }

    public void setLocationlijst(ArrayList<Location> locationlijst) {
        this.locationlijst = locationlijst;
    }

    public ArrayList<Machine> getMachinelijst() {
        return machinelijst;
    }

    public void setMachinelijst(ArrayList<Machine> machinelijst) {
        this.machinelijst = machinelijst;
    }

    public ArrayList<MachineType> getMachinetypelijst() {
        return machinetypelijst;
    }

    public void setMachinetypelijst(ArrayList<MachineType> machinetypelijst) {
        this.machinetypelijst = machinetypelijst;
    }

    public ArrayList<Truck> getTrucklijst() {
        return trucklijst;
    }

    public void setTrucklijst(ArrayList<Truck> trucklijst) {
        this.trucklijst = trucklijst;
    }

    public int[][] getDistancematrix() {
        return distancematrix;
    }

    public void setDistancematrix(int[][] distancematrix) {
        this.distancematrix = distancematrix;
    }

    public int[][] getTimeMatrix() {
        return timematrix;
    }

    public void setTimematrix(int[][] timematrix) {
        this.timematrix = timematrix;
    }

    public ArrayList<Integer> getEndLocations() {
        return endLocations;
    }

    public void setEndLocations(ArrayList<Integer> endLocations) {
        this.endLocations = endLocations;
    }

    public List<Integer> getStartLocations() {
        return startLocations;
    }

    public void setStartLocations(ArrayList<Integer> startLocations) {
        this.startLocations = startLocations;
    }

    public ArrayList<Truck> getReservetrucklijst() {
        return reservetrucklijst;
    }

    public void setReservetrucklijst(ArrayList<Truck> reservetrucklijst) {
        this.reservetrucklijst = reservetrucklijst;
    }

    public int[][] getTimematrix() {
        return timematrix;
    }

    public int[] getDepotArray() {
        return depotArray;
    }

    public void setDepotArray(int[] depotArray) {
        this.depotArray = depotArray;
    }

    public int getTruckCount() {
        return truckCount;
    }

    public void setTruckCount(int truckCount) {
        this.truckCount = truckCount;
    }

    public int getTruckCapacity() {
        return truckCapacity;
    }

    public void setTruckCapacity(int truckCapacity) {
        this.truckCapacity = truckCapacity;
    }

    public int getTruckWorkingTime() {
        return truckWorkingTime;
    }

    public void setTruckWorkingTime(int truckWorkingTime) {
        this.truckWorkingTime = truckWorkingTime;
    }

    public void leesData(File file) throws FileNotFoundException {
        // file = new File("C:\\Users\\piete\\IdeaProjects\\Labo_Optimalisatie\\inleesfile.txt");
        Scanner sc = null;
        sc = new Scanner(file);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();

            if(line.contains("TRUCK_CAPACITY:")) truckCapacity = Integer.parseInt(line.split(" ")[1]);
            else if(line.contains("TRUCK_WORKING_TIME:")) truckWorkingTime = Integer.parseInt(line.split(" ")[1]);
            else if (line.contains("LOCATIONS:")) {
                int aantal = new Scanner(line).useDelimiter("\\D+").nextInt();     //eerste getal zoeken
                for (int i = 0; i < aantal; i++) {
                    line = sc.nextLine();
                    while (line.charAt(0) == ' ') {
                        line = line.substring(1);
                    }


                    String[] values = line.split("\\s+");
                    int id = Integer.parseInt(values[0]);
                    double lat = Double.parseDouble(values[1]);
                    double lon = Double.parseDouble(values[2]);
                    String name = values[3];

                    Location location = new Location(id, lat, lon, name);
                    locationlijst.add(location);
                }
            } else if (line.contains("DEPOTS")) {
                int aantal = new Scanner(line).useDelimiter("\\D+").nextInt();
                for (int i = 0; i < aantal; i++) {

                    line = sc.nextLine();

                    while (line.charAt(0) == ' ') {
                        line = line.substring(1);
                    }

                    String[] values = line.split("\\s+");
                    int id = Integer.parseInt(values[0]);
                    int locationid = Integer.parseInt(values[1]);

                    Depot depot = new Depot(id,locationlijst.get(locationid));
                    depotlijst.add(depot);
                }
            } else if (line.contains("TRUCKS")) {
                int aantal = new Scanner(line).useDelimiter("\\D+").nextInt();
                for (int i = 0; i < aantal; i++) {

                    line = sc.nextLine();

                    while (line.charAt(0) == ' ') {
                        line = line.substring(1);
                    }

                    String[] values = line.split("\\s+");
                    int id = Integer.parseInt(values[0]);
                    int startlocationid = Integer.parseInt(values[1]);
                    int enlocationid = Integer.parseInt(values[2]);


                    endLocations.add(enlocationid);
                    startLocations.add(startlocationid);


                    Truck truck = new Truck(id, startlocationid, enlocationid, null);

                    if(enlocationid>=depotlijst.size()){         //TODO GAAN ER VANUIT DAT DEPOTIDS ALTIJD EERSTE IDS ZIJN
                        truck.setEndlocationdepot(false);
                    }

                    if(startlocationid>=depotlijst.size()){
                        truck.setBeginlocatiedepot(false);
                    }

                    trucklijst.add(truck);
                }

            } else if (line.contains("MACHINE_TYPES")) {
                int aantal = new Scanner(line).useDelimiter("\\D+").nextInt();
                for (int i = 0; i < aantal; i++) {

                    line = sc.nextLine();

                    while (line.charAt(0) == ' ') {
                        line = line.substring(1);
                    }

                    String[] values = line.split("\\s+");
                    int id = Integer.parseInt(values[0]);
                    int volume = Integer.parseInt(values[1]);
                    int servicetime = Integer.parseInt(values[2]);
                    String name = values[3];

                    MachineType machineType = new MachineType(id, volume, servicetime, name);
                    machinetypelijst.add(machineType);
                }

            } else if (line.contains("MACHINES")) {
                int aantal = new Scanner(line).useDelimiter("\\D+").nextInt();
                for (int i = 0; i < aantal; i++) {

                    line = sc.nextLine();

                    while (line.charAt(0) == ' ') {
                        line = line.substring(1);
                    }

                    String[] values = line.split("\\s+");
                    int id = Integer.parseInt(values[0]);
                    int machinetypeid = Integer.parseInt(values[1]);
                    int locationid = Integer.parseInt(values[2]);

                    Machine machine = new Machine(id, machinetypeid, locationlijst.get(locationid), machinetypelijst.get(machinetypeid));
                    machinelijst.add(machine);
                }

            } else if (line.contains("DROPS")) {
                int aantal = new Scanner(line).useDelimiter("\\D+").nextInt();
                for (int i = 0; i < aantal; i++) {

                    line = sc.nextLine();

                    while (line.charAt(0) == ' ') {
                        line = line.substring(1);
                    }

                    String[] values = line.split("\\s+");
                    int id = Integer.parseInt(values[0]);
                    int machinetypeid = Integer.parseInt(values[1]);
                    int locationid = Integer.parseInt(values[2]);

                    Drop drop = new Drop(id, machinetypeid, locationlijst.get(locationid));
                    droplijst.add(drop);


                }
            }

                else if (line.contains("COLLECTS")) {
                    int aantal = new Scanner(line).useDelimiter("\\D+").nextInt();
                    for (int i = 0; i < aantal; i++) {
                        line = sc.nextLine();

                        while (line.charAt(0) == ' ') {
                            line = line.substring(1);
                        }

                        String[] values = line.split("\\s+");
                        int id = Integer.parseInt(values[0]);
                        int machineid = Integer.parseInt(values[1]);

                        Collect collect = new Collect(id,machinelijst.get(machineid));
                        collectlijst.add(collect);

                     //   machinelijst.get(machineid).setCollect(true);                   //zeggen dat machine collect is

                    }



            } else if (line.contains("TIME_MATRIX")) {
                int aantal = new Scanner(line).useDelimiter("\\D+").nextInt();
                timematrix = new int[aantal][aantal];
                for (int i = 0; i < aantal; i++) {
                    line = sc.nextLine();

                    while (line.charAt(0) == ' ') {
                        line = line.substring(1);
                    }

                    for (int j = 0; j < aantal; j++) {
                        String[] values = line.split("\\s+");
                        timematrix[i][j] = Integer.parseInt(values[j]);
                    }
                }

            } else if (line.contains("DISTANCE_MATRIX: ")) {
                int aantal = new Scanner(line).useDelimiter("\\D+").nextInt();
                distancematrix = new int[aantal][aantal];
                for (int i = 0; i < aantal; i++) {
                    line = sc.nextLine();
                    while (line.charAt(0) == ' ') {
                        line = line.substring(1);
                    }

                    for (int j = 0; j < aantal; j++) {
                        String[] values = line.split("\\s+");
                        distancematrix[i][j] = Integer.parseInt(values[j]);
                    }
                }


            }

        }

        //DEPOTS VULLEN MET MACHINES
        for(int i=0; i<depotlijst.size();i++){
            Depot depot = depotlijst.get(i);
            for(int j=0; j<machinelijst.size();j++){
                Machine machine = machinelijst.get(j);


                if(machine.getLocation().getId()==depot.getLocation().getId()){
                    depot.addMachine(machine);
                }
            }
        }


        //alle trucks toevoegen
        for(int i=0; i<depotlijst.size();i++){
            Depot depot = depotlijst.get(i);
            for(int j=0; j<trucklijst.size();j++){
                Truck truck = trucklijst.get(j);
                if(truck.getHuidigeLocatie()==depot.getLocation().getId()){
                    depot.addTruck(truck);
                }
            }
        }

        for(int i=0; i<depotlijst.size();i++){
            Depot depot = depotlijst.get(i);
            Truck truck = new Truck(trucklijst.size() + i, depot.getLocation().getId(),depot.getLocation().getId(),"dummy");
            depot.addDummyTruck(truck);
            reservetrucklijst.add(truck);
        }


    }
}