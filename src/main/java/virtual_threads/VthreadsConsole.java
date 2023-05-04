package virtual_threads;

import virtual_threads.controller.Controller;
import virtual_threads.model.Model;
import virtual_threads.view.ConsoleView;

import javax.swing.text.View;

public class VthreadsConsole {

    public static void main(String[] args){
        //C:\Users\nicol\Documents\Progetti
        final Model model = new Model();
        final Controller controller = new Controller(model);
        final ConsoleView view = new ConsoleView();
        view.setController(controller);

        view.start();
    }

}
