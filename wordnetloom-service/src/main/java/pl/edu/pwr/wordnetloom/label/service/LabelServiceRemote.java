package pl.edu.pwr.wordnetloom.label.service;

import java.util.List;

public interface LabelServiceRemote {
    List<Object[]> findLabelsByLanguage(String locale);
}
