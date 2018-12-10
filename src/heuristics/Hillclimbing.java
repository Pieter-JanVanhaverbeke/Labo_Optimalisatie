package heuristics;

import solution.Solution;

import java.util.Random;

public class Hillclimbing {
    private Solution bestesolution;
    private Solution huidigesolution;
    private int bestescore;
    private int huidigescore;
    private int teller;
    private Random rng;
    private long start;

    public Hillclimbing(Solution intitialsolution, long seed, long start) {
        bestesolution = intitialsolution;
        huidigesolution = intitialsolution;
        huidigescore = 999999999;
        bestescore = intitialsolution.calculateScore();
        teller=0;
        this.rng = new Random(seed);
        this.start = start;
    }

    public void start(long runtime){
        bestesolution.printStats();
        long end = System.currentTimeMillis() + runtime;
        while (System.currentTimeMillis() < end) {
            localSearch();
            teller++;
        }
        bestesolution.printStats();
    }

    public void localSearch(){
        huidigesolution = bestesolution.getBestNeighbourImproved();
        if (huidigesolution.checkFeasibility()) {
            int huidigescore = huidigesolution.calculateScore();
            if (huidigescore < bestescore) {
                bestescore = huidigescore;
                bestesolution = new Solution(huidigesolution);
                System.out.println(String.format(
                        "time: %dms\t - distance: %d",
                        System.currentTimeMillis() - start,
                        bestescore
                ));
                bestesolution.printStats();
            }
        }
    }

    public Solution getBestesolution() {
        return bestesolution;
    }

    public void setBestesolution(Solution bestesolution) {
        this.bestesolution = bestesolution;
    }

    public Solution getHuidigesolution() {
        return huidigesolution;
    }

    public void setHuidigesolution(Solution huidigesolution) {
        this.huidigesolution = huidigesolution;
    }

    public int getBestescore() {
        return bestescore;
    }

    public void setBestescore(int bestescore) {
        this.bestescore = bestescore;
    }

    public int getHuidigescore() {
        return huidigescore;
    }

    public void setHuidigescore(int huidigescore) {
        this.huidigescore = huidigescore;
    }

    public int getTeller() {
        return teller;
    }

    public void setTeller(int teller) {
        this.teller = teller;
    }

    @Override
    public String toString() {
        return "heuristics.Hillclimbing{" +
                "bestesolution=" + bestesolution +
                ", huidigesolution=" + huidigesolution +
                ", bestescore=" + bestescore +
                ", huidigescore=" + huidigescore +
                ", teller=" + teller +
                '}';
    }
}
