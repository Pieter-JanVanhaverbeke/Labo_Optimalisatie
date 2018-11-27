import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

public class Solution {
    private static final int MAX_WORKING_TIME = 600;
    private static final int MAX_VOLUME = 100;

    //TODO VOLUME KUNNEN CHECKEN

    //TODO TIJD KUNNEN CHECKEN

    //TODO DROPS EN COLLECTS AFGEHANDELD KUNNEN CHECKEN

    //TODO PICKUP VOOR COLLECT CHECKEN

    //TODO EIND EN BEGINPOS CHECKEN

    private Random rng;

    // per truck list:
    //      int[]: [ locationId, machineId, drop/pickupId, link naar geconnecteerd drop/collect ,<machineTypeId> ]
    private LinkedList<int[]>[] solution;
    private LinkedList<Integer>[] truckTimes;
    private LinkedList<Integer>[] truckDistances;
    // per truck list:
    //      { machine id 1, machine id 2, ... }
    private LinkedList<LinkedList<Integer>>[] truckCurrentMachines;

    private int[][] timeMatrix;
    private int[][] distanceMatrix;
    private int[] startLocations;
    private int[] endLocations;
    private int[][] machineStats;
    // [ machine type id, location id ]
    private HashMap<Integer, int[]>  drops;
    // [ machine id, location id ]
    private HashMap<Integer, int[]>  collects;
    // [ machine type id, location id ]
    private HashMap<Integer, int[]> machines;
    private HashMap<Integer, Integer> serviceTimes;
    // TODO hotswap machines *******************************************************************************************
    private HashMap<Integer, LinkedList<int[]>> availableMachines;


    private Data data;

    public Solution(Data data) {
        this.solution = new LinkedList[data.getTrucklijst().size()];
        this.truckCurrentMachines = new LinkedList[data.getTrucklijst().size()];
        this.truckTimes = new LinkedList[data.getTrucklijst().size()];
        this.truckDistances = new LinkedList[data.getTrucklijst().size()];
        this.distanceMatrix = data.getDistancematrix().getDistance();
        this.timeMatrix = data.getTimematrix().getTime();
        this.startLocations = new int[data.getTrucklijst().size()];
        this.endLocations = new int[data.getTrucklijst().size()];
        for (int truck = 0; truck < solution.length; truck++) {
            startLocations[truck] = data.getStartLocations().get(truck);
            endLocations[truck] = data.getTrucklijst().get(truck).getEndlocationid();
        }
        this.data = data;

        this.machines = new HashMap<>();
        this.machineStats = new int[data.getMachinelijst().size()][];
        this.serviceTimes = new HashMap<>();
        for (Machine machine: data.getMachinelijst()) {
            this.serviceTimes.put(machine.getMachineTypeId(), machine.getServicetime());
            this.machineStats[machine.getId()] = new int[]{
                    machine.getMachineTypeId(),
                    machine.getVolume(),
                    machine.getServicetime()
            };
            this.machines.put(machine.getId(), new int[]{
                    machine.getMachineTypeId(),
                    machine.getLocation().getId()
            });
        }

        this.drops = new HashMap<>();
        for (Drop drop: data.getDroplijst()) {
            this.drops.put(drop.getId(), new int[]{
                    drop.getMachineTypeId(),
                    drop.getLocation().getId()
            });
        }
        this.collects = new HashMap<>();
        for (Collect collect: data.getCollectlijst()) {
            this.collects.put(collect.getId(), new int[]{
                    collect.getMachine().getId(),
                    collect.getMachine().getLocation().getId()
            });
        }

        this.rng = new Random(1);
    }

    /**
     *  Method adds data to the tail of the specified truck's route.
     *
     * @param truck     id of the truck to add to.
     * @param data      data to add to machine.
     */
    public void add(int truck, int[] data){
        this.solution[truck].add(data);
    }

    /**
     *  Insert new data into a given trucks route at a certain stop.
     *
     * @param truck first truck.
     * @param stop  stop to insert at.
     * @param data  data to insert.
     */
    public void insert(int truck, int stop, int[] data){
        solution[truck].add(stop, data);
    }

    /**
     *  Swap data of a given truck at a given location with the data of another given truck at a certain location.
     *
     * @param truck         first specified truck.
     * @param stop          stop specified for the first truck.
     * @param otherTruck    second specified stop.
     * @param otherStop     stop specified for the second truck.
     */
    public void swap(int truck, int stop, int otherTruck, int otherStop){
        int[] temp = solution[truck].get(stop);
        solution[truck].set(stop, solution[otherTruck].get(otherStop));
        solution[otherTruck].set(otherStop, temp);
    }

