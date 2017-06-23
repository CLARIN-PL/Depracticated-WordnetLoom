package pl.edu.pwr.wordnetloom.dao.impl;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import pl.edu.pwr.wordnetloom.dao.DAOLocal;
import pl.edu.pwr.wordnetloom.dao.POSDaoLocal;
import pl.edu.pwr.wordnetloom.partofspeech.model.PartOfSpeech;

@Stateless
public class POSDaoBean implements POSDaoLocal {

    @EJB
    private DAOLocal local;

    @Override
    public PartOfSpeech getPOSbyID(Long index) {
        return local.getObject(PartOfSpeech.class, index);
    }

    @Override
    public List<PartOfSpeech> getAllPOSes(List<Long> usedLexicons) {
        return local.getEM()
                .createNamedQuery("PartOfSpeech.getAllPOSes", PartOfSpeech.class)
                .setParameter("ids", usedLexicons)
                .getResultList();
    }

    @Override
    public List<PartOfSpeech> getAllPOSes() {
        return local.getEM()
                .createNamedQuery("PartOfSpeech.getAllPOSesNoLexicon", PartOfSpeech.class)
                .getResultList();
    }
}
