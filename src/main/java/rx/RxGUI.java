package rx;

import rx.controller.Controller;
import rx.model.Model;
import rx.view.GUIView;

public class RxGUI {

    public static void main(String[] args){
        final Controller controller = new Controller(new Model());
        final GUIView view = new GUIView(controller);
    }
}
