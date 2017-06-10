package pl.edu.pwr.wordnetloom.client.systems.ui;

import java.util.List;
import pl.edu.pwr.wordnetloom.client.Main;
import pl.edu.pwr.wordnetloom.client.systems.managers.PosManager;
import pl.edu.pwr.wordnetloom.client.systems.misc.CustomDescription;
import pl.edu.pwr.wordnetloom.model.wordnet.Lexicon;
import pl.edu.pwr.wordnetloom.model.wordnet.PartOfSpeech;

//Pos Combo
public class PartOfSpeechComboBox extends ComboBoxPlain<PartOfSpeech> {

    private static final long serialVersionUID = -2688488007035679886L;
    private List<PartOfSpeech> all;
    private String nullRepresentation;

    public PartOfSpeechComboBox(String nullItemRepresentation) {
        this.nullRepresentation = nullItemRepresentation;
    }

    private void loadPartOfSpeech() {
        all = PosManager.getInstance().getAllPOSes();
    }

    private void loadPosByLexicon(long id) {
        all = PosManager.getInstance().getPOSForLexicon(id);
    }

    private void loadItems() {
        removeAllItems();
        addItem(new CustomDescription<>(nullRepresentation, null));
        all.stream().forEach((pos) -> {
            addItem(new CustomDescription<>(pos.toString(), pos));
        });
    }

    public void showUbyItems() {
        removeAllItems();
        List<PartOfSpeech> ubyOnly = PosManager.getInstance().getPOSForLexicon(1);
        addItem(new CustomDescription<>(nullRepresentation, null));
        for (PartOfSpeech pos : ubyOnly) {
            String desc = Main.getResouce("label." + pos.getUbyType().toString());
            addItem(new CustomDescription<>(desc, pos));
        }
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
