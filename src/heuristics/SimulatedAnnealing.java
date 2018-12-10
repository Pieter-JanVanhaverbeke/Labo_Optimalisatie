package heuristics;

import solution.Solution;

import java.util.Random;

public class SimulatedAnnealing {
    private Solution bestesolution;
    private Solution huidigesolution;
    private Solution buursolution;
    private int bestescore;
    private int huidigescore;
    private int buurscore;
    private int temperatuur;
    private double probability;
    private Random rng;
    private long start;

    public SimulatedAnnealing(Solution intitiele, long seed, long start) {
        this.bestesolution = intitiele;
        this.huidigesolution = intitiele;
        this.buursolution = intitiele;                //SOLUTION DAT JE BIJHOUDT MET VERGELEKEN
        this.bestescore = intitiele.calculateScore();
        this.huidigescore = intitiele.calculateScore();
        this.buurscore = intitiele.calculateScore();
        this.rng = new Random(seed);
        this.temperatuur = 10000;
        this.start = start;
    }

    public void start(long time){
        long end = System.currentTimeMillis() + (1000 * time);
        System.out.println(String.format(
                "time: %dms\t - distance: %d",
                System.currentTimeMillis() - start,
                huidigescore
        ));

        while (temperatuur > 0 && System.currentTimeMillis() < end) {
            simannealing();
        }
        System.out.println(String.format(
                "time: %dms\t - distance: %d",
                System.currentTimeMillis() - start,
                bestescore
        ));
    }

    public void simannealing(){
      //  buursolution = new solution.Solution(huidigesolution);
        buursolution = huidigesolution.getBestNeighbour();


        //eerst checken of feasible is
        if (buursolution.checkFeasibility()) {
            buurscore = buursolution.calculateScore();      //setten score

            double delta = (buurscore - huidigescore);   //TODO DELTA DECLAREREN



            if (delta < 0) {            //NEG IS BETERE OPL
                huidigesolution = new Solution(buursolution);
                huidigescore = huidigesolution.calculateScore();

                //SCORE IS BETER DAN BESTE UPDATEN      //TODO BUURSCORE ZETTEN
                if (buurscore < bestescore) {
                    bestescore = buurscore;
                    bestesolution = new Solution(buursolution);
                    bestesolution.printStats();
                    cooling();
                }
            }

            //GEEN BETERE NEIGHBOUR
            else{
                probability =  Math.exp(-delta/temperatuur);
                int prob = (int) (probability*1000);            //boolean setten
                //Random random = new Random
                // TODO moet het ni zo? nog is delen door 1000? ********************************************************
                int kans = rng.nextInt(1000) / 1000;
              //  System.out.println("kans: " + kans);
                boolean neembuursolution = kans<prob;

              //  System.out.println("prob: " + prob);


                if(neembuursolution){
                    huidigesolution = new Solution(buursolution);               //VERDER GAAN MET BUURSOLUTION DIE ZWAKKER IS IN SCORE
                    huidigescore = huidigesolution.calculateScore();
                    System.out.println(String.format(
                            "time: %sms\t - distance: %d",
                            System.currentTimeMillis() - start,
                            huidigescore
                    ));
                    cooling();
                }
                else{
                    //      huidigesolution = new solution.Solution(buursolution,bestesolution.getRNG());                //BLIJVEN VERDERWERKEN MET HUIDIGE SOLUTION
                    cooling();
                }

            }


        }

    }


    //TODO TEMPERATUUR OP GOEDE MANIER KOELEN
    private void cooling(){
        temperatuur = temperatuur-1;
//        System.out.println(temperatuur);
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
}
