package executors;

import executors.controller.Controller;
import executors.model.Model;
import executors.view.ConsoleView;

import javax.swing.text.View;

public class ExecutorsConsole {

    public static void main(String[] args){
        final Model model = new Model();
        final Controller controller = new Controller(model);
        final ConsoleView view = new ConsoleView();
        view.setController(controller);

        view.start();
    }

}
