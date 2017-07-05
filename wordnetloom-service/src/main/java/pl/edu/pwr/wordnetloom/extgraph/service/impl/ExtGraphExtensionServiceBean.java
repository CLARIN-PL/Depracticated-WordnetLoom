package pl.edu.pwr.wordnetloom.extgraph.service.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;
import pl.edu.pwr.wordnetloom.extgraph.repository.ExtGraphExtensionRepository;
import pl.edu.pwr.wordnetloom.extgraph.service.ExtGraphExtensionServiceLocal;

@Stateless
public class ExtGraphExtensionServiceBean implements ExtGraphExtensionServiceLocal {

    @Inject
    private ExtGraphExtensionRepository extGraphExtensionRepository;

}
