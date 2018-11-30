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
    private static final int SEED = 100;
    private static final int TRUCK_COUNT = 40;

    //TODO VOLUME KUNNEN CHECKEN

    //TODO TIJD KUNNEN CHECKEN

    //TODO DROPS EN COLLECTS AFGEHANDELD KUNNEN CHECKEN

    //TODO PICKUP VOOR COLLECT CHECKEN

    //TODO EIND EN BEGINPOS CHECKEN

    private Random rng;

    // per truck list:
    //      int[]: [ locationId, machineId, drop/pickupId, link naar geconnecteerd drop/collect ,<machineTypeId> ]
    private LinkedList<int[]>[] solution;
    private int truckCount;
    private LinkedList<Integer>[] truckTimes;
    private LinkedList<Integer>[] truckDistances;
    // per truck list:
    //      { machine id 1, machine id 2, ... }
    private LinkedList<LinkedList<Integer>>[] truckCurrentMachines;

    private int[] startLocations;
    private int[] endLocations;
    // TODO hotswap machines *******************************************************************************************
    private HashMap<Integer, LinkedList<int[]>> availableMachines;

    private Data data;

    public Solution(Data data) {

        this.solution = new LinkedList[data.getTrucklijst().size()];
        this.truckCount = this.solution.length;
        this.truckCurrentMachines = new LinkedList[data.getTrucklijst().size()];
        this.truckTimes = new LinkedList[data.getTrucklijst().size()];
        this.truckDistances = new LinkedList[data.getTrucklijst().size()];
        this.startLocations = new int[data.getTrucklijst().size()];
        this.endLocations = new int[data.getTrucklijst().size()];
        for (int truck = 0; truck < solution.length; truck++) {
            startLocations[truck] = data.getStartLocations().size() > truck ?
                    data.getStartLocations().get(truck) :
                    (data.getTrucklijst().get(truck).getStoplijst().isEmpty() ?
                            data.getTrucklijst().get(truck).getEndlocationid() :
                            data.getTrucklijst().get(truck).getStoplijst().get(0).getStoplocatieid()
                    );
            endLocations[truck] =  data.getEndLocations().size() > truck ?
                    data.getEndLocations().get(truck) :
                    data.getTrucklijst().get(truck).getEndlocationid();
        }
        this.data = data;

        this.rng = new Random(SEED);
    }

    public Solution(Solution solution, Random random) {

        this.rng = random;

        this.solution = new LinkedList[solution.truckCount];
        this.truckCount = solution.truckCount;
        for (int truck = 0; truck < truckCount; truck++) {
            // remove last truck if unused -----------------------------------------------------------------------------
            if (truckCount > TRUCK_COUNT && truck == truckCount - 1 && solution.solution[truck].size() == 2) {
                truckCount--;
                continue;
            }
            this.solution[truck] = new LinkedList<>();
            for (int[] data: solution.solution[truck]) this.solution[truck].addLast(data.clone());
        }
        this.truckTimes = new LinkedList[solution.truckTimes.length];
        for (int truck = 0; truck < solution.truckTimes.length; truck++) {
            this.truckTimes[truck] = new LinkedList<>();
            for (Integer time: solution.truckTimes[truck]) this.truckTimes[truck].addLast(new Integer(time));
        }
        this.truckDistances = new LinkedList[solution.truckDistances.length];
        for (int truck = 0; truck < solution.truckDistances.length; truck++) {
            this.truckDistances[truck] = new LinkedList<>();
            for (Integer distance: solution.truckDistances[truck]) this.truckDistances[truck].addLast(new Integer(distance));
        }
        this.truckCurrentMachines = new LinkedList[solution.truckCurrentMachines.length];
        for (int truck = 0; truck < solution.truckCurrentMachines.length; truck++) {
            this.truckCurrentMachines[truck] = new LinkedList<>(solution.truckCurrentMachines[truck]);
        }
        this.startLocations = solution.startLocations.clone();
        this.endLocations = solution.endLocations.clone();

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
            buffer.append(this.toString());
            buffer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getTruckDistance(LinkedList<int[]> truck) {
        int distance = 0;
        for (int stop = 0; stop < truck.size() - 1; stop++) {
            distance += data.getDistancematrix()[truck.get(stop)[0]][truck.get(stop + 1)[0]];
        }
        return distance;
    }

    private int getTruckTime(LinkedList<int[]> truck) {
        int time = 0;
        for (int stop = 0; stop < truck.size() - 1; stop++) {
            if(truck.get(stop)[1] >= 0) time += data.getMachineStats()[truck.get(stop)[1]][2];
            time += data.getTimematrix()[truck.get(stop)[0]][truck.get(stop + 1)[0]];
        }
        if(truck.getLast()[1] >= 0) time += data.getMachineStats()[truck.getLast()[1]][2];
        return time;
    }

    private int getTotalTrucks(){
        int totalTrucks = 0;
        for (LinkedList<int[]> stops : solution) {
            if (stops != null && stops.size() > 2) {
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

        // check all trucks --------------------------------------------------------------------------------------------
        // assume all collects and drops handled? ----------------------------------------------------------------------
        for(int truck = 0; truck < solution.length; truck++) {
            // only check if truck has stop list ---------------------------------------------------------------------------
            if(solution[truck] != null && solution[truck].size() > 2){

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
                    for (Integer machine: machines) volume += data.getMachineStats()[machine][1];
                    if (volume > MAX_VOLUME) {
                        return false;
                    }
                }
            }
        }

        return feasible;
    }

    // TODO reverse move if infeasible *********************************************************************************
    public void move(){

        // TODO swap to "empt" truck list ******************************************************************************

        // get collect and drop from first truck -----------------------------------------------------------------------
        int firstTruck;
        do {
            firstTruck = rng.nextInt(truckCount);
        } while (solution[firstTruck].size() <= 2);
        int firstStop = 1 + rng.nextInt(solution[firstTruck].size() - 2);
        int firstCollect = Math.min(firstStop, solution[firstTruck].get(firstStop)[3]);
        int firstDrop = Math.max(firstStop, solution[firstTruck].get(firstStop)[3]);

        // update first truck ------------------------------------------------------------------------------------------
        updateCouplingRemove(firstTruck, firstCollect, firstDrop);
        int[] drop = solution[firstTruck].remove(firstDrop);
        int[] collect = solution[firstTruck].remove(firstCollect);
        if (drop[1] != collect[1]) {
            throw new RuntimeException();
        }
        update(firstTruck, firstCollect);

        // get second truck and positions ------------------------------------------------------------------------------
        int secondTruck;
        do {
            secondTruck = rng.nextInt(TRUCK_COUNT);
        } while (solution[secondTruck].size() <= 2);
        int secondCollect = 1 + rng.nextInt(solution[secondTruck].size() - 2);
        int secondDrop = secondCollect + rng.nextInt(solution[secondTruck].size() - 1 - secondCollect);
/*
        // TODO remove *************************************************************************************************
        System.out.println(String.format(
                "moving from truck %d: %d:%d -> %d:%d\n\titem %d\nto truck %d\n",
                firstTruck,
                collect[0], collect[1],
                drop[0], drop[1],
                (drop[1] == collect[1] ? drop[1] : -1),
                secondTruck
        ));
        // TODO remove *************************************************************************************************
*/
        // update all couplings ----------------------------------------------------------------------------------------
        drop[3] = secondCollect;
        collect[3] = secondDrop + 1;
        updateCouplingAdd(secondTruck, secondCollect, secondDrop);
        solution[secondTruck].add(secondDrop, drop);
        solution[secondTruck].add(secondCollect, collect);
        update(secondTruck, secondCollect);
    }

    private void updateCouplingAdd(int truck, int start, int secondEdit){
        for (int stop = start; stop < solution[truck].size(); stop++) {
            if (solution[truck].get(stop).length > 2) {
                if (solution[truck].get(stop)[3] >= start) {
                    solution[truck].get(stop)[3] += (solution[truck].get(stop)[3] >= secondEdit ? 2 : 1);
                } else if (solution[truck].get(stop)[3] < start) {
                    solution[truck].get(solution[truck].get(stop)[3])[3]
                            += (stop >= secondEdit ? 2 : 1);
                }
            }
        }
    }

    private void updateCouplingRemove(int truck, int start, int secondEdit){
        for (int stop = start; stop < solution[truck].size(); stop++) {
            if (solution[truck].get(stop).length > 2) {
                if (solution[truck].get(stop)[3] < start) {
                    solution[truck].get(solution[truck].get(stop)[3])[3]
                            -= (stop >= secondEdit ? 2 : 1);
                } else if (solution[truck].get(stop)[3] >= start) {
                    solution[truck].get(stop)[3]
                            -= (solution[truck].get(stop)[3] >= secondEdit ? 2 : 1);
                }
            }
        }
    }

    private void update(int truck, int start) {

        // truck times and volumes updating ----------------------------------------------------------------------------
        for (int stop = start; stop < solution[truck].size(); stop++) {

            // delete previous if exists -------------------------------------------------------------------------------
            while (stop < truckTimes[truck].size()) truckTimes[truck].remove(stop);
            while (stop < truckDistances[truck].size()) truckDistances[truck].remove(stop);
            while (stop < truckCurrentMachines[truck].size()) truckCurrentMachines[truck].remove(stop);

            // updating trucks driven time -----------------------------------------------------------------------------
            truckTimes[truck].addLast(
                    (truckTimes[truck].isEmpty() ? 0 : truckTimes[truck].getLast()) +
                    (stop < 1 ? 0 : data.getTimematrix()[solution[truck].get(stop - 1)[0]][solution[truck].get(stop)[0]]) +
                    (solution[truck].get(stop)[1] == -1 ? 0 : data.getMachineStats()[solution[truck].get(stop)[1]][2])
            );
            // updating trucks driven distance -------------------------------------------------------------------------
            truckDistances[truck].addLast(
                    (truckDistances[truck].isEmpty() ? 0 : truckDistances[truck].getLast()) +
                    (stop < 1 ? 0 : data.getDistancematrix()[solution[truck].get(stop - 1)[0]][solution[truck].get(stop)[0]])
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
            solution[truck].addLast(new int[]{startLocations[truck], -1});
            for(int stopId = 0; stopId < data.getTrucklijst().get(truck).getStoplijst().size(); stopId++){
                stop = data.getTrucklijst().get(truck).getStoplijst().get(stopId);

                // adding stop to solution matrix ----------------------------------------------------------------------
                for(Integer machine: stop.getMachines()){
                    solution[truck].addLast(new int[]{
                            stop.getStoplocatieid(),
                            machine,
                            -1,
                            -1
                    });
                }
            }
            solution[truck].addLast(new int[]{endLocations[truck], -1});

            for (int stopId = 0; stopId < solution[truck].size(); stopId++) {

                // setting bidirectional coupling of collect and drop ------------------------------------------
                if (solution[truck].get(stopId).length > 2) {
                    if (solution[truck].get(stopId)[1] != -1 &&
                            currentMachines.keySet().contains(solution[truck].get(stopId)[1])) {
                        solution[truck].get(stopId)[3] = currentMachines.get(solution[truck].get(stopId)[1]);
                        solution[truck].get(currentMachines.get(solution[truck].get(stopId)[1]))[3] = stopId;
                        currentMachines.remove(solution[truck].get(stopId)[1]);
                    }

                    if (solution[truck].get(stopId)[1] != -1 &&
                            !currentMachines.keySet().contains(solution[truck].get(stopId)[1])) {
                        currentMachines.put(solution[truck].get(stopId)[1], stopId);
                    }
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

        buffer.append(String.format("DISTANCE: %d\n", getTotalDistance()));
        buffer.append(String.format("TRUCKS: %d\n", getTotalTrucks()));

        int id = -1;
        for(int truck = 0; truck < solution.length; truck++){
            if(solution[truck]!= null && solution[truck].size() > 2){
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

        return buffer.toString();
    }

    public Random getRNG() {
        return rng;
    }

    public void printStats() {
        System.out.println(String.format("DISTANCE: %d\n", getTotalDistance()));
        System.out.println(String.format("TRUCKS: %d\n", getTotalTrucks()));
    }
}
