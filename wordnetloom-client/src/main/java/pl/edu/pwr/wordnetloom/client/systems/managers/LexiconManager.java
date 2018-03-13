package pl.edu.pwr.wordnetloom.client.systems.managers;

import pl.edu.pwr.wordnetloom.client.remote.RemoteConnectionProvider;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Loggable;
import pl.edu.pwr.wordnetloom.common.model.GenericEntity;
import pl.edu.pwr.wordnetloom.lexicon.model.Lexicon;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
                instance.loadLexiconMarker();
            }
        }
        return instance;
    }

    public void load(List<Lexicon> lexicons) {
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

    public Lexicon findLexiconById(final Long id){
        return lexicons
                .stream()
                .filter( l -> l.getId().equals(id))
                .findAny()
                .orElse(null);
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

    public Set<Lexicon> getUserChosenLexicons() {

        Set<Lexicon> list = new HashSet<>();

        String[] lexiconArray = RemoteConnectionProvider
                .getInstance()
                .getUser()
                .getSettings()
                .getChosenLexicons().split(";");

        for (String element : lexiconArray) {
            try {
                Long id = Long.parseLong(element);
                list.add(findLexiconById(id));
            } catch (NumberFormatException ex) {
                logger().error("Invalid format");
                list = new HashSet<>(lexicons);
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

    public String lexiconNamesToString(Set<Lexicon> lexicon) {
        return lexicon.stream()
                .map(l -> l.getName())
                .collect(Collectors.joining(", "));
    }

    public String lexiconIdToString(Set<Lexicon> lexicon) {
        return lexicon.stream()
                .map(l -> l.getId()+ "")
                .collect(Collectors.joining(";"));
    }

}
