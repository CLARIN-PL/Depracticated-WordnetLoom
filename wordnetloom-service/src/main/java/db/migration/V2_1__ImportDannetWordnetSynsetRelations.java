package db.migration;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class V2_1__ImportDannetWordnetSynsetRelations implements JdbcMigration {


    private Connection connection;

    @Override
    public void migrate(Connection connection) throws Exception {
        this.connection = connection;

        List<LocalizedString> strings = new ArrayList<>();

        List<RelTyp> rels = getRelationTypes(connection, strings);

        saveAllowedLexicons(rels);
        System.out.println("Saving allowed lexicons done");

        saveSynsetRaltaion(getSynsetRelation(connection));

    }


    private List<SynsetRelation> getSynsetRelation(Connection connection) throws SQLException {

        List<SynsetRelation> synsetRelations = new ArrayList<>();
        String QUERY = "SELECT r.relation_type_id as rt_id, r.syn_set_id as parent, r.target_syn_set_id as child FROM dannet.relations r";
        PreparedStatement statement = connection.prepareStatement(QUERY);

        ResultSet rs = statement.executeQuery();
        while (rs.next()){

            Long tid = rs.getLong("rt_id");
            Long parent = rs.getLong("parent");
            Long child = rs.getLong("child");

            SynsetRelation rt = new SynsetRelation(new Long("888"+parent),new Long("888" +child), tid);
            synsetRelations.add(rt);
        }
        return synsetRelations;
    }

    private List<RelTyp> getRelationTypes(Connection connection, final List<LocalizedString> strings) throws SQLException {

        String QUERY = "SELECT rt.id as id, rt.name as name, rt.word_net_name as wname, rt.reverse_id as rev FROM dannet.relation_types rt";
        String QUERY_COUNT = "SELECT count(s.id) as c FROM wordnet.application_localised_string s";

        PreparedStatement statement = connection.prepareStatement(QUERY);
        PreparedStatement statement2 = connection.prepareStatement(QUERY_COUNT);
        List<RelTyp> rel = new ArrayList<>();

        ResultSet rs = statement.executeQuery();
        ResultSet rs2 = statement2.executeQuery();
        Integer count = 10;

        if(rs2.first()){
            count = rs2.getInt("c");
        }

        AtomicLong nextId = new AtomicLong(count+1);

        while (rs.next()) {

            Long id = rs.getLong("id");
            String name = rs.getString("name");
            String wname = rs.getString("wname");
            Long rev = rs.getLong("rev");

            LocalizedString nameS = new LocalizedString(nextId.getAndIncrement(), name);
            LocalizedString shortDispS = new LocalizedString(nextId.getAndIncrement(), name);
            LocalizedString dispS = new LocalizedString(nextId.getAndIncrement(), name);
            LocalizedString descS = new LocalizedString(nextId.getAndIncrement(), wname);

            strings.add(nameS);
            strings.add(shortDispS);
            strings.add(dispS);
            strings.add(descS);

            RelTyp rt = new RelTyp(id, nameS.id, descS.id, dispS.id, shortDispS.id, rev);
            rel.add(rt);
        }
        return rel;
    }


    private void saveAllowedLexicons(List<RelTyp> relTypes) throws SQLException {
        String INSERT_QUERY = "INSERT INTO wordnet.relation_type_allowed_lexicons (relation_type_id, lexicon_id) VALUES(?, ?)";
        PreparedStatement insert = connection.prepareStatement(INSERT_QUERY);

        for (RelTyp rt : relTypes) {
            insert.setLong(1, rt.id);
            insert.setLong(2, 2l);
            insert.executeUpdate();
        }
    }


    private void saveSynsetRaltaion(List<SynsetRelation> relations) throws SQLException {
        String INSERT_QUERY = "INSERT INTO wordnet.synset_relation (parent_synset_id, child_synset_id, synset_relation_type_id) VALUES(?, ?, ?)";
        PreparedStatement insert = connection.prepareStatement(INSERT_QUERY);

        for (SynsetRelation r : relations) {
            insert.setLong(1, r.parent);
            insert.setLong(2, r.child);
            insert.setLong(3, r.rel);
            insert.executeUpdate();
        }
    }


    private class LocalizedString {
        Long id;
        String value;

        public LocalizedString(Long id, String value) {
            this.id = id;
            this.value = value;
        }
    }

    private class Relation {

        Long parent;
        String child;
        String rel;

        public Relation(Long parent, String child, String rel) {
            this.parent = parent;
            this.child = child;
            this.rel = rel;
        }
    }

    private class SynsetRelation {

        Long parent;
        Long child;
        Long rel;

        public SynsetRelation(Long parent, Long child, Long rel) {
            this.parent = parent;
            this.child = child;
            this.rel = rel;
        }
    }

    private class Sense {
        Long posId;
        Long wordId;
        Long synsetId;
        Long lexiconId = 2l;
        int synset_position = 0;
        int variant;

        public Sense(Long posId, Long wordId, Long synsetId, int synset_position, int variant) {
            this.posId = posId;
            this.wordId = wordId;
            this.synsetId = synsetId;
            this.synset_position = synset_position;
            this.variant = variant;
        }
    }

    private class RelTyp {
        Long id;
        int autoRevers = 0;
        Long nameId;
        Long descId;
        String relationArg = "SYNSET_RELATION";
        Long dispTextId;
        Long shortDispId;
        Long rev;
        String color = "#000000";
        String node_position = "LEFT";

        public RelTyp(Long id, Long nameId, Long descId, Long dispTextId, Long shortDispId, Long rev) {
            this.id = id;
            this.nameId = nameId;
            this.descId = descId;
            this.shortDispId = shortDispId;
            this.dispTextId = dispTextId;
            this.rev = rev;
        }
    }
}