    /**
     *  Calculates score for the current solution.
     *
     * @return  score.
     */
    public int calculateScore(){

        int score = 0;
        for (LinkedList<Integer> truckDistance: truckDistances) {
            score += truckDistance.getLast();
        }
        return score;
    }

    // TODO prune

    /*
    PROBLEM: <name_of_used_input_file>
    DISTANCE: <value>
    TRUCKS: <number_of_used_trucks>
    <truckId> <distance> <time> <locationId(:machine_id)...>
     */

    /**
     *  Writes current solution to file.
     *
     * @param original  original file specifying the problem.
     */

    public void writeSolution(File original){

        try {
            // TODO make output file variable
            BufferedWriter buffer = new BufferedWriter(new FileWriter(new File("src/data/solution")));
            buffer.append(String.format("PROBLEM: %s\n", original.getName()));
            buffer.append(String.format("DISTANCE: %d\n", getTotalDistance()));
            buffer.append(String.format("TRUCKS: %d\n", getTotalTrucks()));

            int id = -1;
            for(int truck = 0; truck < solution.length; truck++){
                if(solution[truck]!= null && solution[truck].size() != 0){
                    buffer.append(String.format("%d %d %d", truck, getTruckDistance(solution[truck]), getTruckTime(solution[truck])));
                    for(int[] stop: solution[truck]){
                        if(stop[0] != id){
                            id = stop[0];
                            buffer.append(String.format(" %d", stop[0]));
                        }
                        if(stop[1] != -1){
                            buffer.append(String.format(":%d", stop[1]));
                        }
                    }
                    id = -1;
                    buffer.append("\n");
                }
            }
            buffer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getTruckDistance(LinkedList<int[]> truck) {
        int distance = 0;
        for (int stop = 0; stop < truck.size() - 1; stop++) {
            distance += distanceMatrix[truck.get(stop)[0]][truck.get(stop + 1)[0]];
        }
        return distance;
    }

    private int getTruckTime(LinkedList<int[]> truck) {
        int time = 0;
        for (int stop = 0; stop < truck.size() - 1; stop++) {
            if(truck.get(stop)[1] >= 0) time += data.getMachinelijst().get(truck.get(stop)[1]).getServicetime();
            time += timeMatrix[truck.get(stop)[0]][truck.get(stop + 1)[0]];
        }
        if(truck.getLast()[1] >= 0) time += data.getMachinelijst().get(truck.getLast()[1]).getServicetime();
        return time;
    }

    private int getTotalTrucks(){
        int totalTrucks = 0;
        for (LinkedList<int[]> stops : solution) {
            if (stops != null && stops.size() != 0) {
                totalTrucks++;
            }
        }
        return totalTrucks;
    }

    private int getTotalDistance() {
        int totalDistance = 0;
        for (LinkedList<int[]> stops : solution) {
            if (stops != null && stops.size() != 0) {
                totalDistance+=getTruckDistance(stops);
            }
        }
        return totalDistance;
    }

    /**
     *  <p>
     *      Method to check feasibility.
     *      checked constraints:
     *      <ul>
     *          <li>truck does not drive longer than maximum time.</li>
     *          <li>truck starts at it's start location</li>
     *          <li>truck ends at it's end location</li>
     *      </ul>
     *  </p>
     *
     * @return  true if feasible, false if not.
     */

    public boolean checkFeasibility(){

        // temporary variables -----------------------------------------------------------------------------------------
        boolean feasible = true;
        int volume;
        HashMap<Integer, int[]> tempDrops = new HashMap<>(drops);
        HashMap<Integer, int[]> tempCollects = new HashMap<>(collects);
        HashMap<Integer, int[]> tempMachines = new HashMap<>(machines);
        HashSet<Integer> currentMachines;

        // check all trucks --------------------------------------------------------------------------------------------
        // assume all collects and drops handled? ----------------------------------------------------------------------
        for(int truck = 0; truck < solution.length; truck++) {
            // only check if truck has stop list ---------------------------------------------------------------------------
            if(solution[truck] != null && solution[truck].size() != 0){

                // begin and end location ----------------------------------------------------------------------------------
                if(startLocations[truck] != solution[truck].getFirst()[0] ||
                        endLocations[truck] != solution[truck].getLast()[0]) {
                    return false;
                }

                // time check ----------------------------------------------------------------------------------------------
                if(truckTimes[truck].getLast() > MAX_WORKING_TIME){
                    return false;
                }

                // volume check --------------------------------------------------------------------------------------------
                for (LinkedList<Integer> machines: truckCurrentMachines[truck]) {
                    volume = 0;
                    for (Integer machine: machines) volume += machineStats[machine][1];
                    if (volume > MAX_VOLUME) {
                        return false;
                    }
                }

                //TODO PICKUP VOOR COLLECT CHECKEN
                currentMachines = new HashSet<>();
                for (int[] stop: solution[truck]) {
                    // stop is drop or collect -------------------------------------------------------------------------
                    if (stop.length >= 3 && stop[2] != -1) {
                        // check if machine isn't already collected ----------------------------------------------------
                        if (!currentMachines.contains(stop[1]) && collects.containsKey(stop[2])) {
                            // check if machine id and location matche -------------------------------------------------
                            if (stop[1] == collects.get(stop[2])[0] && stop[0] == collects.get(stop[2])[1]) {
                                currentMachines.add(stop[1]);
                                tempCollects.remove(stop[2]);
                            } else {
                                feasible = false;
                                break;
                            }
                        }
                        // check if machine isn't already dropped ------------------------------------------------------
                        else if (currentMachines.contains(stop[1]) && drops.containsKey(stop[2])) {
                            // check if machine type id and location matche --------------------------------------------
                            if (stop[4] == drops.get(stop[2])[0] && stop[0] == collects.get(stop[2])[1]) {
                                currentMachines.remove(stop[1]);
                                tempDrops.remove(stop[2]);
                            } else {
                                feasible = false;
                                break;
                            }
                        } else {
                            feasible = false;
                            break;
                        }
                    }
                    // stop is pickup or drop off of machine at depot --------------------------------------------------
                    else if (stop[1] != -1) {
                        if (!currentMachines.contains(stop[1]) &&
                                machines.containsKey(stop[1]) &&
                                machines.get(stop[1])[1] == stop[0]) {
                            currentMachines.add(stop[1]);
                            machines.remove(stop[1]);
                        } else if (currentMachines.contains(stop[1]) &&
                                !machines.containsKey(stop[1])) {
                            currentMachines.remove(stop[1]);
                        } else {
                            feasible = false;
                            break;
                        }
                    }
                }
                // truck is still loaded -------------------------------------------------------------------------------
                if (currentMachines.size() > 0) {
                    feasible = false;
                    break;
                }
            }
        }

        // check if all drops and collects are done --------------------------------------------------------------------
        if (!tempCollects.isEmpty() || !tempDrops.isEmpty()) {
            feasible = false;
        }

        return feasible;
    }

    // TODO reverse move if infeasible *********************************************************************************
    public void move(){

        // get first truck and matching stops --------------------------------------------------------------------------
        int firstTruck = rng.nextInt(solution.length);
        int firstStop = rng.nextInt(solution[firstTruck].size());
        int firstCollectStop = Math.min(solution[firstTruck].get(firstStop)[3], firstStop);
        int firstDropStop = Math.max(solution[firstTruck].get(firstStop)[3], firstStop);
        int[] collect = solution[firstTruck].get(firstCollectStop);
        int[] drop = solution[firstTruck].get(firstDropStop);

        // TODO chance to swap with available machine ******************************************************************

        // remove from first truck and update delta control structures --------------------------------------------------
        solution[firstTruck].remove(firstDropStop);
        solution[firstTruck].remove(firstCollectStop);
        updateCouplingRemove(firstTruck, firstCollectStop, firstDropStop);
        update(firstTruck, firstCollectStop);

        //get second truck and locations for stops ---------------------------------------------------------------------
        int secondTruck = rng.nextInt(solution.length);
        int secondCollectStop = rng.nextInt(solution[secondTruck].size() - 1);
        int secondDropStop =
                secondCollectStop + 1 +
                rng.nextInt(solution[secondTruck].size() - secondCollectStop - 1);

        // add collect and drop to second truck and update delta control structures ------------------------------------
        // update coupling ---------------------------------------------------------------------------------------------
        updateCouplingAdd(secondTruck, secondCollectStop, secondDropStop);
        // add drop first because the list size and the indexes will change --------------------------------------------
        solution[secondTruck].add(secondDropStop, drop);
        solution[secondTruck].add(secondCollectStop, collect);
        drop[3] = secondCollectStop;
        collect[3] = secondDropStop;
        update(secondTruck, secondCollectStop);
    }

    private void updateCouplingAdd(int truck, int start, int secondEdit){

        for (int stop = start; start < solution[truck].size(); stop++) {
            solution[truck].get(stop)[3] = solution[truck].get(stop)[3] + (stop >= secondEdit ? 2 : 1);
        }
    }

    private void updateCouplingRemove(int truck, int start, int secondEdit){

        for (int stop = start; start < solution[truck].size(); stop++) {
            solution[truck].get(stop)[3] = solution[truck].get(stop)[3] - (stop >= secondEdit ? 2 : 1);
        }
    }

    private void update(int truck, int start) {

        // truck times and volumes updating ----------------------------------------------------------------------------
        for (int stop = start; stop < solution[truck].size(); stop++) {

            // delete previous if exists -------------------------------------------------------------------------------
            if (stop < truckTimes[truck].size()) truckTimes[truck].remove(stop);
            if (stop < truckDistances[truck].size()) truckDistances[truck].remove(stop);
            if (stop < truckCurrentMachines[truck].size()) truckCurrentMachines[truck].remove(stop);

            // updating trucks driven time -----------------------------------------------------------------------------
            truckTimes[truck].addLast(
                    (truckTimes[truck].isEmpty() ? 0 : truckTimes[truck].getLast()) +
                    (stop < 1 ? 0 : timeMatrix[solution[truck].get(stop - 1)[0]][solution[truck].get(stop)[0]]) +
                    (solution[truck].get(stop)[1] == -1 ? 0 : machineStats[solution[truck].get(stop)[1]][2])
            );
            // updating trucks driven distance -------------------------------------------------------------------------
            truckDistances[truck].addLast(
                    (truckDistances[truck].isEmpty() ? 0 : truckDistances[truck].getLast()) +
                    (stop < 1 ? 0 : distanceMatrix[solution[truck].get(stop - 1)[0]][solution[truck].get(stop)[0]])
            );

            // updating machines on truck at certain stop --------------------------------------------------------------
            // else add previous set or new set if no previous ---------------------------------------------------------
            truckCurrentMachines[truck].addLast(truckCurrentMachines[truck].isEmpty() ?
                    new LinkedList<>() :
                    new LinkedList<>(truckCurrentMachines[truck].getLast())
            );
            // if machine collected or dropped at certain location -> add or remove it ---------------------------------
            if (solution[truck].get(stop)[1] != -1) {
                if (truckCurrentMachines[truck].getLast().contains(solution[truck].get(stop)[1])) {
                    truckCurrentMachines[truck].getLast().remove(new Integer(solution[truck].get(stop)[1]));
                } else truckCurrentMachines[truck].getLast().addLast(solution[truck].get(stop)[1]);
            }
        }
    }

    /**
     * Method to load initial solution into right format.
     *
     */
    public void load(){

        // temporary variables -----------------------------------------------------------------------------------------
        HashMap<Integer, Integer> currentMachines;
        Stop stop;

        for(int truck = 0; truck < data.getTrucklijst().size(); truck++){
            currentMachines = new HashMap<>();

            // route building ------------------------------------------------------------------------------------------
            solution[truck] = new LinkedList<>();
            truckTimes[truck] = new LinkedList<>();
            truckDistances[truck] = new LinkedList<>();
            truckCurrentMachines[truck] = new LinkedList<>();
            for(int stopId = 0; stopId < data.getTrucklijst().get(truck).getStoplijst().size(); stopId++){
                stop = data.getTrucklijst().get(truck).getStoplijst().get(stopId);

                // adding stop to solution matrix ----------------------------------------------------------------------
                if(stop.getMachines().isEmpty()) solution[truck].addLast(new int[]{stop.getStoplocatieid(), -1});
                else {
                    for(Integer machine: stop.getMachines()){
                        solution[truck].addLast(new int[]{
                                stop.getStoplocatieid(),
                                machine
                        });
                    }
                }

                // setting bidirectional coupling of collect and drop --------------------------------------------------
                if (solution[truck].get(stopId)[1] != -1 && currentMachines.keySet().contains(solution[truck].get(stopId)[1])){
                    solution[truck].get(stopId)[3] = currentMachines.get(solution[truck].get(stopId)[1]);
                    solution[truck].get(currentMachines.get(solution[truck].get(stopId)[1]))[3] = stopId;
                }
            }

            // updating datastructures for delta evaluation ------------------------------------------------------------
            update(truck, 0);
        }
    }

    /**
     *  Debug output string method.
     *
     * @return  debug output string.
     */
    @Override
    public String toString(){

        StringBuffer buffer = new StringBuffer();

        for(int truck = 0; truck < solution.length; truck++){
            if(solution[truck].isEmpty()) buffer.append("X -");
            else {
                for (int stop = 0; stop < solution[truck].size(); stop++) {
                    if (solution[truck].get(stop) == null) buffer.append("X");
                    else {
                        for (int i = 0; i < solution[truck].get(stop).length; i++) {
                            buffer.append(solution[truck].get(stop)[i]);
                            if (i < solution[truck].get(stop).length - 1) buffer.append(", ");
                        }
                    }
                    buffer.append("\t");
                }
            }
            buffer.append("\n");
        }
        return buffer.toString();
    }
}
