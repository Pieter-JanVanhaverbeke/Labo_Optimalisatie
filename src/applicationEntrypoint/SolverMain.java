package applicationEntrypoint;

import dataclasses.Data;
import heuristics.Hillclimbing;
import heuristics.Oplossing;
import heuristics.SimulatedAnnealing;
import heuristics.TabuSearch;
import solution.Solution;
import tvh.dataClasses.Location;
import tvh.interfaces.ScoreUpdater;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class SolverMain {

    private ScoreUpdater updater;

    public SolverMain(ScoreUpdater updater) {
        this.updater = updater;
    }

    public void run(String[] args) {

        String inputFileName = "src/data/tvh_problem_4.txt";
        String outputFileName = "src/data/solution";
        long seed = 0;
        long time = 600;
        String heuristic = "simulated-annealing";
        int deadIterationThreshold = 200;
        int ruinCount = 200;

        if (args != null) {
            for (String arg : args) {
                if (arg.contains("--problem")) {
                    inputFileName = arg.split("=")[1];
                } else if (arg.contains("--solution")) {
                    outputFileName = arg.split("=")[1];
                } else if (arg.contains("--seed")) {
                    try { seed = Long.parseLong(arg.split("=")[1]); }
                    catch (NumberFormatException e) {
                        System.out.println(String.format(
                                "seed %s not valid, using default seed %d",
                                arg.split("=")[1],
                                seed)
                        );
                    }
                } else if (arg.contains("--time")) {
                    try { time = Long.parseLong(arg.split("=")[1]); }
                    catch (NumberFormatException e) {
                        System.out.println(String.format(
                                "time %s not valid, using default time %d",
                                arg.split("=")[1],
                                time)
                        );
                    }
                } else if (arg.contains("--heuristic")) {
                    heuristic = arg.split("=")[1];
                } else if (arg.contains("--dead-iteration-threshold")) {
                    try { deadIterationThreshold = Integer.parseInt(arg.split("=")[1]); }
                    catch (NumberFormatException e) {
                        System.out.println(String.format(
                                "dead iteration threshold %s not valid, using default %d",
                                arg.split("=")[1],
                                deadIterationThreshold)
                        );
                    }
                } else if (arg.contains("--ruin-count")) {
                    try { ruinCount = Integer.parseInt(arg.split("=")[1]); }
                    catch (NumberFormatException e) {
                        System.out.println(String.format(
                                "ruin count %s not valid, using default %d",
                                arg.split("=")[1],
                                ruinCount)
                        );
                    }
                }
            }
        }

        try {
            File inputFile = new File(inputFileName);
            File outputFile = new File(outputFileName);
            Data data = new Data();
            data.leesData(inputFile);
            Oplossing oplossing = new Oplossing(data);
            Solution initial = oplossing.start();

            switch (heuristic) {
                case "randomsearch":
                    System.out.println(String.format("%s not implemented", heuristic));
                    break;
                case "hillclimbing":
                    Hillclimbing hillclimbing = new Hillclimbing(initial, seed, updater);
                    hillclimbing.start(time);
                    break;
                case "ruin-rebuild":
                    System.out.println(String.format("%s not implemented", heuristic));
                    break;
                case "simulated-annealing":
                    SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing(initial, seed, updater);
                    simulatedAnnealing.start(time);
                    break;
                case "tabu-search":
                    TabuSearch tabuSearch = new TabuSearch(initial);
                    tabuSearch.start(time);
                    break;
                default:
            }

            oplossing.writeSolution(inputFile, outputFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SolverMain solverMain = new SolverMain(new ScoreUpdater() {
            @Override
            public void locations(ArrayList<Location> locations) {

            }

            @Override
            public void done() {
                System.out.println("finished");
            }

            @Override
            public void updateScore(int score, long iteration) {
                System.out.println(String.format(
                        "%d\t%d",
                        iteration,
                        score
                ));
            }
        });
    }
}
