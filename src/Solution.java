import java.util.LinkedList;
import java.util.List;

public class Solution {

    private LinkedList<int[]>[] solution;
    private Timematrix timematrix;
    private Distancematrix distancematrix;

    public Solution(int trucks, Distancematrix distancematrix, Timematrix timematrix) {
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
