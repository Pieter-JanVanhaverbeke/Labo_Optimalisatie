import java.io.File;
import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        String fileName;
        if(args.length > 0) fileName = args[0];
        else fileName = "src/data/tvh_problem_3.txt";

        //Beginnen met inlezen:
        Data data = new Data();
        File file = new File(fileName);
        data.leesData(file);


        //Starten oplossing
        Oplossing opl = new Oplossing(data);
        opl.start();

        opl.writeSolution(file.getName());
        }
    }




