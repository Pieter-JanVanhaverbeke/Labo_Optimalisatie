import dataclasses.Data;
import heuristics.Hillclimbing;
import heuristics.Oplossing;
import solution.Solution;
import tvh.dataClasses.Location;
import tvh.interfaces.ScoreUpdater;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class JarMain {

    public static void main(String[] args) {

        // vars --------------------------------------------------------------------------------------------------------
        String problem = null;
        String solution = null;
        long seed = -1;
        long time = -1;
        long start = System.currentTimeMillis();

        // start processing --------------------------------------------------------------------------------------------
        System.out.println(String.format(
                "starting at %s...",
                new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(new Date(start))
        ));

        // read args ---------------------------------------------------------------------------------------------------
        for (String arg: args) {
            if (arg.contains("--problem")) problem = arg.split("=")[1];
            else if (arg.contains("--solution")) solution = arg.split("=")[1];
            else if (arg.contains("--seed")) {
                try { seed = Long.parseLong(arg.split("=")[1]); }
                catch (NumberFormatException e) {
                    System.out.println("could not parse seed...");
                    return;
                }
            }
            else if (arg.contains("--time")) {
                try { time = Long.parseLong(arg.split("=")[1]); }
                catch (NumberFormatException e) {
                    System.out.println("could not parse time...");
                    return;
                }
            }
        }

        // return on missing variables ---------------------------------------------------------------------------------
        if (problem == null) {
            System.out.println("missing --problem...");
            return;
        } else if (solution == null) {
            System.out.println("missing --solution...");
            return;
        } else if (seed == -1) {
            System.out.println("missing --seed...");
            return;
        } else if (time == -1) {
            System.out.println("missing --time...");
            return;
        }
        System.out.println(String.format(
                "running tvh solver for problem %s with random seed %d for %d seconds.",
                problem,
                seed,
                time
        ));

        // read data ---------------------------------------------------------------------------------------------------
        System.out.println("reading data...");
        Data data = new Data();
        File problemFile = new File(problem);
        try {
            data.leesData(problemFile);
            System.out.println("data read!");
        } catch (FileNotFoundException e) {
            System.out.println(String.format(
                    "could not read problem file at locatien %s",
                    problem
            ));
            return;
        }

        // Oplossing init ----------------------------------------------------------------------------------------------
        System.out.println("building initial solution...");
        Oplossing oplossing = new Oplossing(data);
        Solution initial = oplossing.start();
        System.out.println("initial solution built!");

        // heuristic start ---------------------------------------------------------------------------------------------
        System.out.println("starting with heuristic...");
        Hillclimbing hillclimbing = new Hillclimbing(initial, seed, new ScoreUpdater() {

            private int best = initial.calculateScore();
            private long startTime = start;

            @Override
            public void locations(ArrayList<Location> locations) {

            }

            @Override
            public void done() {
                System.out.println(String.format(
                        "finished at %s\nfinal best solution is %d",
                        new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(new Date()),
                        best
                ));
            }

            @Override
            public void updateScore(int score, long iteration) {
                if (score < best) best = score;
                System.out.println(String.format("time: %d ms\t - score: %d", iteration - startTime, score));
            }
        });
        // calculate new runtime taking into consideration the time already passed -------------------------------------
        hillclimbing.start(
                (time * 1000) - (System.currentTimeMillis() - start) - 20000
        );

        // write output ------------------------------------------------------------------------------------------------
        hillclimbing.getBestesolution().writeSolution(problemFile, new File(solution));
    }
}
