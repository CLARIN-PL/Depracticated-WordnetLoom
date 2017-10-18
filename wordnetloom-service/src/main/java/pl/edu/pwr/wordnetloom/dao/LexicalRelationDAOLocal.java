package pl.edu.pwr.wordnetloom.dao;

import pl.edu.pwr.wordnetloom.model.RelationType;
import pl.edu.pwr.wordnetloom.model.Sense;
import pl.edu.pwr.wordnetloom.model.SenseRelation;

import javax.ejb.Local;
import java.util.List;
import java.util.Set;

@Local
public interface LexicalRelationDAOLocal extends DAOLocal {

    void dbDelete(SenseRelation rel);

    void dbDelete(RelationType relation);

    void dbDeleteAll();

    public SenseRelation dbGet(Long id);

    public List<SenseRelation> dbGetSubRelations(Sense sense, RelationType relationType);

    public List<SenseRelation> dbFastGetRelations(RelationType relationType);

    public Set<Long> dbSelftRelations();

    public List<SenseRelation> dbGetUpperRelations(Sense sense, RelationType relationType);

    public List<SenseRelation> dbGetFullRelations(Sense parent);

    public List<SenseRelation> dbGetRelations(Sense parent, Sense child, RelationType relationType);

    public SenseRelation dbGetRelation(Sense parent, Sense child, RelationType relationType);

    public boolean dbMakeRelation(Sense parent, Sense child, RelationType relation);

    public void dbDeleteConnection(Sense sense);

    public List<SenseRelation> dbFullGetRelations();

    public int dbGetRelationsCount();

    public int dbGetRelationUseCount(RelationType relation);

    public void dbMove(RelationType oldRelation, RelationType newRelation);

    public boolean dbRelationExists(Sense parent, Sense child, RelationType relationType);

    public List<RelationType> dbGetRelationTypesOfUnit(Sense sense);

    public int dbGetRelationCountOfUnit(Sense sense);

    public int dbDeleteImproper();

}
