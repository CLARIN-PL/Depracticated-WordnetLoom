package pl.edu.pwr.wordnetloom.plugins.lexeditor.views;

import java.awt.Color;
import java.util.Collection;

import pl.edu.pwr.wordnetloom.plugins.viwordnet.views.ViwnGraphViewUI;
import pl.edu.pwr.wordnetloom.systems.listeners.SimpleListenerInterface;
import pl.edu.pwr.wordnetloom.workbench.abstracts.AbstractView;
import pl.edu.pwr.wordnetloom.workbench.interfaces.Workbench;
import pl.edu.pwr.wordnetloom.model.Sense;
import pl.edu.pwr.wordnetloom.model.Synset;

/**
 * wyswietlenie struktury synsetu
 * @author Max
 */
public class SynsetStructureView extends AbstractView implements SimpleListenerInterface {

	static private Color colorOfSecond = new Color(220,220,255); // kolor drugiego okna

	/**
	 * kontruktor dla klasy
	 * @param workbench - wskaznik dla workbencha
	 * @param title - etykieta dla okienka
	 * @param showRelations - czy pokazać przycisk z relacjami
	 * @param showGoToLexEditor - czy pokazać przycisk do przejscia
	 * @param bottomButtons - czy przyciski na dole (true) czy zboku (false) 
	 * @param viewNumber - number widoku
	 * @param graphUI 
	 */
	public SynsetStructureView(Workbench workbench, String title, boolean showRelations, boolean showGoToLexEditor, boolean bottomButtons, int viewNumber, ViwnGraphViewUI graphUI) {
		super(workbench,title,new SynsetStructureViewUI(showRelations,showGoToLexEditor,bottomButtons, graphUI));
		if (viewNumber==2)
			getUI().setBackgroundColor(colorOfSecond); // kolor dla drugiego
	}

	/**
	 * przyszedl komunikat o tym, ze zmieniony zostal zaznaczony synset
	 */
	public void doAction(Object object, int tag) {
		SynsetStructureViewUI view=(SynsetStructureViewUI)getUI();
		view.refreshData((Synset)object);
	}

	/**
	 * odswiezenie listy leksemow
	 */
	public void refreshData() {
		getViewUI().refreshData(getViewUI().getLastSynset());
	}

	/**
	 * odczytanie ostatniego synsetu
	 * @return ostani synset
	 */
	public Synset getLastSynset() {
		return getViewUI().getLastSynset();
	}

	/**
	 * odczytanie wybranych jednostek
	 * @return kolekcja jednostka
	 */
	public Collection<Sense> getSelectedUnits() {
		return getViewUI().getSelectedUnits();
	}

	/**
	 * dodanie obiektu nasługującego klikniecia zmiane zaznaczenia na liscie
	 * @param newListener - sluchacz
	 */
	public void addSelectionListener(SimpleListenerInterface newListener) {
		getUI().addActionListener(newListener);
	}
	
	/**
	 * dodanie sluchacza dla zdarzenia klikniecia w przycisk
	 * @param newListener - sluchacz
	 */
	public void addClickListener(SimpleListenerInterface newListener) {
		getViewUI().addClickListener(newListener);
	}

	/**
	 * ostatnio zaznaczone jednostki
	 * @param units - ostatnio zaznaczone jednostki
	 */
	public void setLastUnits(Collection<Sense> units) {
		getViewUI().setLastUnits(units);
	}

	/**
	 * odczytanie prawdziwego ui widoku
	 * @return ui widoku
	 */
	private SynsetStructureViewUI getViewUI() {
		return (SynsetStructureViewUI)getUI();
	}
	
	/**
	 * dodanie obiektu nasługującego zmian w zaznaczeniu jednostki
	 * @param newListener - sluchacz
	 */
	public void addUnitChangeListener(SimpleListenerInterface newListener) {
		getUI().addActionListener(newListener);
	}
	
	public void addSynsetUpdateListener(SimpleListenerInterface newListener) {
		((SynsetStructureViewUI)getUI()).addSynsetUpdateListener(newListener);
	}
}
