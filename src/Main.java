import java.io.File;
import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        String fileName;
        if(args.length > 0) fileName = args[0];
        else fileName = "src/data/tvh_problem_6.txt";

        //Beginnen met inlezen:
        Data data = new Data();
        File file = new File(fileName);
        data.leesData(file);

        //Starten oplossing
        Oplossing opl = new Oplossing(data);
        Solution initiele = opl.start();
        initiele.writeSolution(file);

        Hillclimbing hillclimbing = new Hillclimbing(initiele);
        hillclimbing.start(10000);
        hillclimbing.getBestesolution().writeSolution(file);
    }
}




