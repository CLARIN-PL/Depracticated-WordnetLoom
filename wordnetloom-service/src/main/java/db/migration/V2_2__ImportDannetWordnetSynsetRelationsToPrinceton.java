package db.migration;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class V2_2__ImportDannetWordnetSynsetRelationsToPrinceton implements JdbcMigration {


    private Connection connection;

    @Override
    public void migrate(Connection connection) throws Exception {
        this.connection = connection;

        Map<String,String> mappings = new HashMap<>();
        String in = "/opt/ski-pwn-3.1.txt";

        try (Stream<String> stream = Files.lines(Paths.get(in))) {

            stream.forEach(line -> {
               String[] row = line.split(Pattern.quote("||"));
               String k = row[0];
               String v = row[1];
               mappings.put(k,v);
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Mappings loaded");

        List<LocalizedString> strings = new ArrayList<>();

        Map<String,Long> relations = new HashMap<>();
        relations.put("eq_synonym", 200l);
        relations.put("eq_has_hyponym", 201l);
        relations.put("eq_has_hyperonym", 202l);
        relations.put("eq_near_synonym", 203l);

        List<RelTyp> rel = getRelationTypes(connection, strings, relations);
        saveLocalisedString(strings);
        System.out.println("Saving localised Strings Done");
        saveRelType(rel);
        System.out.println("Saving Relation types Done");
        saveAllowedLexicons(rel, 1l);
        saveAllowedLexicons(rel, 2l);

        Set<SynsetRelation> sr = getRelations(relations, mappings);
        System.out.println("Loading Relations Done");
        saveSynsetRaltaion(sr);
    }

    private Set<SynsetRelation> getRelations(Map<String,Long> relations,  Map<String,String> mappings ) throws SQLException {
        Set<SynsetRelation> synsetRelations = new HashSet<>();
        String QUERY = "SELECT r.key as keyId, r.syn_set_id as synset, r.relation_type_name as rel FROM dannet.alignments r WHERE r.source_id = 'wordnet30'";
        PreparedStatement statement = connection.prepareStatement(QUERY);

        Map<String,Long> prince = findAllSynsetPrinceton();

        ResultSet rs = statement.executeQuery();
        while (rs.next()){
            String key = rs.getString("keyId");
            Long parent = new Long("888" +rs.getLong("synset"));
            Long rel = relations.get(rs.getString("rel"));

            String princeton = mappings.get(key);

            if(princeton != null) {
                Long child = prince.get(princeton);
                if(child != null){
                    SynsetRelation r = new SynsetRelation(parent, child, rel);
                    synsetRelations.add(r);
                }
            }
        }
        return synsetRelations;
    }

    private Map<String,Long> findAllSynsetPrinceton() throws SQLException {
        Map<String,Long> map = new HashMap<>();
        String QUERY = "SELECT sa.princeton_id as k, sa.synset_id as v FROM wordnet.synset_attributes sa where sa.princeton_id is not null";
        PreparedStatement statement = connection.prepareStatement(QUERY);
        ResultSet rs = statement.executeQuery();
        while(rs.next()){
            String key = rs.getString("k");
            Long val = rs.getLong("v");
            map.put(key,val);
        }
        return map;
    }

    private void saveRelType(List<RelTyp> relTyps) throws SQLException {
        String INSERT_QUERY = "INSERT INTO wordnet.relation_type (id, auto_reverse, display_text_id, name_id, description_id, relation_argument, short_display_text_id, color, node_position, multilingual) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
            insert.setBoolean(10, rt.multilingual);
            insert.executeUpdate();
        }
        connection.commit();
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
    private List<RelTyp> getRelationTypes(Connection connection, final List<LocalizedString> strings,  final Map<String,Long> relations) throws SQLException {

        String QUERY_COUNT = "SELECT count(s.id) as c FROM wordnet.application_localised_string s";

        PreparedStatement statement = connection.prepareStatement(QUERY_COUNT);
        List<RelTyp> rel = new ArrayList<>();

        ResultSet rs = statement.executeQuery();

        Integer count = 10;

        if(rs.first()){
            count = rs.getInt("c");
        }

        AtomicLong nextId = new AtomicLong(count+1);

        relations.forEach((r,v) -> {

            LocalizedString nameS = new LocalizedString(nextId.getAndIncrement(), r);
            LocalizedString shortDispS = new LocalizedString(nextId.getAndIncrement(), r);
            LocalizedString dispS = new LocalizedString(nextId.getAndIncrement(), r);
            LocalizedString descS = new LocalizedString(nextId.getAndIncrement(), r);

            strings.add(nameS);
            strings.add(shortDispS);
            strings.add(dispS);
            strings.add(descS);

            RelTyp rt = new RelTyp(v, nameS.id, descS.id, dispS.id, shortDispS.id);
            rel.add(rt);
        });

        return rel;
    }


    private void saveAllowedLexicons(List<RelTyp> relTypes, long lex) throws SQLException {
        String INSERT_QUERY = "INSERT INTO wordnet.relation_type_allowed_lexicons (relation_type_id, lexicon_id) VALUES(?, ?)";
        PreparedStatement insert = connection.prepareStatement(INSERT_QUERY);

        for (RelTyp rt : relTypes) {
            insert.setLong(1, rt.id);
            insert.setLong(2, lex);
            insert.executeUpdate();
        }
        connection.commit();
    }


    private void saveSynsetRaltaion(Set<SynsetRelation> relations) throws SQLException {
        String INSERT_QUERY = "INSERT INTO wordnet.synset_relation (parent_synset_id, child_synset_id, synset_relation_type_id) VALUES(?, ?, ?)";
        PreparedStatement insert = connection.prepareStatement(INSERT_QUERY);

        for (SynsetRelation r : relations) {
            insert.setLong(1, r.parent);
            insert.setLong(2, r.child);
            insert.setLong(3, r.rel);
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


    private class SynsetRelation {

        Long parent;
        Long child;
        Long rel;

        public SynsetRelation(Long parent, Long child, Long rel) {
            this.parent = parent;
            this.child = child;
            this.rel = rel;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SynsetRelation)) return false;

            SynsetRelation that = (SynsetRelation) o;

            if (parent != null ? !parent.equals(that.parent) : that.parent != null) return false;
            if (child != null ? !child.equals(that.child) : that.child != null) return false;
            return rel != null ? rel.equals(that.rel) : that.rel == null;
        }

        @Override
        public int hashCode() {
            int result = parent != null ? parent.hashCode() : 0;
            result = 31 * result + (child != null ? child.hashCode() : 0);
            result = 31 * result + (rel != null ? rel.hashCode() : 0);
            return result;
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
        String color = "#000000";  //TODO: Set defined colour for Equivalence Relations 
        String node_position = "RIGHT";
        Boolean multilingual = true;

        public RelTyp(Long id, Long nameId, Long descId, Long dispTextId, Long shortDispId) {
            this.id = id;
            this.nameId = nameId;
            this.descId = descId;
            this.shortDispId = shortDispId;
            this.dispTextId = dispTextId;
        }
    }
}