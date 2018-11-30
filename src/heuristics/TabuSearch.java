package heuristics;

import solution.Solution;

public class TabuSearch {
    Solution bestesolution;
    Solution huidigesolution;
    int bestescore;
    int huidigescore;
    int teller;

    //Tabulijst


    public TabuSearch(Solution sol){
        //INTITIALISEER EERSTE SOLUTION
        bestesolution = sol;
        huidigesolution = sol;
         huidigescore = 999999999;
         bestescore = 999999999;
        teller=0;
    }


    public Solution start(long time){
        long end = System.currentTimeMillis() + (time * 1000);
        //AANTAL ITERATIES
        while(System.currentTimeMillis() < end){

            //ZOEK BESTE OPL
            searchNeighbourhood();





            teller++;
        }





        return bestesolution;
    }


    //Functie die kijkt of solution beter is
    public boolean isBetterSolution() {
        if (huidigesolution.calculateScore() < bestesolution.calculateScore()) {
            bestesolution = huidigesolution;
            return true;
        }
        return false;
    }


    public void searchNeighbourhood(){
        //METHODE SWAPPEN
        huidigesolution.swap(1,2,1,1);

        if(huidigesolution.checkFeasibility()){                 //if feasable, checken op score
            huidigescore = huidigesolution.calculateScore();            //halen van score nieuwe solution
            if(huidigescore<bestescore){                                //als beter is updaten beste solution
                bestescore = huidigescore;
                bestesolution = huidigesolution;
            }
        }

    }













}
