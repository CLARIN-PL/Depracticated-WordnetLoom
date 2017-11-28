package pl.edu.pwr.wordnetloom.ui.search;

import de.felixroske.jfxsupport.FXMLController;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.pwr.wordnetloom.application.utils.Loggable;
import pl.edu.pwr.wordnetloom.ui.search.results.SearchResultListView;
import pl.edu.pwr.wordnetloom.ui.search.sense.SenseSearchView;

import javax.annotation.PostConstruct;

@FXMLController
public class SearchController implements Loggable {

    @Autowired
    SenseSearchView senseSearchView;

    @Autowired
    SearchView searchView;

    @Autowired
    SearchResultListView searchResultListView;

    @PostConstruct
    public void init() {

        ((TabPane) searchView.getView()).getTabs().clear();
        ((TabPane) searchView.getView()).getTabs().add(wrapInTab(senseSearchView.getView()));

    }

    private Tab wrapInTab(Node n) {
        Tab tab = new Tab();
        tab.setContent(n);
        return tab;
    }
}
