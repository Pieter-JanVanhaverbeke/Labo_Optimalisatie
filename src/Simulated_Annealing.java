import java.util.Random;

public class Simulated_Annealing {
    private Solution bestesolution;
    private Solution huidigesolution;
    private Solution buursolution;
    private int bestescore;
    private int huidigescore;
    private int buurscore;
    private int temperatuur;
    private double probability;
    private Random rng;

    public Simulated_Annealing(Solution intitiele) {
        this.bestesolution = intitiele;
        this.huidigesolution = intitiele;
        this.buursolution = intitiele;                //SOLUTION DAT JE BIJHOUDT MET VERGELEKEN
        this.bestescore = intitiele.calculateScore();
        this.huidigescore = intitiele.calculateScore();
        this.buurscore = intitiele.calculateScore();
        this.rng = new Random(1);
        this.temperatuur = 5000;
    }

    public void start(){
        //  aantalminuten = aantalminuten*60000;
        bestesolution.printStats();
        //  long end = System.currentTimeMillis() + aantalminuten;
        while (temperatuur>0) {
            simannealing();
        }
        bestesolution.printStats();
    }

    public void simannealing(){
        buursolution = new Solution(huidigesolution);
        buursolution.move();

        //eerst checken of feasible is
        if (buursolution.checkFeasibility()) {
            buurscore = buursolution.calculateScore();      //setten score

            double delta = buurscore - huidigescore;   //TODO DELTA DECLAREREN
            if (delta < 0) {            //NEG IS BETERE OPL

                //SCORE IS BETER DAN BESTE UPDATEN      //TODO BUURSCORE ZETTEN
                if (buurscore < bestescore) {
                    bestesolution.printStats();
                    bestescore = huidigescore;
                    bestesolution = new Solution(buursolution);
                    huidigesolution = new Solution(buursolution);
                    //     buursolution = new Solution(huidigesolution, huidigesolution.getRNG());
                    cooling();
                }
            }

            //GEEN BETERE NEIGHBOUR
            else{
                probability =  Math.exp(-delta/temperatuur);
                int prob = (int) (probability*1000);            //boolean setten
                boolean neembuursolution = rng.nextInt(1000)>prob;
                System.out.println("prob: " + prob);


                if(neembuursolution){
                    buursolution = new Solution(huidigesolution);               //VERDER GAAN MET BUURSOLUTION DIE ZWAKKER IS IN SCORE
                    huidigescore = buurscore;
                    cooling();
                }
                else{
                    //      huidigesolution = new Solution(buursolution,bestesolution.getRNG());                //BLIJVEN VERDERWERKEN MET HUIDIGE SOLUTION
                    cooling();
                }

            }


        }

    }


    //TODO TEMPERATUUR OP GOEDE MANIER KOELEN
    private void cooling(){
        temperatuur = temperatuur-50;
        System.out.println(temperatuur);
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