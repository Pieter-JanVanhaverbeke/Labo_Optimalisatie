import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class Data {
    private ArrayList<Depot> depotlijst;
    private ArrayList<Drop> droplijst;
    private ArrayList<Collect> collectlijst;
    private ArrayList<Location> locationlijst;
    private ArrayList<Machine> machinelijst;
    private ArrayList<MachineType> machinetypelijst;
    private ArrayList<Truck> trucklijst;

    private HashSet<Integer> endLocations;
    private HashSet<Integer> startLocations;

    private DistanceMatrix distancematrix;
    private TimeMatrix timematrix;

    public Data(){
        depotlijst = new ArrayList<Depot>();
        droplijst = new ArrayList<Drop>() ;
        collectlijst = new ArrayList<Collect>() ;
        locationlijst = new ArrayList<Location>() ;
        machinelijst  = new ArrayList<Machine>() ;
        machinetypelijst = new ArrayList<MachineType>() ;
        trucklijst = new ArrayList<Truck>() ;

        startLocations = new HashSet<>();
        endLocations = new HashSet<>();


       //  Distancematrix distancematrix = new Distancematrix();
       //  Timematrix timematrix = new Timematrix();
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

    public DistanceMatrix getDistancematrix() {
        return distancematrix;
    }

    public void setDistancematrix(DistanceMatrix distancematrix) {
        this.distancematrix = distancematrix;
    }

    public TimeMatrix getTimematrix() {
        return timematrix;
    }

    public void setTimematrix(TimeMatrix timematrix) {
        this.timematrix = timematrix;
    }

    public HashSet<Integer> getEndLocations() {
        return endLocations;
    }

    public void setEndLocations(HashSet<Integer> endLocations) {
        this.endLocations = endLocations;
    }

    public HashSet<Integer> getStartLocations() {
        return startLocations;
    }

    public void setStartLocations(HashSet<Integer> startLocations) {
        this.startLocations = startLocations;
    }

    public void leesData(File file) throws FileNotFoundException {
        // file = new File("C:\\Users\\piete\\IdeaProjects\\Labo_Optimalisatie\\inleesfile.txt");
        Scanner sc = null;
        sc = new Scanner(file);


        for (int i = 0; i < 4; i++) {
            sc.nextLine();      //info lijn verwijderen
        }
        while (sc.hasNextLine()) {
            String line = sc.nextLine();

            if (line.contains("LOCATIONS:")) {
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

                    Location huidigelocatie = locationlijst.get(startlocationid);

                    endLocations.add(enlocationid);
                    startLocations.add(startlocationid);


                    Truck truck = new Truck(id, startlocationid, enlocationid, null);
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
                timematrix = new TimeMatrix(aantal, aantal);
                for (int i = 0; i < aantal; i++) {
                    line = sc.nextLine();

                    while (line.charAt(0) == ' ') {
                        line = line.substring(1);
                    }

                    for (int j = 0; j < aantal; j++) {
                        String[] values = line.split("\\s+");
                        timematrix.addTime(i, j, Integer.parseInt(values[j]));
                    }
                }

            } else if (line.contains("DISTANCE_MATRIX: ")) {
                int aantal = new Scanner(line).useDelimiter("\\D+").nextInt();
                distancematrix = new DistanceMatrix(aantal, aantal);
                for (int i = 0; i < aantal; i++) {
                    line = sc.nextLine();
                    while (line.charAt(0) == ' ') {
                        line = line.substring(1);
                    }

                    for (int j = 0; j < aantal; j++) {
                        String[] values = line.split("\\s+");
                        distancematrix.addDistance(i, j, Integer.parseInt(values[j]));
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


                if(truck.getEndlocationid()==depot.getLocation().getId()){
                    depot.addTruck(truck);
                }
            }
        }




    }
}