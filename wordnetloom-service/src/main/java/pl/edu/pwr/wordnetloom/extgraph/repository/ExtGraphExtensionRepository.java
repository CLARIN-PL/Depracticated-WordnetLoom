package pl.edu.pwr.wordnetloom.extgraph.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pl.edu.pwr.wordnetloom.common.repository.GenericRepository;
import pl.edu.pwr.wordnetloom.extgraph.model.ExtGraphExtension;
import pl.edu.pwr.wordnetloom.synset.model.Synset;

@Stateless
public class ExtGraphExtensionRepository extends GenericRepository<ExtGraphExtension> {

    @PersistenceContext
    EntityManager em;

    @Inject
    ExtGraphRepository extGraphRepository;

    public void persist(Collection<ExtGraphExtension> exts) {
        for (ExtGraphExtension e : exts) {
            persist(e);
        }
    }

    public Collection<ExtGraphExtension> finaAll(Long[] extgraphIds) {
        if (extgraphIds.length == 0) {
            return new ArrayList<>();
        }
        return getEntityManager().createQuery("SELECT e FROM ExtGraphExtension e WHERE e.extGraph.id IN (:ids)", ExtGraphExtension.class)
                .setParameter("ids", Arrays.asList(extgraphIds))
                .getResultList();
    }

    public Collection<ExtGraphExtension> findByWord(String word) {
        return finaAll(extGraphRepository.findIDsByWord(word).toArray(new Long[]{}));
    }

    public Collection<ExtGraphExtension> findByWordAndPackageNo(String word, int packageno) {
        Long[] ids = (extGraphRepository.findIDsByWordAndPackageNo(word, packageno)).toArray(new Long[]{});
        return finaAll(ids);
    }

    public void deleteBySynset(Synset s) {
        getEntityManager().createQuery("DELETE FROM ExtGraphExtension e WHERE e.extGraph.id IN (SELECT ee.id FROM ExtGraph ee WHERE ee.synset.id = :synset)")
                .setParameter("synset", s.getId())
                .executeUpdate();
    }

    @Override
    protected Class<ExtGraphExtension> getPersistentClass() {
        return ExtGraphExtension.class;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
