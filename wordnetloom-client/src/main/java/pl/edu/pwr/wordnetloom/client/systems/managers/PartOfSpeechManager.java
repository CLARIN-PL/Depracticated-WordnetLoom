package pl.edu.pwr.wordnetloom.client.systems.managers;

import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.client.workbench.interfaces.Loggable;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class PartOfSpeechManager implements Loggable {

    private static PartOfSpeechManager instance;

    private List<PartOfSpeech> partOfSpeeches;


    private PartOfSpeechManager() {
        List<PartOfSpeech> list = RemoteService.partOfSpeechServiceRemote.findAll();
        partOfSpeeches = Collections.unmodifiableList(Collections.synchronizedList(list));
    }

    public static PartOfSpeechManager getInstance() {
        if (instance == null) {
            synchronized (PartOfSpeechManager.class) {
                if (instance == null) {
                    instance = new PartOfSpeechManager();
                }
            }
        }
        return instance;
    }

    public PartOfSpeech getById(Long id) {
        return partOfSpeeches
                .stream()
                .filter(p -> id.equals(p.getId()))
                .findAny()
                .orElse(null);
    }

    public List<PartOfSpeech> getByLexiconId(Long id) {
        List<PartOfSpeech> filtred = new ArrayList<>();

//        partOfSpeeches.stream().filter((pos) -> (pos.getLexicon().getId() == id)).forEach((pos) -> {
//            filtred.add(pos);
//        });
        return filtred;
    }

    public String partsOfSpeechToString(Set<PartOfSpeech> pos) {
        return pos.stream()
                .map(p -> LocalisationManager.getInstance().getLocalisedString(p.getName()))
                .collect(Collectors.joining(", "));
    }


    public HashMap<PartOfSpeech, Color> getBackgroundColors() {
        HashMap<PartOfSpeech, Color> backgroundColors = new HashMap<>();
        partOfSpeeches.forEach(p -> {
            Color c;
            try {
                c = Color.decode(p.getColor());
            } catch (Exception e) {
                c = Color.decode("#000000");
                logger().warn(p.getColor() + " is not a valid color falling back to #000000");
            }

            backgroundColors.put(p, c);
        });
        return backgroundColors;
    }

    public List<PartOfSpeech> getAll() {
        return partOfSpeeches;
    }
}
