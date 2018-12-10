import heuristics.IteratedLocalSearch;

import java.util.HashMap;

public class OvernightMain {

    public static void main(String[] args) {

        // jobs for hill climbing heuristic ----------------------------------------------------------------------------
        HashMap<String,String> hillClimbJobs = new HashMap<>();
        hillClimbJobs.put(
                "src\\data\\tvh_problem_3.txt",
                "src\\data\\HC_solution_3.txt"
        );
        hillClimbJobs.put(
                "src\\data\\tvh_problem_4.txt",
                "src\\data\\HC_solution_4.txt"
        );
        hillClimbJobs.put(
                "src\\data\\tvh_problem_5.txt",
                "src\\data\\HC_solution_5.txt"
        );
        hillClimbJobs.put(
                "src\\data\\tvh_problem_6.txt",
                "src\\data\\HC_solution_6.txt"
        );
        hillClimbJobs.put(
                "src\\data\\tvh_problem_7.txt",
                "src\\data\\HC_solution_7.txt"
        );
        hillClimbJobs.put(
                "src\\data\\tvh_problem_8.txt",
                "src\\data\\HC_solution_8.txt"
        );

        // jobs for iterated local search heuristic --------------------------------------------------------------------
        HashMap<String,String> iteratedLocalJobs = new HashMap<>();
        iteratedLocalJobs.put(
                "src\\data\\tvh_problem_3.txt",
                "src\\data\\IL_solution_3.txt"
        );
        iteratedLocalJobs.put(
                "src\\data\\tvh_problem_4.txt",
                "src\\data\\IL_solution_4.txt"
        );
        iteratedLocalJobs.put(
                "src\\data\\tvh_problem_5.txt",
                "src\\data\\IL_solution_5.txt"
        );
        iteratedLocalJobs.put(
                "src\\data\\tvh_problem_6.txt",
                "src\\data\\IL_solution_6.txt"
        );
        iteratedLocalJobs.put(
                "src\\data\\tvh_problem_7.txt",
                "src\\data\\IL_solution_7.txt"
        );
        iteratedLocalJobs.put(
                "src\\data\\tvh_problem_8.txt",
                "src\\data\\IL_solution_8.txt"
        );
/*
        // run hill climbing jobs --------------------------------------------------------------------------------------
        for (String key: hillClimbJobs.keySet()) {
            JarHillClimbingMain.main(new String[]{
                    String.format("--problem=%s", key),
                    String.format("--solution=%s", hillClimbJobs.get(key)),
                    String.format("--seed=%d", 152),
                    String.format("--time=%d", 30 * 60),
            });
        }
*/
        // run iterated local search jobs ------------------------------------------------------------------------------
        for (String key: iteratedLocalJobs.keySet()) {
            JarIteratedMain.main(new String[]{
                    String.format("--problem=%s", key),
                    String.format("--solution=%s", iteratedLocalJobs.get(key)),
                    String.format("--seed=%d", 152),
                    String.format("--time=%d", 30 * 60),
            });
        }
    }
}
