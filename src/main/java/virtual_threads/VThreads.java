package virtual_threads;

import virtual_threads.controller.Controller;
import virtual_threads.model.Model;
import virtual_threads.view.GuiView;

public class VThreads {

    public static void main(String[] args){
        final Model model = new Model();
        final Controller controller = new Controller(model);
        final GuiView view = new GuiView(controller);
    }

}
