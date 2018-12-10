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
    private double temperatuur;
    private int eindtemperatuur;
    private double probability;
    private Random rng;

    private long start;
    private double kb;
    private long starttijd;
    private long endtime;
    private int itterations;
    private boolean zoeklocaalmin;
    private int moveteller;

    public SimulatedAnnealing(Solution intitiele, long seed) {
        this.bestesolution = intitiele;
        this.huidigesolution = intitiele;
        this.buursolution = intitiele;                //SOLUTION DAT JE BIJHOUDT MET VERGELEKEN
        this.bestescore = intitiele.calculateScore();
        this.huidigescore = intitiele.calculateScore();
        this.buurscore = intitiele.calculateScore();
        this.rng = new Random(seed);
        this.temperatuur = 40000;
        this.eindtemperatuur = 0;
        this.itterations = 0;



        kb = Math.pow(1.38064852,-23);
    }

    public void start(long time){
        long tijd = System.currentTimeMillis();


        long end = tijd + (1000 * time);

        while (temperatuur > eindtemperatuur && System.currentTimeMillis() < end) {
            simannealing();
        }
        bestesolution.printStats();
    }

    public void simannealing(){
        itterations++;
        buursolution = new solution.Solution(huidigesolution);
        buursolution.move();



        //eerst checken of feasible is
        if (buursolution.checkFeasibility()) {
            moveteller++;
            buurscore = buursolution.calculateScore();      //setten score

            double delta = ((double)huidigescore - (double)buurscore);

            if (buurscore <= huidigescore) {            //NEG IS BETERE OPL


                if(buurscore<huidigescore){
                    itterations=0;
                }

                huidigesolution = new Solution(buursolution);
                huidigescore = huidigesolution.calculateScore();
                cooling();



                //SCORE IS BETER DAN BESTE UPDATEN
                if (buurscore < bestescore) {
                    bestescore = buurscore;
                    bestesolution = new Solution(buursolution);
                }
            }

            //GEEN BETERE NEIGHBOUR
            else{
                probability =  Math.exp(delta/(temperatuur*kb));
                double kans = rng.nextDouble();


                boolean neembuursolution = kans<probability;

                if(neembuursolution){


                    huidigesolution = new Solution(buursolution);               //VERDER GAAN MET BUURSOLUTION DIE ZWAKKER IS IN SCORE
                    huidigescore = huidigesolution.calculateScore();
                    cooling();
                }
                else{
                    if(itterations>50000){
                        itterations = 0;
                        reheating();
                    }
                    //BLIJVEN VERDERWERKEN MET HUIDIGE SOLUTION
                    cooling();
                }

            }


        }

    }

    //TODO TEMPERATUUR OP GOEDE MANIER KOELEN
    private void cooling(){

        temperatuur = 0.9999*temperatuur;               //temperatuur lichtjes dalen

    }

    private void reheating(){
        temperatuur = 15000 + temperatuur ;
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


