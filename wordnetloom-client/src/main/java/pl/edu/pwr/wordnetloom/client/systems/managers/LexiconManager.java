package pl.edu.pwr.wordnetloom.client.systems.managers;

import pl.edu.pwr.wordnetloom.client.remote.RemoteConnectionProvider;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Loggable;
import pl.edu.pwr.wordnetloom.common.model.GenericEntity;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class LexiconManager implements Loggable {

    private static volatile LexiconManager instance = null;
    private List<Lexicon> lexicons;

    private boolean lexiconMarker;

    private LexiconManager() {
        lexicons = new ArrayList<>();
    }

    public static LexiconManager getInstance() {
        if (instance == null) {
            synchronized (LexiconManager.class) {
                instance = new LexiconManager();
            }
        }
        return instance;
    }

    public void loadLexicons(List<Lexicon> lexicons) {
        this.lexicons.clear();
        this.lexicons.addAll(lexicons);
    }

    private void loadLexiconMarker() {
        lexiconMarker = RemoteConnectionProvider.getInstance().getUser().getSettings().getLexionMarker();
    }

    public List<Lexicon> getLexicons() {
        return lexicons;
    }

    public boolean getLexiconMarker() {
        return lexiconMarker;
    }

    public void setLexiconMarkerOn() {
        lexiconMarker = true;
    }

    public void setLexiconMarkerOff() {
        lexiconMarker = false;
    }


    public List<Long> getUserChosenLexiconsIds() {
        List<Long> list = new ArrayList<>();
        String[] lexiconArray = RemoteConnectionProvider.getInstance().getUser().getSettings().getChosenLexicons().split(";");

        for (String element : lexiconArray) {
            try {
                Long id = Long.parseLong(element);
                list.add(id);
            } catch (NumberFormatException ex) {
                logger().error("Invalid format");
                list = getLexiconsIds();
            }
        }
        return list;
    }

    public List<Long> getLexiconsIds() {
        return lexicons
                .stream()
                .map(GenericEntity::getId)
                .collect(Collectors.toList());
    }

}
