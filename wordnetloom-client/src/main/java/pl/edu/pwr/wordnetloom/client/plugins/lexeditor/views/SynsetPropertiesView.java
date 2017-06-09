package pl.edu.pwr.wordnetloom.client.plugins.lexeditor.views;

import java.awt.Color;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.views.ViwnGraphViewUI;
import pl.edu.pwr.wordnetloom.client.systems.listeners.SimpleListenerInterface;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractView;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;
import pl.edu.pwr.wordnetloom.model.wordnet.Synset;

/**
 * wyswietlenie wlasciwosci synsetu
 *
 * @author Max
 */
public class SynsetPropertiesView extends AbstractView implements SimpleListenerInterface {

    static private Color colorOfSecond = new Color(220, 220, 255); // kolor drugiego okna

    /**
     * kontruktor dla klasy
     *
     * @param workbench - wskaznik dla workbencha
     * @param title - etykieta dla okienka
     * @param viewNumber - number widoku
     * @param graphUI
     */
    public SynsetPropertiesView(Workbench workbench, String title, int viewNumber, ViwnGraphViewUI graphUI) {
        super(workbench, title, new SynsetPropertiesViewUI(graphUI));
        if (viewNumber == 2) {
            getUI().setBackgroundColor(colorOfSecond); // kolor dla drugiego
        }
    }

    /**
     * przyszedl komunikat o tym, ze zmieniony zostal zaznaczony synset
     */
    public void doAction(Object object, int tag) {
        SynsetPropertiesViewUI view = (SynsetPropertiesViewUI) getUI();
        view.refreshData((Synset) object);
    }

    /**
     * dodanie obiektu nasługującego zmian w opisie jednoski
     *
     * @param newListener - sluchacz
     */
    public void addChangeListener(SimpleListenerInterface newListener) {
        getUI().addActionListener(newListener);
    }
}
