import java.io.File;
import java.util.Random;

public class Hillclimbing {
    private Solution bestesolution;
    private Solution huidigesolution;
    private int bestescore;
    private int huidigescore;
    private int teller;
    private Data data;
    private Random rng;

    public Hillclimbing(Solution intitialsolution) {
        bestesolution = intitialsolution;
        huidigesolution = intitialsolution;
        huidigescore = 999999999;
        bestescore = intitialsolution.calculateScore();
        teller=0;
        this.rng = new Random(1);
    }

    public void start(int aantalminuten){
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
        huidigesolution = new Solution(bestesolution);
        huidigesolution = huidigesolution.getBestNeighbour();
        if (huidigesolution.checkFeasibility()) {
            int huidigescore = huidigesolution.calculateScore();
            if (huidigescore < bestescore) {
                bestesolution.printStats();
                bestescore = huidigescore;
                bestesolution = new Solution(huidigesolution);
            }
        }
    }



    public void firstImprovment(){


//      int randomtruck = rng.nextInt(huidigesolution.getSolution().length);
      int randomstop = rng.nextInt();
  //    int randomtruck2 = rng.nextInt(huidigesolution.getSolution().length);
      int randomstop2 = rng.nextInt();

      //RANDOM TRUCK MET RANDOM STOP WISSELEN MET ALLE STOPS VAN TRUCK

          int randomtruck2size = 0;
          for(int i=0;i<randomtruck2size;i++){
              int stop2 = 0;
 //             huidigesolution.swap(randomtruck,randomstop,randomtruck2,stop2);              //ALLE NEIGHBOURS ZIJN ALLE SWAPS VAN RANDOM TRUCK EN RANDOM STOP MET ANDERE TRUCK ZIJN STOPS
              if(huidigesolution.checkFeasibility()){
  //                if(huidigesolution.getTotaldistance()<bestesolution.getTotaldistance()){
//                      bestesolution = new Solution(huidigesolution);
  //                    bestescore = bestesolution.getTotaldistance();
                  }
              }
          }
  //    }


        /*

        1) Voor 2 random trucks, swap alle stops met elkaar
                //IF(improvement)
                THEN bestesolution = huidige solution

            IF(
         */

        /*for(int i=0; i<data.getTrucklijst().size();i++){
            Truck truck = data.getTrucklijst().get(i);
            huidigesolution.swap(i,);
        }
        Solution solution = huidigesolution;

        solution.swap(0,0,0,0);
        solution.



    public void swap(int truck, int stop, int otherTruck, int otherStop){
        int[] temp = solution[truck].get(stop);
        solution[truck].set(stop, solution[otherTruck].get(otherStop));
        solution[otherTruck].set(otherStop, temp);
    }


        */







    public void steepestdescent(){

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
        return "Hillclimbing{" +
                "bestesolution=" + bestesolution +
                ", huidigesolution=" + huidigesolution +
                ", bestescore=" + bestescore +
                ", huidigescore=" + huidigescore +
                ", teller=" + teller +
                '}';
    }
}
