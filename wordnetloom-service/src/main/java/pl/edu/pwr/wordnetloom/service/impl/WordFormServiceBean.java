package pl.edu.pwr.wordnetloom.service.impl;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import pl.edu.pwr.wordnetloom.dao.WordFormDaoLocal;
import pl.edu.pwr.wordnetloom.model.wordnet.Sense;
import pl.edu.pwr.wordnetloom.model.wordnet.Text;
import pl.edu.pwr.wordnetloom.model.wordnet.Word;
import pl.edu.pwr.wordnetloom.model.wordnet.WordForm;
import pl.edu.pwr.wordnetloom.service.WordFormServiceRemote;

@Stateless
public class WordFormServiceBean implements WordFormServiceRemote {

    @EJB
    private WordFormDaoLocal wordForm;

    @Override
    public void deleteAllForms() {
        wordForm.deleteAllForms();
    }

    @Override
    public WordForm getFormFor(Word word, List<String> tags) {
        List<Text> texts = new ArrayList<>();
        tags.stream().forEach((t) -> {
            texts.add(new Text(t));
        });

        return wordForm.getFormFor(word, texts);
    }

    @Override
    public List<Sense> getUnitsWithoutForm() {
        return wordForm.getUnitsWithoutForm();
    }

}
