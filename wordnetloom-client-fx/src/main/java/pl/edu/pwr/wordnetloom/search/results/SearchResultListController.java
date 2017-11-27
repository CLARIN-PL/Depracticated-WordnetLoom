package pl.edu.pwr.wordnetloom.search.results;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import de.felixroske.jfxsupport.FXMLController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pwr.wordnetloom.remote.RemoteService;
import pl.edu.pwr.wordnetloom.search.results.events.ShowGraphEvent;
import pl.edu.pwr.wordnetloom.search.sense.events.SenseSearchEvent;
import pl.edu.pwr.wordnetloom.sense.model.Sense;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;


@FXMLController
public class SearchResultListController implements Initializable {

    private EventBus eventBus;

    @FXML
    private ListView<Sense> lstResults;

    @Autowired
    public void SearchResultListController(final EventBus bus) {
        eventBus = bus;
        eventBus.register(this);
    }

    @Subscribe
    public void onSearch(SenseSearchEvent event) {

        List<Sense> senses = RemoteService.senseRemote.findByLikeLemma(event.getText(), Arrays.asList(1l, 2l, 3l));
        lstResults.setItems(FXCollections.observableArrayList(senses));

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        lstResults.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            eventBus.post(new ShowGraphEvent(newValue));
        });
    }
}
