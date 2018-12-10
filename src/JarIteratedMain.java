import dataclasses.Data;
import heuristics.IteratedLocalSearch;
import heuristics.Oplossing;
import solution.Solution;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JarIteratedMain {

    public static void main(String[] args) {

        // vars --------------------------------------------------------------------------------------------------------
        ArgBundle bundle = new ArgBundle(args);

        // check args --------------------------------------------------------------------------------------------------
        bundle.checkArgs();

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
        IteratedLocalSearch iteratedLocalSearch = new IteratedLocalSearch(initial, bundle.getSeed(), bundle.getStart());
        // calculate new runtime taking into consideration the time already passed -------------------------------------
        iteratedLocalSearch.run(
                ((bundle.getTime() * 1000) - (System.currentTimeMillis() - bundle.getStart()) - 20000)
        );

        // write output ------------------------------------------------------------------------------------------------
        iteratedLocalSearch.getGlobalBest().writeSolution(problemFile, new File(bundle.getSolution()));
    }
}
