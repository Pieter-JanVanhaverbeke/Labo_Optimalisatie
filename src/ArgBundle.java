import dataclasses.Data;

import java.io.File;
import java.io.FileNotFoundException;

public class ArgBundle {

    // =================================================================================================================
    // fields ==========================================================================================================
    private String problem;
    private String solution;
    private long seed;
    private long time;
    private long start;

    // =================================================================================================================
    // init ============================================================================================================
    public ArgBundle(String[] args) {
        // default -----------------------------------------------------------------------------------------------------
        problem = null;
        solution = null;
        seed = -1;
        time = -1;
        start = System.currentTimeMillis();

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
    }

    // =================================================================================================================
    // getters and setters =============================================================================================

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public long getSeed() {
        return seed;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    // =================================================================================================================
    // other methods ===================================================================================================

    /**
     * Method used to check and make sure all arguments are provided.
     *
     * @return  true if all needed arguments were provided
     */
    public boolean checkArgs() {

        // return on missing variables ---------------------------------------------------------------------------------
        if (problem == null) {
            System.out.println("missing --problem");
            return false;
        } else if (solution == null) {
            System.out.println("missing --solution");
            return false;
        } else if (seed == -1) {
            System.out.println("missing --seed");
            return false;
        } else if (time == -1) {
            System.out.println("missing --time");
            return false;
        }
        System.out.println(String.format(
                "running tvh solver for problem %s with random seed %d for %d seconds.",
                problem,
                seed,
                time
        ));
        return true;
    }

    /**
     * Method to read in the data from the problem file.
     *
     * @param problemFile   specific problem file
     *
     * @return              read data
     * @throws FileNotFoundException    thrown when the problem file could not be found
     */
    public Data generateData(File problemFile) throws FileNotFoundException {
        Data data = new Data();
        data.leesData(problemFile);
        System.out.println("data read!");
        return data;
    }
}
