import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {



        //Beginnen met inlezen:
        Data data = new Data();
        File file = new File("C:\\Users\\piete\\IdeaProjects\\Labo_Optimalisatie\\inleesfile.txt");
        data.leesData(file);

        }
    }




