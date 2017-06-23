package pl.edu.pwr.wordnetloom.dao.impl;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import pl.edu.pwr.wordnetloom.dao.CorpusExampleDaoLocal;
import pl.edu.pwr.wordnetloom.dao.DAOLocal;
import pl.edu.pwr.wordnetloom.corpusexample.model.CorpusExample;
import pl.edu.pwr.wordnetloom.word.model.Word;

@Stateless
public class CorpusExampleDaoBean implements CorpusExampleDaoLocal {

    @EJB
    private DAOLocal local;

    @Override
    public List<CorpusExample> getCorpusExamplesFor(Word word) {
        return local.getEM().createNamedQuery("CorpusExample.getCorpusExamplesFor", CorpusExample.class)
                .setParameter("word", word.getWord())
                .getResultList();
    }

}
