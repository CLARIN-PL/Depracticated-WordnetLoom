package db.migration;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import pl.edu.pwr.wordnetloom.synset.model.SynsetAttributes;
import pl.edu.pwr.wordnetloom.word.model.Word;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class V2_0__ImportDannetWordnetSenses implements JdbcMigration {


    private Connection connection;

    @Override
    public void migrate(Connection connection) throws Exception {
        this.connection = connection;

        saveSense(getSenses(connection));
        System.out.println("Saving sense done");

        List<LocalizedString> strings = new ArrayList<>();

        List<RelTyp> rels = getRelationTypes(connection, strings);

        saveLocalisedString(strings);
        System.out.println("Saving strings done");

        saveRelType(rels);
        System.out.println("Saving relation types done");

    }

    private List<Sense> getSenses(Connection connection) throws SQLException {
        String QUERY = "SELECT s.word_id as id, s.syn_set_id as syn, w.pos_tag_id as pos FROM dannet.word_senses s JOIN dannet.words w ON w.id = s.word_id";
        PreparedStatement statement = connection.prepareStatement(QUERY);
        List<Sense> senses = new ArrayList<>();
        ResultSet rs = statement.executeQuery();
        while (rs.next()){

            Long wid = rs.getLong("id");
            Long syn = rs.getLong("syn");
            Long pos = getPos(rs.getInt("pos"));

            Sense sen = new Sense(pos, wid, new Long("888"+syn), 0, 1);
            senses.add(sen);

        }
        return senses;
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

            SynsetRelation rt = new SynsetRelation(parent,child, tid);
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

    private Long getPos(int id) {
        switch (id) {
            case 29:
                return 2l;
            case 30:
                return 1l;
            case 31:
                return 4l;
            case 32:
                return 5l;
        }
        return 5l;
    }

    private void saveSense(List<Sense> senses) throws SQLException {
        String INSERT_QUERY = "INSERT INTO wordnet.sense (word_id, lexicon_id, part_of_speech_id, variant, synset_id, synset_position, domain_id) VALUES(?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement insert = connection.prepareStatement(INSERT_QUERY);

        for (Sense se : senses) {
            insert.setLong(1, se.wordId);
            insert.setLong(2, se.lexiconId);
            insert.setLong(3, se.posId);
            insert.setInt(4, se.variant);
            insert.setLong(5, se.synsetId);
            insert.setInt(6, se.synset_position);
            insert.setLong(7, 1l);
            insert.executeUpdate();
        }
    }

    private void saveLocalisedString(List<LocalizedString> strings) throws SQLException {
        String INSERT_QUERY = "INSERT INTO wordnet.application_localised_string (id, value, language) VALUES(?, ?, ?)";
        PreparedStatement insert = connection.prepareStatement(INSERT_QUERY);
        for (LocalizedString s : strings) {
            insert.setLong(1, s.id);
            insert.setString(2, s.value);
            insert.setString(3, "en");
            insert.executeUpdate();
        }
        connection.commit();
    }

    private void saveRelType(List<RelTyp> relTyps) throws SQLException {
        String INSERT_QUERY = "INSERT INTO wordnet.relation_type (id, auto_reverse, display_text_id, name_id, description_id, relation_argument, short_display_text_id, color, node_position) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement insert = connection.prepareStatement(INSERT_QUERY);
        for (RelTyp rt : relTyps) {
            insert.setLong(1, rt.id);
            insert.setInt(2, rt.autoRevers);
            insert.setLong(3, rt.dispTextId);
            insert.setLong(4, rt.nameId);
            insert.setLong(5, rt.descId);
            insert.setString(6, rt.relationArg);
            insert.setLong(7, rt.shortDispId);
            insert.setString(8, rt.color);
            insert.setString(9, rt.node_position);
            insert.executeUpdate();
        }
        connection.commit();
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