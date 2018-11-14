public class Solution {

    private int [][][] solution;
    private Timematrix timematrix;
    private Distancematrix distancematrix;

    public Solution(int trucks, int stops, Distancematrix distancematrix, Timematrix timematrix) {
        this.solution = new int[trucks][stops][];
        this.distancematrix = distancematrix;
        this.timematrix = timematrix;
    }

    public void add(int truck, int stop, int[] data){
        this.solution[truck][stop] = data;
    }

    public void swap(int truck, int stop, int otherTruck, int otherStop){
        int[] temp = solution[truck][stop];
        solution[truck][stop] = solution[otherTruck][otherStop];
        solution[otherTruck][otherStop] = temp;
    }

    public int calculateScore(){

        int score = 0;
        for(int truck = 0; truck < solution.length; truck++){
            for(int stop = 0; stop < solution[0].length; stop++){
                if(stop == solution[0].length - 1 || solution[truck][stop] == null || solution[truck][stop+1] == null) break;
                else score += distancematrix.getDistance()
                        [ solution[truck][stop][0] ]
                        [ solution[truck][stop + 1][0] ];
            }
        }
        return score;
    }

    @Override
    public String toString(){

        StringBuffer buffer = new StringBuffer();

        for(int truck = 0; truck < solution.length; truck++){
            for(int stop = 0; stop < solution[0].length; stop++){
                if(solution[truck][stop] == null) buffer.append("X");
                else {
                    for(int i = 0; i < solution[truck][stop].length; i++){
                        buffer.append(solution[truck][stop][i]);
                        if(i < solution[truck][stop].length - 1) buffer.append(", ");
                    }
                }
                buffer.append("\t");
            }
            buffer.append("\n");
        }
        return buffer.toString();
    }
}
