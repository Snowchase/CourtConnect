package edu.ui;

import java.awt.*;
import edu.Controller.Controller;

public class eventViewScreen {

    private Panel eventViewPanel;
    private Controller eventViewController;

    public eventViewScreen(Controller eventViewController) {
        this.eventViewController = eventViewController;
        eventViewPanel = new Panel();

    }
}
