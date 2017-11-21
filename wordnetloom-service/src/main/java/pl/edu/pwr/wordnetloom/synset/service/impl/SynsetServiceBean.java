package pl.edu.pwr.wordnetloom.synset.service.impl;

import pl.edu.pwr.wordnetloom.common.dto.DataEntry;
import pl.edu.pwr.wordnetloom.relationtype.model.RelationType;
import pl.edu.pwr.wordnetloom.sense.model.Sense;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import pl.edu.pwr.wordnetloom.synset.repository.SynsetRepository;
import pl.edu.pwr.wordnetloom.synset.service.SynsetServiceLocal;
import pl.edu.pwr.wordnetloom.synset.service.SynsetServiceRemote;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@Stateless
@Remote(SynsetServiceRemote.class)
@Local(SynsetServiceLocal.class)
public class SynsetServiceBean implements SynsetServiceLocal {

    @Inject
    SynsetRepository synsetRepository;

    @Override
    public void clone(Synset synset) {
        //add sense cloning
    }

    @Override
    public boolean delete(Synset synset) {
        // remove  relations
        // remove units
        // remove synset
        synsetRepository.delete(synset);
        return true;
    }

    @Override
    public Synset findSynsetBySense(Sense sense, List<Long> lexicons) {
        return synsetRepository.findSynsetBySense(sense, lexicons);
    }

    public void delete(RelationType relation, List<Long> lexicons) {
        //Removes relation with subrelations
//        Collection<SynsetRelationType> children = findChildren(relation);
//        for (SynsetRelationType item : children) {
//            synsetRelationRepository.delete(relation);
//            delete(item);
//        }
//        delete(relation);
    }

    @Override
    public Map<Long, DataEntry> prepareCacheForRootNode(Synset synset, List<Long> lexicons) {
        return synsetRepository.prepareCacheForRootNode(synset.getId(), lexicons);

//        Map<Long, DataEntry> map = new HashMap<>();
//
//        long start = System.currentTimeMillis();
//        // step 1 - synset relations from and to root synset (synset)
//        List<SynsetRelation> rels = relations.getRelatedRelations(synset, lexicons); // fetch
//        DataEntry rootEntry = new DataEntry();
//        rootEntry.setSynset(synset);
//
//        HashMap<Long, Synset> synsets = new HashMap<>(); // wszystkie synsety do wyświetlenia (z relacji 1wszego stopnia)
//
//        // step 1.5 - setting associations
//        for (SynsetRelation s : rels) {
//            if (s.getSynsetFrom().getId().equals(synset.getId())) {
//                rootEntry.getRelsFrom().add(s);
//            } else {
//                rootEntry.getRelsTo().add(s);
//            }
//
//            synsets.put(s.getSynsetFrom().getId(), s.getSynsetFrom());
//            synsets.put(s.getSynsetTo().getId(), s.getSynsetTo());
//        }
//
//        synsets.remove(synset.getId());
//
//        // step 2 - synset relations from and to related synsets
//        if (!synsets.isEmpty()) { // otherwise Unexpected end of Subtree exception
//            rels = relations.getRelatedRelations(synsets.keySet());
//            map.put(rootEntry.getSynset().getId(), rootEntry);
//            synsets.put(rootEntry.getSynset().getId(), rootEntry.getSynset());
//
//            List<SynsetInfo> infos = dao.getEM().
//                    createQuery("SELECT NEW pl.edu.pwr.wordnetloom.model.dto.SynsetInfo(sy.id, se.partOfSpeech.id, name.text, lemma.word, syt.value.text, se.senseNumber, lexId.text) FROM Synset sy JOIN sy.senseToSynset AS sts JOIN sts.sense AS se JOIN se.domain AS dom JOIN dom.name AS name JOIN sy.synsetAttributes AS syt JOIN se.lemma as lemma JOIN se.lexicon as lex JOIN lex.lexiconIdentifier as lexId WHERE sts.senseIndex = 0 AND syt.type.typeName.text = :abstractName AND sy.id IN (:ids)", SynsetInfo.class)
//                    .setParameter("abstractName", Synset.ISABSTRACT)
//                    .setParameter("ids", lexicons)
//                    .getResultList();
//
//            List<CountInfo> counts = dao.getEM()
//                    .createQuery("SELECT NEW pl.edu.pwr.wordnetloom.model.dto.CountInfo(sy.id, count(se)) FROM Synset AS sy JOIN sy.senseToSynset AS sts JOIN sts.sense AS se WHERE sy.id IN (:ids) GROUP BY sy.id", CountInfo.class)
//                    .setParameter("ids", lexicons)
//                    .getResultList();
//
//            HashMap<Long, CountInfo> counter = new HashMap<>();
//            for (CountInfo c : counts) {
//                counter.put(c.getSynsetID(), c);
//            }
//
//            for (SynsetInfo sysInf : infos) {
//                DataEntry e = map.get(sysInf.getSynsetID());
//                StringBuilder sb = new StringBuilder();
//                if ("1".equals(sysInf.getIsAbstract())) {
//                    sb.append("S ");
//                }
//                sb.append(sysInf.getWord());
//                sb.append(" ");
//                sb.append(sysInf.getSenseNumber());
//                sb.append(" (");
//                sb.append(sysInf.getDomain());
//                sb.append(")");
//
//                CountInfo c = counter.get(sysInf.getSynsetID());
//                if (c != null && c.getCount() != null && c.getCount().intValue() > 1) {
//                    sb.append(" ...");
//                }
//
//                e.setLabel(sb.toString());
//                e.setLexicon(sysInf.getLexicon());
//                e.setPosID(sysInf.getPosID());
//            }
//        }
//
//        // step 4 - finish
//        long end = System.currentTimeMillis();
//        System.out.println("Time: " + Long.toString(end - start));
    }
}
