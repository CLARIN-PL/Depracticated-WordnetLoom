package pl.edu.pwr.wordnetloom.client.plugins.lexeditor.views;

import java.awt.Color;
import java.util.Collection;
import pl.edu.pwr.wordnetloom.client.plugins.viwordnet.views.ViwnGraphViewUI;
import pl.edu.pwr.wordnetloom.client.systems.listeners.SimpleListenerInterface;
import pl.edu.pwr.wordnetloom.client.workbench.abstracts.AbstractView;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Workbench;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.synset.model.Synset;

/**
 * wyswietlenie struktury synsetu
 *
 * @author Max
 */
public class SynsetStructureView extends AbstractView implements SimpleListenerInterface {

    private static final Color colorOfSecond = new Color(220, 220, 255);

    /**
     * kontruktor dla klasy
     *
     * @param workbench - wskaznik dla workbencha
     * @param title - etykieta dla okienka
     * @param showRelations - czy pokazać przycisk z relacjami
     * @param showGoToLexEditor - czy pokazać przycisk do przejscia
     * @param bottomButtons - czy przyciski na dole (true) czy zboku (false)
     * @param viewNumber - number widoku
     * @param graphUI
     */
    public SynsetStructureView(Workbench workbench, String title, boolean showRelations, boolean showGoToLexEditor, boolean bottomButtons, int viewNumber, ViwnGraphViewUI graphUI) {
        super(workbench, title, new SynsetStructureViewUI(showRelations, showGoToLexEditor, bottomButtons, graphUI));
        if (viewNumber == 2) {
            getUI().setBackgroundColor(colorOfSecond); // kolor dla drugiego
        }
    }

    @Override
    public void doAction(Object object, int tag) {
        SynsetStructureViewUI view = (SynsetStructureViewUI) getUI();
        view.refreshData((Synset) object);
    }

    public void refreshData() {
        getViewUI().refreshData(getViewUI().getLastSynset());
    }

    public Synset getLastSynset() {
        return getViewUI().getLastSynset();
    }

    public Collection<Sense> getSelectedUnits() {
        return getViewUI().getSelectedUnits();
    }

    public void addSelectionListener(SimpleListenerInterface newListener) {
        getUI().addActionListener(newListener);
    }

    public void addClickListener(SimpleListenerInterface newListener) {
        getViewUI().addClickListener(newListener);
    }

    public void setLastUnits(Collection<Sense> units) {
        getViewUI().setLastUnits(units);
    }

    private SynsetStructureViewUI getViewUI() {
        return (SynsetStructureViewUI) getUI();
    }

    public void addUnitChangeListener(SimpleListenerInterface newListener) {
        getUI().addActionListener(newListener);
    }

    public void addSynsetUpdateListener(SimpleListenerInterface newListener) {
        ((SynsetStructureViewUI) getUI()).addSynsetUpdateListener(newListener);
    }
}
