package pl.edu.pwr.wordnetloom.extgraph.service.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.edu.pwr.wordnetloom.extgraph.repository.ExtGraphRepository;
import pl.edu.pwr.wordnetloom.extgraph.service.ExtGraphServiceLocal;

@Stateless
public class ExtGraphServiceBean implements ExtGraphServiceLocal {

    @Inject
    private ExtGraphRepository extGraphRepository;

}
