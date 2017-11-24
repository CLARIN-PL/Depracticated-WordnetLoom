package pl.edu.pwr.wordnetloom.application;

import de.felixroske.jfxsupport.AbstractFxmlView;
import de.felixroske.jfxsupport.FXMLView;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

@FXMLView
public class ApplicationView extends AbstractFxmlView {

    private Pane applicationPane;

    public ApplicationView() {
        Pane pane = new Pane();
        applicationPane = pane;
    }

    @Override
    public Parent getView() {
        return applicationPane;
    }
}
