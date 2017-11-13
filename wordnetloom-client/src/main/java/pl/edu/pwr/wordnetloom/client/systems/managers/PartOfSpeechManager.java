package pl.edu.pwr.wordnetloom.client.systems.managers;

import pl.edu.pwr.wordnetloom.client.remote.RemoteService;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PartOfSpeechManager {

    private static PartOfSpeechManager instance;

    private List<PartOfSpeech> cache;


    private PartOfSpeechManager() {
        List<PartOfSpeech> list = RemoteService.partOfSpeechServiceRemote.findAll();
        cache = Collections.unmodifiableList(Collections.synchronizedList(list));
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
        PartOfSpeech pos = null;
        for (int i = 0; i < cache.size() && pos == null; i++) {
            if (cache.get(i).getId().intValue() == id) {
                pos = cache.get(i);
            }
        }
        return pos;
    }

    public PartOfSpeech getNormalized(PartOfSpeech pos) {
        if (pos == null) {
            return null;
        }

        for (PartOfSpeech p : cache) {
            if (p.getId().equals(pos.getId())) {
                return p;
            }
        }
        return null;
    }

    @Deprecated
    public PartOfSpeech decode(String s) {
        PartOfSpeech pos = null;
        if (s == null || s.equals("default")) {
            pos = cache.get(0);
        }

//        for (int i = 0; i < cache.size() && pos == null; i++) {
//            if (cache.get(i).getName(languageCode).toString().equals(s)) {
//                pos = cache.get(i);
//            }
//        }
        return pos;
    }

    public List<PartOfSpeech> getByLexiconId(Long id) {
        List<PartOfSpeech> filtred = new ArrayList<>();
//        cache.stream().filter((pos) -> (pos.getLexicon().getId() == id)).forEach((pos) -> {
//            filtred.add(pos);
//        });
        return filtred;
    }

    public List<PartOfSpeech> getAll() {
        return cache;
    }
}