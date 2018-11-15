import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class Solution {

    /*
    int[]: [locationId, machineId, drop/pickupId, ?miss nog link naar dropoff, wie weet? ,<machineTypeId>]
     */
    private LinkedList<int[]>[] solution;
    private TimeMatrix timematrix;
    private DistanceMatrix distancematrix;

    public Solution(int trucks, DistanceMatrix distancematrix, TimeMatrix timematrix) {
        this.solution = new LinkedList[trucks];
        for(int i = 0; i < solution.length; i++) solution[i] = new LinkedList<int[]>();
        this.distancematrix = distancematrix;
        this.timematrix = timematrix;
    }

    public void add(int truck, int[] data){
        this.solution[truck].add(data);
    }

    public void insert(int truck, int stop, int[] data){
        solution[truck].add(stop, data);
    }

    public void swap(int truck, int stop, int otherTruck, int otherStop){
        int[] temp = solution[truck].get(stop);
        solution[truck].set(stop, solution[otherTruck].get(otherStop));
        solution[otherTruck].set(otherStop, temp);
    }

    public int calculateScore(){

        int score = 0;
        for(int truck = 0; truck < solution.length; truck++){
            for(int stop = 0; stop < solution[0].size(); stop++){
                if(stop == solution[0].size() - 1 || solution[truck].get(stop) == null || solution[truck].get(stop + 1) == null) break;
                else score += distancematrix.getDistance()
                        [ solution[truck].get(stop)[0] ]
                        [ solution[truck].get(stop + 1)[0] ];
            }
        }
        return score;
    }

    /*
    PROBLEM: <name_of_used_input_file>
    DISTANCE: <value>
    TRUCKS: <number_of_used_trucks>
    <truckId> <distance> <time> <locationId(:machine_id)...>
     */

    public void writeSolution(File original){

        try {
            BufferedWriter buffer = new BufferedWriter(new FileWriter(new File("data/solution")));
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
                            buffer.append(String.format(" %d:%d", stop[0], stop[1]));
                        } else {
                            buffer.append(String.format(":%d", stop[1]));
                        }
                    }
                    id = -1;
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
            distance += distancematrix.getDistance()[truck.get(stop)[0]][truck.get(stop + 1)[0]];
        }
        return distance;
    }

    private int getTruckTime(LinkedList<int[]> truck) {
        int time = 0;
        for (int stop = 0; stop < truck.size() - 1; stop++) {
            time += timematrix.getTime()[truck.get(stop)[0]][truck.get(stop + 1)[0]];
        }
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
