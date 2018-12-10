import dataclasses.Data;
import heuristics.Oplossing;
import heuristics.SimulatedAnnealing;
import solution.Solution;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JarMain {

    private static final long SAFETY = 10000;

    public static void main(String[] args) {

        // vars --------------------------------------------------------------------------------------------------------
        ArgBundle bundle = new ArgBundle(args);

        // check args --------------------------------------------------------------------------------------------------
        if (!bundle.checkArgs()) return;

        // start processing --------------------------------------------------------------------------------------------
        System.out.println(String.format(
                "starting at %s...",
                new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(new Date(bundle.getStart()))
        ));

        // read data ---------------------------------------------------------------------------------------------------
        System.out.println("reading data...");
        File problemFile = new File(bundle.getProblem());
        Data data;
        try {
            data = bundle.generateData(problemFile);
        } catch (FileNotFoundException e) {
            System.out.println(String.format(
                    "could not read problem file at location %s",
                    bundle.getProblem()
            ));
            return;
        }

        // Oplossing init ----------------------------------------------------------------------------------------------
        System.out.println("building initial solution...");
        Oplossing oplossing = new Oplossing(data);
        Solution initial = oplossing.start(bundle.getSeed());
        System.out.println("initial solution built!");

        // heuristic start ---------------------------------------------------------------------------------------------
        System.out.println("starting with heuristic...");
        SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing(initial, bundle.getStart());
        // calculate new runtime taking into consideration the time already passed -------------------------------------
        simulatedAnnealing.start(
                ((bundle.getTime() * 1000) - (System.currentTimeMillis() - bundle.getStart()) - SAFETY)
        );

        // write output ------------------------------------------------------------------------------------------------
        simulatedAnnealing.getBestesolution().writeSolution(problemFile, new File(bundle.getSolution()));
    }
}
