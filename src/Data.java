import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Data {

    private ArrayList<Location> locationslijst;
    private ArrayList<Depot> depotlijst;
    private ArrayList<Drop> droplijst;
    private ArrayList<Collect> collectlijst;
    private ArrayList<Location> locationlijst;
    private ArrayList<Machine> machinelijst;
    private ArrayList<MachineType> machinetypelijst;
    private ArrayList<Truck> trucklijst;


    private Distancematrix distancematrix;
    private Timematrix timematrix;

    public Data(){
        locationslijst = new ArrayList<Location>();
        depotlijst = new ArrayList<Depot>();
        droplijst = new ArrayList<Drop>() ;
        collectlijst = new ArrayList<Collect>() ;
        locationlijst = new ArrayList<Location>() ;
        machinelijst  = new ArrayList<Machine>() ;
        machinetypelijst = new ArrayList<MachineType>() ;
        trucklijst = new ArrayList<Truck>() ;


       //  Distancematrix distancematrix = new Distancematrix();
       //  Timematrix timematrix = new Timematrix();
    }

    public ArrayList<Location> getLocationslijst() {
        return locationslijst;
    }

    public void setLocationslijst(ArrayList<Location> locationslijst) {
        this.locationslijst = locationslijst;
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

    public Distancematrix getDistancematrix() {
        return distancematrix;
    }

    public void setDistancematrix(Distancematrix distancematrix) {
        this.distancematrix = distancematrix;
    }

    public Timematrix getTimematrix() {
        return timematrix;
    }

    public void setTimematrix(Timematrix timematrix) {
        this.timematrix = timematrix;
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
            //    System.out.println(line);

            if (line.contains("LOCATIONS:")) {
                int aantal = new Scanner(line).useDelimiter("\\D+").nextInt();     //eerste getal zoeken
                for (int i = 0; i < aantal; i++) {
                    line = sc.nextLine();
                    String[] values = line.split("\\s+");

                    int id = Integer.parseInt(values[1]);
                    double lat = Double.parseDouble(values[2]);
                    double lon = Double.parseDouble(values[3]);
                    String name = values[4];

                    Location location = new Location(id, lat, lon, name);
                    locationslijst.add(location);
                }
            } else if (line.contains("DEPOTS")) {
                int aantal = new Scanner(line).useDelimiter("\\D+").nextInt();
                for (int i = 0; i < aantal; i++) {

                    line = sc.nextLine();
                    String[] values = line.split("\\s+");
                    int id = Integer.parseInt(values[1]);
                    int locationid = Integer.parseInt(values[2]);

                    Depot depot = new Depot(id, locationid);
                    depotlijst.add(depot);
                }
            } else if (line.contains("Trucks")) {
                int aantal = new Scanner(line).useDelimiter("\\D+").nextInt();
                for (int i = 0; i < aantal; i++) {

                    line = sc.nextLine();
                    String[] values = line.split("\\s+");
                    int id = Integer.parseInt(values[1]);
                    int startlocationid = Integer.parseInt(values[2]);
                    int enlocationid = Integer.parseInt(values[3]);

                    Truck truck = new Truck(id, startlocationid, enlocationid, null);
                    trucklijst.add(truck);
                }
            } else if (line.contains("MACHINE_TYPES")) {
                int aantal = new Scanner(line).useDelimiter("\\D+").nextInt();
                for (int i = 0; i < aantal; i++) {

                    line = sc.nextLine();
                    String[] values = line.split("\\s+");
                    int id = Integer.parseInt(values[1]);
                    int volume = Integer.parseInt(values[2]);
                    int servicetime = Integer.parseInt(values[3]);
                    String name = values[4];

                    MachineType machineType = new MachineType(id, volume, servicetime, name);
                    machinetypelijst.add(machineType);
                }

            } else if (line.contains("MACHINES")) {
                int aantal = new Scanner(line).useDelimiter("\\D+").nextInt();
                for (int i = 0; i < aantal; i++) {

                    line = sc.nextLine();
                    String[] values = line.split("\\s+");
                    int id = Integer.parseInt(values[1]);
                    int machinetypeid = Integer.parseInt(values[2]);
                    int locationid = Integer.parseInt(values[3]);

                    Machine machine = new Machine(id, machinetypeid, locationid);
                    machinelijst.add(machine);
                }

            } else if (line.contains("DROPS")) {
                int aantal = new Scanner(line).useDelimiter("\\D+").nextInt();
                for (int i = 0; i < aantal; i++) {

                    line = sc.nextLine();
                    String[] values = line.split("\\s+");
                    int id = Integer.parseInt(values[1]);
                    int machinetypeid = Integer.parseInt(values[2]);
                    int locationid = Integer.parseInt(values[3]);

                    Drop drop = new Drop(id, machinetypeid, locationid);
                    droplijst.add(drop);
                }

            } else if (line.contains("TIME_MATRIX")) {
                int aantal = new Scanner(line).useDelimiter("\\D+").nextInt();
                timematrix = new Timematrix(aantal, aantal);
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
                distancematrix = new Distancematrix(aantal, aantal);
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


    }
}