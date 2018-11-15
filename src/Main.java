import java.io.File;
import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        String fileName;
        if(args.length > 0) fileName = args[0];
        else fileName = "src/data/tvh_problem_3.txt";

        //Beginnen met inlezen:
        Data data = new Data();
        File file = new File(fileName);
        data.leesData(file);


        //Starten oplossing
        Oplossing opl = new Oplossing(data);
        opl.start();

        opl.writeSolution(file.getName());

        Solution solution = new Solution(data.getTrucklijst().size(), data.getDistancematrix(), data.getTimematrix());
        solution.add(2, new int[]{1, 2});
        solution.add(2, new int[]{2, 3});
        solution.add(1, new int[]{1, 2});
        solution.add(1, new int[]{2, 3});
        solution.add(1, new int[]{3, 4});
        System.out.println(solution);
        solution.insert(1, 1, new int[]{10,11});
        solution.swap(1, 0, 2, 1);
        System.out.println(solution);
        }
    }




