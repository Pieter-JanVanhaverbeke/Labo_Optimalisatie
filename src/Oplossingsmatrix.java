public class Oplossingsmatrix {

    private int [] [] oplossing;


    public Oplossingsmatrix(int aantaltrucks, int langsteroute) {
        oplossing = new int[aantaltrucks][langsteroute] ;
    }


    public Oplossingsmatrix(int[][] oplossing) {
        this.oplossing = oplossing;
    }

    public int[][] getOplossing() {
        return oplossing;
    }

    public void setOplossing(int[][] oplossing) {
        this.oplossing = oplossing;
    }

    public void addToOplossing(int x, int y, int oplossingswaarde){
        oplossing[x][y] = oplossingswaarde;
    }


    //TODO methode om oplossing te controleren schrijven
    public boolean checkOplossing(){


        return true;
    }

}
