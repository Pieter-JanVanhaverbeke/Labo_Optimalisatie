package heuristics;

import solution.Solution;

import java.text.SimpleDateFormat;
import java.util.Date;

public class IteratedLocalSearch {

    private Solution current;
    private int currentScore;
    private Solution best;
    private int score;
    private Solution globalBest;
    private int globalScore;

    private long start;
    private long end;

    public IteratedLocalSearch(Solution initial, long start) {
        this.current = new Solution(initial);
        this.currentScore = current.calculateScore();
        this.best = new Solution(initial);
        this.score = best.calculateScore();
        this.globalScore = Integer.MAX_VALUE;

        this.start = start;
    }

    public void run(long time) {

        int deadIterationThreshold = 500;
        int shakerCount = 50;
        end = System.currentTimeMillis() + time;


        // run basic iterated local search -----------------------------------------------------------------------------
        System.out.println("starting iterated local search...");
        System.out.println(String.format(
                "time: %dms\t - distance: %d",
                System.currentTimeMillis() - start,
                currentScore
        ));

        // improve once ------------------------------------------------------------------------------------------------
        improve();

        // start iterated local search ---------------------------------------------------------------------------------
        int newScore;
        int deadIterations = 0;
        while (System.currentTimeMillis() < end) {
            current = best.getBestNeighbourImproved();
            if(current.checkFeasibility()) {
                newScore = current.calculateScore();
                if (score > newScore) {
                    deadIterations = 0;
                    score = newScore;
                    best = new Solution(current);
                    System.out.println(String.format(
                            "time: %dms\t - distance: %d",
                            System.currentTimeMillis() - start,
                            score
                    ));
                } else {
                    deadIterations++;
                }
            }

            if (deadIterations >= deadIterationThreshold) {
                System.out.println("too long without change");
                if (score < globalScore) {
                    globalBest = new Solution(best);
                    globalScore = score;
                }
                // improve routes --------------------------------------------------------------------------------------
                improve();

                // get worse solution ----------------------------------------------------------------------------------
                shake(shakerCount);
                System.out.println(String.format(
                        "time: %dms\t - distance: %d",
                        System.currentTimeMillis() - start,
                        currentScore
                ));
                deadIterations = 0;
            }
        }

        // print result ------------------------------------------------------------------------------------------------
        if (score < globalScore) {
            globalBest = best;
            globalScore = score;
        }
        System.out.println(String.format(
                "finished at %s\nfinal best solution is %d",
                new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(new Date()),
                globalScore
        ));
    }

    private void improve() {
        System.out.println("improving...");

        // local init ------------------------------------------------------------------------------------------
        int tempBest;
        int lastBest = -1;
        int counter;

        // run improve algorithm -------------------------------------------------------------------------------
        for (int truck = 0; truck < current.getTruckCount(); truck++) {
            counter = 0;
            do {
                if (end < System.currentTimeMillis()) return;
                tempBest = current.improveTruck(truck);
                if (lastBest == tempBest || tempBest == -1) counter++;
                lastBest = tempBest;
            } while (counter < 5);
            System.out.println(String.format(
                    "time: %dms\t - distance: %d",
                    System.currentTimeMillis() - start,
                    currentScore = current.calculateScore()
            ));
            if (currentScore < score) {
                best = new Solution(current);
                score = currentScore;
            }
        }
    }

    private void shake(int ruinCounter) {
        for (int ruin = 0; ruin < ruinCounter; ruin++) {
            current.move();
            if (current.checkFeasibility()) {
                best = new Solution(current);
                score = best.calculateScore();
                System.out.println(String.format(
                        "time: %dms\t - distance: %d",
                        System.currentTimeMillis() - start,
                        best.calculateScore()
                ));
            } else {
                current = new Solution(best);
            }
        }
        current = new Solution(best);
    }

    public Solution getGlobalBest() {
        return globalBest;
    }
}
