package heuristics;

import solution.Solution;
import tvh.interfaces.ScoreUpdater;

import java.io.File;
import java.util.Random;

public class Hillclimbing {
    private Solution bestesolution;
    private Solution huidigesolution;
    private int bestescore;
    private int huidigescore;
    private int teller;
    private ScoreUpdater scoreUpdater;
    private Random rng;

    public Hillclimbing(Solution intitialsolution, long seed, ScoreUpdater updater) {
        bestesolution = intitialsolution;
        huidigesolution = intitialsolution;
        huidigescore = 999999999;
        bestescore = intitialsolution.calculateScore();
        teller=0;
        this.rng = new Random(seed);
        this.scoreUpdater = updater;


    }

    public void start(long aantalminuten){
        aantalminuten = aantalminuten*60000;
        bestesolution.printStats();
        long end = System.currentTimeMillis() + aantalminuten;
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
                scoreUpdater.updateScore(bestescore,System.currentTimeMillis());
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
