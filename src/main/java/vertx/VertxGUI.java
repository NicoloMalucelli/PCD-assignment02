package vertx;

import io.vertx.core.Vertx;
import utils.SetupInfo;
import vertx.controller.Controller;
import vertx.model.Model;
import vertx.view.ConsoleAgent;
import vertx.view.GuiAgent;

public class VertxGUI {

    public static void main(String[] args){
        Vertx vertx = Vertx.vertx();
        Model model = new Model();
        Controller controller = new Controller(model);
        //C:\Users\nicol\Documents\Progetti\scarabeo
        vertx.deployVerticle(new GuiAgent(controller));
    }

}
