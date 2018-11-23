import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class Solution {

    public static final int MAX_TIME = 600;

    /*
    int[]: [locationId, machineId, drop/pickupId, ?miss nog link naar dropoff, wie weet? ,<machineTypeId>]
     */
    private LinkedList<int[]>[] solution;
    private LinkedList<Integer>[] truckTimes;

    private int[][] timeMatrix;
    private int[][] distanceMatrix;
    private int[] startLocations;
    private int[] endLocations;

    private Data data;

    public Solution(Data data) {
        this.solution = new LinkedList[data.getTrucklijst().size()];
        this.distanceMatrix = data.getDistancematrix().getDistance();
        this.timeMatrix = data.getTimematrix().getTime();
        this.startLocations = new int[data.getTrucklijst().size()];
        this.endLocations = new int[data.getTrucklijst().size()];
        this.data = data;
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
        for(int truck = 0; truck < solution.length; truck++){
            for(int stop = 0; stop < solution[0].size(); stop++){
                if(stop == solution[0].size() - 1 || solution[truck].get(stop) == null || solution[truck].get(stop + 1) == null) break;
                else score += distanceMatrix
                        [ solution[truck].get(stop)[0] ]
                        [ solution[truck].get(stop + 1)[0] ];
            }
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
                    if(truck == 30){
                        System.out.println("break");
                    }
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

        for(LinkedList<int[]> truck: solution){

            if(truck != null && truck.size() != 0){

                if(getTruckTime(truck) > MAX_TIME
                        || !data.getStartLocations().contains(truck.getFirst()[0])
                        || !data.getEndLocations().contains(truck.getLast()[0])) return false;
            }
        }
        return true;
    }

    /**
     * Method to load initial solution into right format.
     *
     */
    public void load(){

        for(int truck = 0; truck < data.getTrucklijst().size(); truck++){
            solution[truck] = new LinkedList<>();
            for(Stop stop: data.getTrucklijst().get(truck).getStoplijst()){
                if(stop.getMachines().isEmpty()) solution[truck].addLast(new int[]{stop.getStoplocatieid(), -1});
                else {
                    for(Integer machine: stop.getMachines()){
                        solution[truck].addLast(new int[]{
                                stop.getStoplocatieid(),
                                machine
                        });
                    }
                }
            }
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
