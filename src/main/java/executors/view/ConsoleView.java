package executors.view;

import executors.controller.Controller;
import utils.SetupInfo;
import utils.Strings;

import java.io.IOException;
import java.util.Scanner;

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

        this.controller.getReport(new SetupInfo(dir, nFiles, nIntervals, lastInterval)).thenAccept(r -> {
            System.out.println(r.getRanking(2));
            System.out.println(r.getDistribution());
        });

        try {
            this.controller.startScan(new SetupInfo(dir, nFiles, nIntervals, lastInterval));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}
