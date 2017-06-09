package pl.edu.pwr.wordnetloom.dao.impl;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import pl.edu.pwr.wordnetloom.dao.DAOLocal;
import pl.edu.pwr.wordnetloom.dao.LexicalUnitDAOLocal;
import pl.edu.pwr.wordnetloom.dao.WordFormDaoLocal;
import pl.edu.pwr.wordnetloom.model.wordnet.Sense;
import pl.edu.pwr.wordnetloom.model.wordnet.Text;
import pl.edu.pwr.wordnetloom.model.wordnet.Word;
import pl.edu.pwr.wordnetloom.model.wordnet.WordForm;

@Stateless
public class WordFormDaoBean implements WordFormDaoLocal {

    @EJB
    private LexicalUnitDAOLocal lexUnit;
    @EJB
    private DAOLocal local;

    @Override
    public void deleteAllForms() {
        local.getEM().createNamedQuery("WordForm.deleteAllForms", WordForm.class)
                .executeUpdate();
    }

    @Override
    public WordForm getFormFor(Word word, List<Text> tags) {
        ArrayList<String> tagS = new ArrayList<String>();
        for (Text text : tags) {
            tagS.add(text.getText());
        }

        List<WordForm> list = local.getEM().createNamedQuery("WordForm.getFormFor", WordForm.class)
                .setParameter("word", word.getWord())
                .setParameter("tags", tagS)
                .getResultList();
        if (list.isEmpty() || list.get(0) == null) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<Sense> getUnitsWithoutForm() {
        return lexUnit.dbGetUnitsWithoutForms();
    }

}
