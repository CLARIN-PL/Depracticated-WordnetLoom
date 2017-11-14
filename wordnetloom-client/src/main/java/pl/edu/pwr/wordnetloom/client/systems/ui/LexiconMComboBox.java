package pl.edu.pwr.wordnetloom.client.systems.ui;

import pl.edu.pwr.wordnetloom.client.systems.managers.LexiconManager;
import pl.edu.pwr.wordnetloom.client.systems.misc.CustomDescription;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;

import java.util.List;

//Lexcion combo
public class LexiconMComboBox extends MComboBox<Lexicon> {

    private static final long serialVersionUID = -3667933167567163L;
    private List<Lexicon> all;
    private final String nullRepresentation;

    public LexiconMComboBox(String nullItemRepresentation) {
        nullRepresentation = nullItemRepresentation;
        loadLexicons();
        loadItems();
    }

    private void loadLexicons() {
        all = LexiconManager.getInstance().getFullLexicons();
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
}
