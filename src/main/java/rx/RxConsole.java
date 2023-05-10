package rx;

import rx.controller.Controller;
import rx.model.Model;
import rx.view.ConsoleView;

public class RxConsole {

    public static void main(String[] args){
        //C:\Users\nicol\Documents\Progetti\Scarabeo
        Controller controller = new Controller(new Model());
        ConsoleView view = new ConsoleView();
        view.setController(controller);
        view.start();
    }

}
