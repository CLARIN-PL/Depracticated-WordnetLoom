package pl.edu.pwr.wordnetloom.ui.search.sense;

import com.google.common.eventbus.EventBus;
import de.felixroske.jfxsupport.FXMLController;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pwr.wordnetloom.application.utils.Loggable;
import pl.edu.pwr.wordnetloom.ui.search.sense.events.SenseSearchEvent;

@FXMLController
public class SenseSearchController implements Loggable {


    @FXML
    private TextField txtSearch;

    private EventBus eventBus;

    @Autowired
    public void SenseSearchController(EventBus bus) {
        eventBus = bus;
    }

    public void onSearch(Event event) {
        eventBus.post(new SenseSearchEvent(txtSearch.getText()));
    }
}
