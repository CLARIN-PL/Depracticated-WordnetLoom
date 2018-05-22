package pl.edu.pwr.wordnetloom.client.systems.ui;

import pl.edu.pwr.wordnetloom.client.systems.managers.LexiconManager;
import pl.edu.pwr.wordnetloom.client.systems.misc.CustomDescription;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;

import java.util.ArrayList;
import java.util.List;

public class LexiconComboBox extends MComboBox<Lexicon> {

    private static final long serialVersionUID = -3667933167567163L;
    private List<Lexicon> all;
    private final String nullRepresentation;

    public LexiconComboBox(String nullItemRepresentation) {
        nullRepresentation = nullItemRepresentation;
        loadLexicons();
        loadItems();
    }

    private void loadLexicons() {
        all = new ArrayList<>(LexiconManager.getInstance().getUserChosenLexicons());
    }

    private void loadItems() {
        removeAllItems();
        addItem(new CustomDescription<>(nullRepresentation, null));
        all.stream().filter((lexicon) -> (lexicon.getId() > 0)).forEach((lexicon) -> {
            addItem(new CustomDescription<>(lexicon.toString(), lexicon));
        });
    }

    public void refreshLexicons() {
        loadLexicons();
        loadItems();
    }

    public Lexicon getSelectedLexicon(){
        int selectedIndex = getSelectedIndex();
        if(selectedIndex == 0){ // if selected item is null representation
            return null;
        }
        return all.get(selectedIndex - 1); // subtract 1, because first element is null (CustomDescription(nullRepresentation, null))
    }
}
