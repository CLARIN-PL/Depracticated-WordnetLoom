package pl.edu.pwr.wordnetloom.label.service.impl;

import pl.edu.pwr.wordnetloom.common.dto.StringMapEntry;
import pl.edu.pwr.wordnetloom.label.exception.UnsupportedLanguageException;
import pl.edu.pwr.wordnetloom.label.repository.LabelRepository;
import pl.edu.pwr.wordnetloom.label.service.LabelServiceLocal;
import pl.edu.pwr.wordnetloom.label.service.LabelServiceRemote;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
@Remote(LabelServiceRemote.class)
@Local(LabelServiceLocal.class)
public class LabelServiceBean implements LabelServiceRemote{

    @Inject
    LabelRepository labelRepository;

    @Override
    public List<Object[]> findLabelsByLanguage(String locale) {
        List<Object[]> labels = labelRepository.findAllLabels(locale);
        if(labels.isEmpty()){
            throw new UnsupportedLanguageException(locale);
        }
        return labels;
    }












}
