import dataclasses.Data;
import heuristics.Hillclimbing;
import heuristics.Oplossing;
import solution.Solution;
import tvh.dataClasses.Location;
import tvh.interfaces.ScoreUpdater;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

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
        initiele.writeSolution(file, new File("src/data/solution_4"));

        Hillclimbing hillclimbing = new Hillclimbing(initiele, 0, new ScoreUpdater() {
            private int best;

            @Override
            public void locations(ArrayList<Location> locations) {

            }

            @Override
            public void done() {
                System.out.println(String.format("best score: %d", best));
            }

            @Override
            public void updateScore(int score, long iteration) {
                if (score < best) best = score;
                System.out.println(String.format(
                        "%d - %d",
                        iteration,
                        score
                ));
            }
        });
        hillclimbing.start(15);
        hillclimbing.getBestesolution().writeSolution(file, new File("src/data/solution_4"));
    }
}




