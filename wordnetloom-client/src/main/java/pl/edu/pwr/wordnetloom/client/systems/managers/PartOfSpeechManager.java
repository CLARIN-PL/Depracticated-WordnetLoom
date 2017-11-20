package pl.edu.pwr.wordnetloom.client.systems.managers;

import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PartOfSpeechManager {

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

    @Deprecated
    public PartOfSpeech decode(String s) {
        PartOfSpeech pos = null;
        if (s == null || s.equals("default")) {
            pos = partOfSpeeches.get(0);
        }

//        for (int i = 0; i < partOfSpeeches.size() && pos == null; i++) {
//            if (partOfSpeeches.get(i).getName(languageCode).toString().equals(s)) {
//                pos = partOfSpeeches.get(i);
//            }
//        }
        return pos;
    }

    public List<PartOfSpeech> getByLexiconId(Long id) {
        List<PartOfSpeech> filtred = new ArrayList<>();
//        partOfSpeeches.stream().filter((pos) -> (pos.getLexicon().getId() == id)).forEach((pos) -> {
//            filtred.add(pos);
//        });
        return filtred;
    }

    public List<PartOfSpeech> getAll() {
        return partOfSpeeches;
    }
}
