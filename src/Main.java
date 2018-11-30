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

        //Beginnen met inlezen:
        Data data = new Data();
        File file = new File(fileName);
        data.leesData(file);

        //Starten oplossing
        Oplossing opl = new Oplossing(data);
        Solution initiele = opl.start();
        initiele.writeSolution(file, new File("src/data/solution"));

        Hillclimbing hillclimbing = new Hillclimbing(initiele);
        hillclimbing.start(1);
        hillclimbing.getBestesolution().writeSolution(file, new File("src/data/solution"));
    }
}




