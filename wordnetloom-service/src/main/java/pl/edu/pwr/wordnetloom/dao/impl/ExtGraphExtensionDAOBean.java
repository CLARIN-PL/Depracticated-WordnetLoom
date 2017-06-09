package pl.edu.pwr.wordnetloom.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import pl.edu.pwr.wordnetloom.dao.DAOLocal;
import pl.edu.pwr.wordnetloom.dao.ExtGraphDAOLocal;
import pl.edu.pwr.wordnetloom.dao.ExtGraphExtensionDAOLocal;
import pl.edu.pwr.wordnetloom.model.wordnet.ExtGraphExtension;
import pl.edu.pwr.wordnetloom.model.wordnet.Synset;

@Stateless
// FIXME: refactor names
public class ExtGraphExtensionDAOBean implements ExtGraphExtensionDAOLocal {

    @EJB
    private ExtGraphDAOLocal extGraphDAO;

    @EJB
    private DAOLocal local;

    @Override
    public void dbSave(Collection<ExtGraphExtension> exts) {
        for (ExtGraphExtension e : exts) {
            local.persistObject(e);
        }
    }

    @Override
    public Collection<ExtGraphExtension> dbFullGet() {
        return local.getEM().createNamedQuery("ExtGraphExtension.dbFullGet", ExtGraphExtension.class)
                .getResultList();
    }

    @Override
    public Collection<ExtGraphExtension> dbFullGet(Long[] extgraph_ids) {
        if (extgraph_ids.length == 0) {
            return new ArrayList<ExtGraphExtension>();
        }
        return local.getEM().createNamedQuery("ExtGraphExtension.dbFullGetIDs", ExtGraphExtension.class)
                .setParameter("ids", Arrays.asList(extgraph_ids))
                .getResultList();
    }

    @Override
    public Collection<ExtGraphExtension> dbFullGet(String word) {
        return dbFullGet(extGraphDAO.getIDsFromWord(word).toArray(new Long[]{}));
    }

    @Override
    public Collection<ExtGraphExtension> dbFullGet(String word, int packageno) {
        Long[] ids = (extGraphDAO.getIDsFromWord(word, packageno)).toArray(new Long[]{});
        return dbFullGet(ids);
    }

    // FIXME: remove, this won't be necessary anymore with hibernate
    @Override
    public List<ExtGraphExtension> dbGetRelation(List<ExtGraphExtension> exts) {
        return exts;
    }

    @Override
    public void dbDeleteForSynset(Synset s) {
        local.getEM().createNamedQuery("ExtGraphExtension.deleteForSynset")
                .setParameter("synset", s.getId())
                .executeUpdate();
    }
}
