package pl.edu.pwr.wordnetloom.client.systems.ui;

import pl.edu.pwr.wordnetloom.client.systems.managers.PartOfSpeechManager;
import pl.edu.pwr.wordnetloom.client.systems.misc.CustomDescription;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;

import java.util.List;

//Pos Combo
public class PartOfSpeechComboBox extends MComboBox<PartOfSpeech> {

    private static final long serialVersionUID = -2688488007035679886L;
    private List<PartOfSpeech> all;
    private String nullRepresentation;

    public PartOfSpeechComboBox(String nullItemRepresentation) {
        nullRepresentation = nullItemRepresentation;
    }

    private void loadPartOfSpeech() {
        all = PartOfSpeechManager.getInstance().getAll();
    }

    private void loadPosByLexicon(long id) {
        all = PartOfSpeechManager.getInstance().getByLexiconId(id);
    }

    private void loadItems() {
        removeAllItems();
        addItem(new CustomDescription<>(nullRepresentation, null));
        all.stream().forEach((pos) -> {
            //  addItem(new CustomDescription<>(pos.getName(RemoteConnectionProvider.getInstance().getLanguage()), pos));
        });
    }

    public void filterByLexicon(Lexicon lexicon) {
        loadPosByLexicon(lexicon.getId());
        loadItems();
    }

    public void withoutFilter() {
        loadPartOfSpeech();
        loadItems();
    }
}
