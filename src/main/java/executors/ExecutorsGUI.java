package executors;

import executors.controller.Controller;
import executors.model.Model;
import executors.view.ConsoleView;
import executors.view.GuiView;

public class ExecutorsGUI {

    public static void main(String[] args){
        final Model model = new Model();
        final Controller controller = new Controller(model);
        final GuiView view = new GuiView(controller);
    }

}
