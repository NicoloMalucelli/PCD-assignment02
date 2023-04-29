package executors.view;

import executors.controller.Controller;
import utils.*;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class ConsoleView {
    private Controller controller;

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void start(){
        String tmp;
        System.out.print("Root directory: ");
        Scanner scanner = new Scanner(System.in);
        final String dir = scanner.nextLine(); //C:\Users\nicol\Documents\Progetti\scarabeo

        do{
            System.out.print("Number of files to visualize: ");
            tmp = scanner.nextLine();
        }while (!Strings.isNumeric(tmp) || Integer.parseInt(tmp) <= 1);
        final Integer nFiles = Integer.parseInt(tmp);

        do{
            System.out.print("Number of  interval to visualize: ");
            tmp = scanner.nextLine();
        }while (!Strings.isNumeric(tmp) || Integer.parseInt(tmp) <= 1);
        final Integer nIntervals = Integer.parseInt(tmp);

        do{
            System.out.print("Last interval max: ");
            tmp = scanner.nextLine();
        }while (!Strings.isNumeric(tmp) || Integer.parseInt(tmp) <= 1);
        final Integer lastInterval = Integer.parseInt(tmp);

        try {
            Result result = this.controller.getReport(new SetupInfo(dir, nFiles, nIntervals, lastInterval)).get();
            for(AnalyzedFile analyzedFile : result.getRanking()){
                System.out.println(analyzedFile);
            }
            System.out.println();
            for(Map.Entry<Interval, Integer> analyzedFile : result.getDistribution().entrySet()){
                System.out.println(analyzedFile.getKey() + " = " + analyzedFile.getValue());
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

    }



}
