package solution;

import dataclasses.Data;
import dataclasses.Machine;
import dataclasses.Stop;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

public class Solution {

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

    public Solution(Data data, long seed) {

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

        this.rng = new Random(seed);
    }

    public Solution(Solution solution) {

        this.rng = solution.rng;

        this.solution = new LinkedList[solution.truckCount];
        this.truckCount = solution.truckCount;
        this.data = solution.data;
        for (int truck = 0; truck < truckCount; truck++) {
            // remove last truck if unused -----------------------------------------------------------------------------
            if (truckCount > data.getTruckCount() && truck == truckCount - 1 && solution.solution[truck].size() == 2) {
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
            for (Integer distance: solution.truckDistances[truck]) {
                this.truckDistances[truck].addLast(new Integer(distance));
            }
        }
        this.truckCurrentMachines = new LinkedList[solution.truckCurrentMachines.length];
        for (int truck = 0; truck < solution.truckCurrentMachines.length; truck++) {
            this.truckCurrentMachines[truck] = new LinkedList<>(solution.truckCurrentMachines[truck]);
        }
        this.startLocations = solution.startLocations.clone();
        this.endLocations = solution.endLocations.clone();
        this.availableMachines = new HashMap<>();
        for (Integer type: solution.availableMachines.keySet()) {
            this.availableMachines.computeIfAbsent(type, k -> new LinkedList<>());
            for (int[] data: solution.availableMachines.get(type)) {
                this.availableMachines.get(type).addLast(data.clone());
            }
        }
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

    public void writeSolution(File original, File outputFile){

        try {
            BufferedWriter buffer = new BufferedWriter(new FileWriter(outputFile));
            buffer.append(String.format("PROBLEM: %s\n", original.getName()));
            buffer.append(this.toString());
            buffer.close();
            System.out.println(String.format("output written to %s", outputFile.getAbsolutePath()));
        } catch (IOException e) {
            System.out.println(String.format(
                    "could not open output at location %s\nwriting to default output solution.txt",
                    outputFile.getAbsolutePath()
            ));
            this.writeSolution(original, new File("solution.txt"));
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
            time += data.getTimeMatrix()[truck.get(stop)[0]][truck.get(stop + 1)[0]];
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

        // check all trucks --------------------------------------------------------------------------------------------
        // assume all collects and drops handled? ----------------------------------------------------------------------
        for(int truck = 0; truck < solution.length; truck++) {
            feasible = checkTruckFeasibility(truck);
            if (!feasible) break;
        }

        return feasible;
    }

    private boolean checkTruckFeasibility(int truck) {
        // temporary variable ------------------------------------------------------------------------------------------
        int volume;

        // only check if truck has stop list ---------------------------------------------------------------------------
        if(solution[truck] != null && solution[truck].size() > 2){

            // begin and end location ----------------------------------------------------------------------------------
            if(startLocations[truck] != solution[truck].getFirst()[0] ||
                    endLocations[truck] != solution[truck].getLast()[0]) {
                return false;
            }

            // time check ----------------------------------------------------------------------------------------------
            if(truckTimes[truck].getLast() > data.getTruckWorkingTime()){
                return false;
            }

            // volume check --------------------------------------------------------------------------------------------
            for (LinkedList<Integer> machines: truckCurrentMachines[truck]) {
                volume = 0;
                for (Integer machine: machines) volume += data.getMachineStats()[machine][1];
                if (volume > data.getTruckCapacity()) {
                    return false;
                }
            }
        }

        return true;
    }

    // TODO reverse move if infeasible *********************************************************************************
    public void move(){

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
        secondTruck = rng.nextInt(data.getTruckCount());
        int secondCollect;
        int secondDrop;
        if (solution[secondTruck].size() > 2) {
            secondCollect = 1 + rng.nextInt(solution[secondTruck].size() - 2);
            secondDrop = secondCollect + rng.nextInt(solution[secondTruck].size() - 1 - secondCollect);
        } else {
            secondCollect = 1;
            secondDrop = 1;
        }

        // update all couplings ----------------------------------------------------------------------------------------
        updateCouplingAdd(secondTruck, secondCollect, secondDrop);
        solution[secondTruck].add(secondDrop, drop);
        solution[secondTruck].add(secondCollect, collect);
        // hot swap collect --------------------------------------------------------------------------------------------
        if (collect[2] == -1 && availableMachines.get(data.getMachineStats()[collect[1]][0]) != null) {
            collect = fullSwapCollect(solution[secondTruck], collect, secondCollect);
            drop[1] = collect[1];
            solution[secondTruck].set(secondCollect, collect);
        }
        // hot swap drop location --------------------------------------------------------------------------
        if (drop[2] == -1) {
            drop[0] = hotSwapDrop(solution[secondTruck], drop, secondDrop);
        }
        drop[3] = secondCollect;
        collect[3] = secondDrop + 1;
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

    private int[] hotSwapCollect(
            LinkedList<int[]> route,
            int[] collect,
            int collectStop,
            HashMap<Integer, LinkedList<int[]>> availableMachines) {

        int[] best = collect;
        for (int[] available : availableMachines.get(data.getMachineStats()[collect[1]][0])) {
            if (data.getTimeMatrix()[route.get(collectStop - 1)[0]][available[0]] +
                    data.getTimeMatrix()[available[0]][route.get(collectStop + 1)[0]] <
                    data.getTimeMatrix()[route.get(collectStop - 1)[0]][collect[0]] +
                            data.getTimeMatrix()[collect[0]][route.get(collectStop + 1)[0]]
                    ) {
                best = available;
            }
        }

        return best;
    }

    private int[] fullSwapCollect(
            LinkedList<int[]> route,
            int[] collect,
            int collectStop) {
        int[] bestCollect;

        availableMachines.get(data.getMachineStats()[collect[1]][0]).addLast(collect);
        bestCollect = hotSwapCollect(route, collect, collectStop, availableMachines);
        availableMachines.get(data.getMachineStats()[collect[1]][0]).remove(bestCollect);

        return bestCollect;
    }

    private int hotSwapDrop(LinkedList<int[]> truck, int[] drop, int dropStop) {
        int best = drop[0];
        for (int depot : data.getDepotArray()) {
            if (data.getTimeMatrix()[truck.get(dropStop - 1)[0]][depot] +
                    data.getTimeMatrix()[depot][truck.get(dropStop + 1)[0]] <
                    data.getTimeMatrix()[truck.get(dropStop - 1)[0]][drop[0]] +
                            data.getTimeMatrix()[drop[0]][truck.get(dropStop + 1)[0]]
                    ) {
                best = depot;
            }
        }
        return best;
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
                            (stop < 1 ? 0 : data.getTimeMatrix()[solution[truck].get(stop - 1)[0]][solution[truck].get(stop)[0]]) +
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

    public Solution getBestNeighbour() {

        // temporary variables -----------------------------------------------------------------------------------------
        Solution best = new Solution(this);
        int bestTimeDelta = Integer.MIN_VALUE;
        Solution base;
        Solution current;
        int firstTruck;
        int firstStop;
        int firstCollect;
        int firstDrop;
        int[] drop;
        int[] collect;

        // get drop and collect from first truck -----------------------------------------------------------------------
        do {
            firstTruck = rng.nextInt(truckCount);
        } while (solution[firstTruck].size() <= 2);
        firstStop = 1 + rng.nextInt(solution[firstTruck].size() - 2);
        firstCollect = Math.min(firstStop, solution[firstTruck].get(firstStop)[3]);
        firstDrop = Math.max(firstStop, solution[firstTruck].get(firstStop)[3]);

        // make new default solution -----------------------------------------------------------------------------------
        base = new Solution(this);

        // update first truck in base ----------------------------------------------------------------------------------
        base.updateCouplingRemove(firstTruck, firstCollect, firstDrop);
        drop = base.solution[firstTruck].remove(firstDrop);
        collect = base.solution[firstTruck].remove(firstCollect);
        if (drop[1] != collect[1]) {
            throw new RuntimeException();
        }
        base.update(firstTruck, firstCollect);

        // check all other trucks for best new solution ----------------------------------------------------------------
        for (int truck = 0; truck < data.getTruckCount(); truck++) {
            current = new Solution(base);
            for (int collectStop = 1; collectStop < current.solution[truck].size(); collectStop++) {
                current.solution[truck].add(collectStop, collect.clone());

                // only check possible drops if collect can be done ----------------------------------------------------
                for (int dropStop = collectStop + 1; dropStop < current.solution[truck].size(); dropStop++) {
                    current.solution[truck].add(dropStop, drop.clone());

                    // only check time if feasible ---------------------------------------------------------------------
                    current.update(truck, collectStop);
                    if (current.checkTruckFeasibility(truck)) {
                        if (base.truckTimes[truck].getLast() - current.truckTimes[truck].getLast() > bestTimeDelta) {

                            // new best solution and update coupling for added collect and drop ------------------------
                            best = new Solution(base);
                            best.updateCouplingAdd(truck, collectStop, dropStop - 1);
                            best.solution[truck].add(collectStop, collect.clone());
                            best.solution[truck].add(dropStop, drop.clone());
                            best.solution[truck].get(collectStop)[3] = dropStop;
                            best.solution[truck].get(dropStop)[3] = collectStop;
                            best.update(truck, collectStop);

                            // update best time delta ------------------------------------------------------------------
                            bestTimeDelta = base.truckTimes[truck].getLast() - best.truckTimes[truck].getLast();
                        }
                    }

                    // revert drop -------------------------------------------------------------------------------------
                    current.solution[truck].remove(dropStop);
                }

                // revert collect --------------------------------------------------------------------------------------
                current.solution[truck].remove(collectStop);
                current.update(truck, collectStop);
            }
        }

        return best;
    }

    public Solution getBestNeighbourImproved() {

        // temporary variables -----------------------------------------------------------------------------------------
        HashMap<Integer, LinkedList<int[]>> availableMachinesCopy;
        Solution best = new Solution(this);
        int bestTimeDelta = Integer.MIN_VALUE;
        Solution base;
        Solution current;
        int firstTruck;
        int firstStop;
        int firstCollect;
        int firstDrop;
        int[] drop;
        int[] collect;
        int[] originalCollect;
        int[] originalDrop;

        // get drop and collect from first truck -----------------------------------------------------------------------
        do {
            firstTruck = rng.nextInt(truckCount);
        } while (solution[firstTruck].size() <= 2);
        firstStop = 1 + rng.nextInt(solution[firstTruck].size() - 2);
        firstCollect = Math.min(firstStop, solution[firstTruck].get(firstStop)[3]);
        firstDrop = Math.max(firstStop, solution[firstTruck].get(firstStop)[3]);

        // make new default solution -----------------------------------------------------------------------------------
        base = new Solution(this);

        // update first truck in base ----------------------------------------------------------------------------------
        base.updateCouplingRemove(firstTruck, firstCollect, firstDrop);
        drop = base.solution[firstTruck].remove(firstDrop);
        collect = base.solution[firstTruck].remove(firstCollect);
        originalCollect = collect.clone();
        originalDrop = drop.clone();
        base.update(firstTruck, firstCollect);

        // check all other trucks for best new solution ----------------------------------------------------------------
        for (int truck = 0; truck < data.getTruckCount(); truck++) {
            // generate new current solution from base and reset available machines ------------------------------------
            current = new Solution(base);
            availableMachinesCopy = new HashMap<>();
            for (Integer key: availableMachines.keySet()) {
                availableMachinesCopy.put(key, new LinkedList<>());
                for (int[] machineList: availableMachines.get(key)) {
                    availableMachinesCopy.get(key).addLast(machineList.clone());
                }
            }
            // reset to original ---------------------------------------------------------------------------------------
            collect = originalCollect.clone();
            drop = originalDrop.clone();

            for (int collectStop = 1; collectStop < current.solution[truck].size(); collectStop++) {
                // hot swap collect ------------------------------------------------------------------------------------
                current.solution[truck].add(collectStop, collect);
                if (collect[2] == -1 &&
                        availableMachinesCopy.get(data.getMachineStats()[collect[1]][0]) != null) {
                    availableMachinesCopy.get(data.getMachineStats()[collect[1]][0]).addLast(collect);
                    collect = hotSwapCollect(
                            current.solution[truck],
                            collect,
                            collectStop,
                            availableMachinesCopy
                    );
                    availableMachinesCopy.get(data.getMachineStats()[collect[1]][0]).remove(collect);
                    drop[1] = collect[1];
                    current.solution[truck].set(collectStop, collect);
                }

                // only check possible drops if collect can be done ----------------------------------------------------
                for (int dropStop = collectStop + 1; dropStop < current.solution[truck].size(); dropStop++) {
                    // hot swap drop -----------------------------------------------------------------------------------
                    current.solution[truck].add(dropStop, drop);
                    if (drop[2] == -1) {
                        drop[0] = hotSwapDrop(
                                current.solution[truck],
                                drop,
                                dropStop
                        );
                    }

                    // only check time if feasible ---------------------------------------------------------------------
                    current.update(truck, collectStop);
                    if (current.checkTruckFeasibility(truck)) {
                        if (base.truckTimes[truck].getLast() - current.truckTimes[truck].getLast() > bestTimeDelta) {

                            // new best solution and update coupling for added collect and drop ------------------------
                            best = new Solution(base);
                            best.availableMachines = new HashMap<>();
                            for (Integer key: availableMachinesCopy.keySet()) {
                                best.availableMachines.put(key, new LinkedList<>());
                                for (int[] machineList : availableMachinesCopy.get(key)) {
                                    best.availableMachines.get(key).addLast(machineList.clone());
                                }
                            }
                            best.updateCouplingAdd(truck, collectStop, dropStop - 1);
                            best.solution[truck].add(collectStop, collect.clone());
                            best.solution[truck].add(dropStop, drop.clone());
                            best.solution[truck].get(collectStop)[3] = dropStop;
                            best.solution[truck].get(dropStop)[3] = collectStop;
                            best.update(truck, collectStop);
                            if (drop[1] != collect[1]) {
                                throw new RuntimeException();
                            }

                            // update best time delta ------------------------------------------------------------------
                            bestTimeDelta = base.truckTimes[truck].getLast() - best.truckTimes[truck].getLast();
                        }
                    }

                    // revert drop -------------------------------------------------------------------------------------
                    current.solution[truck].remove(dropStop);
                }

                // revert collect --------------------------------------------------------------------------------------
                current.solution[truck].remove(collectStop);
                current.update(truck, collectStop);
            }
        }

        return best;
    }

    /**
     * Method to improve a certain trucks route itself. This method will take a random collect/drop pair and try to
     * fins the best possible point to put it in the route. When trying to find the best place an attempt will be made
     * to find a better collect or drop location, in case the collect or drop does not have a drop or collect id. A
     * better collect will be selected from a list of available machines and a better drop will be selected from a list
     * of depot locations. The new route time will be returned.
     *
     * @param truck the truck id of the truck to be optimised
     * @return      the new route time
     */
    public int improveTruck(int truck) {

        if (solution[truck].size() < 6) return -1;
        // temporary variables -----------------------------------------------------------------------------------------
        int bestTime = truckTimes[truck].getLast();
        int time;
        HashMap<Integer, LinkedList<int[]>> availableMachinesCopy = new HashMap<>();
        for (Integer key: availableMachines.keySet()) {
            availableMachinesCopy.put(key, new LinkedList<>());
            for (int[] machineList: availableMachines.get(key)) {
                availableMachinesCopy.get(key).addLast(machineList.clone());
            }
        }
        LinkedList<int[]> copy = new LinkedList<>();
        for (int[] data: solution[truck]) copy.addLast(data.clone());
        LinkedList<int[]> current = new LinkedList<>();
        for (int[] data: solution[truck]) current.addLast(data.clone());
        int firstStop;
        int[] collect;
        int firstCollect;
        int[] drop;
        int firstDrop;

        // get drop/collect pair to swap -------------------------------------------------------------------------------
        firstStop = 1 + rng.nextInt(solution[truck].size() - 2);
        firstCollect = Math.min(firstStop, solution[truck].get(firstStop)[3]);
        firstDrop = Math.max(firstStop, solution[truck].get(firstStop)[3]);
        drop = current.remove(firstDrop);
        collect = current.remove(firstCollect);

        for (int collectStop = 1; collectStop < current.size(); collectStop++) {
            // hot swap collect ----------------------------------------------------------------------------------------
            current.add(collectStop, collect);
            if (collect[2] == -1 && availableMachinesCopy.get(data.getMachineStats()[collect[1]][0]) != null) {
                availableMachinesCopy.get(data.getMachineStats()[collect[1]][0]).addLast(collect);
                collect = hotSwapCollect(current, collect, collectStop, availableMachinesCopy);
                availableMachinesCopy.get(data.getMachineStats()[collect[1]][0]).remove(collect);
                drop[1] = collect[1];
                current.set(collectStop, collect);
            }

            for (int dropStop = collectStop + 1; dropStop < current.size(); dropStop++) {
                // hot swap drop location ------------------------------------------------------------------------------
                current.add(dropStop, drop);
                if (drop[2] == -1) {
                    drop[0] = hotSwapDrop(current, drop, dropStop);
                }

                if ((time = checkTime(current)) != -1 && time < bestTime) {
                    solution[truck] = new LinkedList<>();
                    for (int[] data: copy) solution[truck].addLast(data.clone());
                    if (collect[2] == -1 && availableMachinesCopy.get(data.getMachineStats()[collect[1]][0]) != null) {
                        availableMachines.put(data.getMachineStats()[collect[1]][0], new LinkedList<>());
                        for (int[] machine : availableMachinesCopy.get(data.getMachineStats()[collect[1]][0])) {
                            availableMachines.get(data.getMachineStats()[collect[1]][0]).addLast(machine.clone());
                        }
                    }
                    updateCouplingRemove(truck, firstCollect, firstDrop);
                    solution[truck].remove(firstDrop);
                    solution[truck].remove(firstCollect);
                    updateCouplingAdd(truck, collectStop, dropStop - 1);
                    collect[3] = dropStop;
                    drop[3] = collectStop;
                    solution[truck].add(collectStop, collect.clone());
                    solution[truck].add(dropStop, drop.clone());

                    update(truck, 0);
                    bestTime = time;
                }
                current.remove(dropStop);
            }
            current.remove(collectStop);
        }

        return bestTime;
    }

    /**
     * Method to check parial feasibility of a route. This will only check the volume and the total time of the route
     * and return the new route time if the route is feasible, -1 will will be returned in case the route is infeasible.
     *
     * @param tempRoute the route to be checked
     * @return          the new time for the route if feasible, -1 otherwise
     */
    private int checkTime(LinkedList<int[]> tempRoute) {

        // temporary variables -----------------------------------------------------------------------------------------
        int volume = 0;
        int time = 0;
        HashSet<Integer> currentMachines = new HashSet<>();

        // check all stops to check volume -----------------------------------------------------------------------------
        for (int stop = 0; stop < tempRoute.size() - 1; stop++) {
            time += data.getTimeMatrix()[tempRoute.get(stop)[0]][tempRoute.get(stop + 1)[0]];
            if (tempRoute.get(stop).length > 2) {
                time += data.getMachineStats()[tempRoute.get(stop)[1]][2];
                if (!currentMachines.contains(tempRoute.get(stop)[1])) {
                    currentMachines.add(tempRoute.get(stop)[1]);
                    volume += data.getMachineStats()[tempRoute.get(stop)[1]][1];
                } else {
                    currentMachines.add(tempRoute.get(stop)[1]);
                    volume -= data.getMachineStats()[tempRoute.get(stop)[1]][1];
                }
            }
            if (volume > data.getTruckCapacity() || time > data.getTruckWorkingTime()) {
                time = -1;
                break;
            }
        }

        return time;
    }

    /**
     * Method to load initial solution into right format.
     *
     */
    public void load(){

        // temporary variables -----------------------------------------------------------------------------------------
        HashMap<Integer, Integer> currentMachines;
        Stop stop;
        LinkedList<Machine> machines = new LinkedList<>();
        for (Machine machine: data.getMachinelijst()) machines.addLast(machine);

        for(int truck = 0; truck < data.getTrucklijst().size(); truck++){
            currentMachines = new HashMap<>();

            // route building ------------------------------------------------------------------------------------------
            solution[truck] = new LinkedList<>();
            truckTimes[truck] = new LinkedList<>();
            truckDistances[truck] = new LinkedList<>();
            truckCurrentMachines[truck] = new LinkedList<>();
            solution[truck].addLast(new int[]{startLocations[truck], -1});

            // TODO fuse insertion and bidirectional coupling into one loop ********************************************
            for(int stopId = 0; stopId < data.getTrucklijst().get(truck).getStoplijst().size(); stopId++){
                stop = data.getTrucklijst().get(truck).getStoplijst().get(stopId);

                // adding stop to solution matrix ----------------------------------------------------------------------
                for(Machine machine: stop.getMachines().keySet()){
                    if (currentMachines.containsKey(machine.getId())) {
                        solution[truck].addLast(new int[]{
                                stop.getStoplocatieid(),
                                machine.getId(),
                                stop.getMachines().get(machine),
                                -1,
                                machine.getMachineTypeId()
                        });
                        currentMachines.remove(machine.getId());
                    } else if (!currentMachines.containsKey(machine.getId())) {
                        solution[truck].addLast(new int[]{
                                stop.getStoplocatieid(),
                                machine.getId(),
                                stop.getMachines().get(machine),
                                -1
                        });
                        currentMachines.put(machine.getId(), 0);
                        machines.remove(machine);
                    }
                }
            }
            solution[truck].addLast(new int[]{endLocations[truck], -1});

            currentMachines = new HashMap<>();
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

        // load available machines -------------------------------------------------------------------------------------
        availableMachines = new HashMap<>();
        for (Machine machine: machines) {
            availableMachines.computeIfAbsent(machine.getMachineTypeId(), k -> new LinkedList<>());
            availableMachines.get(machine.getMachineTypeId()).addLast(new int[]{
                    machine.getLocation().getId(),
                    machine.getId(),
                    -1,
                    -1
            });
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

    public void printStats() {
        System.out.println(String.format("DISTANCE: %d\n", getTotalDistance()));
        System.out.println(String.format("TRUCKS: %d\n", getTotalTrucks()));
    }

    // =================================================================================================================
    // getters & setters ===============================================================================================

    public int getTruckCount() {
        return truckCount;
    }

    public void setTruckCount(int truckCount) {
        this.truckCount = truckCount;
    }
}