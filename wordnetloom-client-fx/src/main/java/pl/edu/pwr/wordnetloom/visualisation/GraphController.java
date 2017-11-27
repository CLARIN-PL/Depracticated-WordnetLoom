package pl.edu.pwr.wordnetloom.visualisation;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import de.felixroske.jfxsupport.FXMLController;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pwr.wordnetloom.common.dto.DataEntry;
import pl.edu.pwr.wordnetloom.remote.RemoteService;
import pl.edu.pwr.wordnetloom.search.results.events.ShowGraphEvent;
import pl.edu.pwr.wordnetloom.synset.model.Synset;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

@FXMLController
public class GraphController implements Initializable {

    @FXML
    private Pane graphPanel;

    private EventBus eventBus;

    private ViwnGraphViewUI ui = new ViwnGraphViewUI();

    private SwingNode swingNode = new SwingNode();

    @Autowired
    public GraphController(final EventBus eventBus) {
        this.eventBus = eventBus;
        eventBus.register(this);
    }

    @Subscribe
    public void onShowGraph(ShowGraphEvent event) {
        Synset rootSynset = RemoteService.synsetRemote.findSynsetBySense(event.getSense(), Arrays.asList(1l, 2l, 3l));
        ui.releaseDataSetCache();

        if (rootSynset != null) {
            Map<Long, DataEntry> entries = RemoteService.synsetRemote.prepareCacheForRootNode(rootSynset, Arrays.asList(1l, 2l, 3l));
            if (entries != null) {
                ui.setEntrySets((HashMap<Long, DataEntry>) entries);
            }
            //pobieranie synsetu z wcześniej pobranej mapy, aby otrzymać obiekt, który ma relacje (nie są leniwymi kolekcjami)
            DataEntry dataEntry = entries.get(rootSynset.getId());
            if (dataEntry != null) {
                rootSynset = dataEntry.getSynset();
            }
            ui.refreshView(rootSynset);
        } else {
            ui.clear();
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        graphPanel.getChildren().add(swingNode);
        
        SwingUtilities.invokeLater(() -> {
            try {
                swingNode.setContent(ui.getSampleGraphViewer());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
