package pl.edu.pwr.wordnetloom.service.impl;

import java.util.Collection;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import pl.edu.pwr.wordnetloom.dao.ExtGraphExtensionDAOLocal;
import pl.edu.pwr.wordnetloom.dao.impl.DAOBean;
import pl.edu.pwr.wordnetloom.model.wordnet.ExtGraphExtension;
import pl.edu.pwr.wordnetloom.service.ExtGraphExtensionServiceRemote;

/**
 * klasa zarządza rozszerzeniami grafów kandydatów
 *
 * @author lburdka
 */
@Stateless
//FIXME: refactor names
public class ExtGraphExtensionServiceBean extends DAOBean implements ExtGraphExtensionServiceRemote {

    @EJB
    private ExtGraphExtensionDAOLocal extGraphExtension;

    @Override
    public void dbSave(Collection<ExtGraphExtension> exts) {
        extGraphExtension.dbSave(exts);
    }

    @Override
    public Collection<ExtGraphExtension> dbFullGet() {
        return extGraphExtension.dbFullGet();
    }

    @Override
    public Collection<ExtGraphExtension> dbFullGet(Long[] extgraph_ids) {
        return extGraphExtension.dbFullGet(extgraph_ids);
    }

    @Override
    public Collection<ExtGraphExtension> dbFullGet(String word) {
        return extGraphExtension.dbFullGet(word);
    }

    @Override
    public Collection<ExtGraphExtension> dbFullGet(String word, int packageno) {
        return extGraphExtension.dbFullGet(word, packageno);
    }

    @Override
    public List<ExtGraphExtension> dbGetRelation(List<ExtGraphExtension> exts) {
        return extGraphExtension.dbGetRelation(exts);
    }

}
