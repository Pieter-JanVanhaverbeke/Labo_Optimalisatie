import dataclasses.Data;
import heuristics.Hillclimbing;
import heuristics.Oplossing;
import solution.Solution;

import java.io.File;
import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        String fileName;
        if(args.length > 0) fileName = args[0];
        else fileName = "src/data/tvh_problem_4.txt";
        long start = System.currentTimeMillis();
        long seed = 0;

        //Beginnen met inlezen:
        Data data = new Data();
        File file = new File(fileName);
        data.leesData(file);

        //Starten oplossing
        Oplossing opl = new Oplossing(data);
        Solution initiele = opl.start(seed);
        initiele.writeSolution(file, new File("src/data/solution_4"));

        Hillclimbing hillclimbing = new Hillclimbing(initiele, 0, start);
        hillclimbing.start(15);
        hillclimbing.getBestesolution().writeSolution(file, new File("src/data/solution_4"));
    }
}




