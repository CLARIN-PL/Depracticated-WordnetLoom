package db.migration;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;
import pl.edu.pwr.wordnetloom.application.importers.lmf.LmfResourceImporter;
import pl.edu.pwr.wordnetloom.application.importers.lmf.model.LexicalResource;
import pl.edu.pwr.wordnetloom.application.importers.lmf.model.Lexicon;
import pl.edu.pwr.wordnetloom.synset.model.Synset;
import pl.edu.pwr.wordnetloom.synset.model.SynsetAttributes;
import pl.edu.pwr.wordnetloom.word.model.Word;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class V1_9__ImportDannetWordnetSynsets implements JdbcMigration {


    private Connection connection;


    @Override
    public void migrate(Connection connection) throws Exception {
        this.connection = connection;

        List<SynsetAttributes> synsetAttributes = new ArrayList<>();

        List<Example> examples = new ArrayList<>();

        saveSynsets(getSynsets(connection, examples, synsetAttributes));
        System.out.println("Saving synset done");
        saveSynsetAttributes(synsetAttributes);
        System.out.println("Saving synset attributes done");
        saveExample(examples);
        System.out.println("Saving synset examples done");

        saveWord(getWords(connection));
        System.out.println("Saving words done");

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

             Sense sen = new  Sense(pos, wid, new Long("888"+syn), 0, 1);
                senses.add(sen);

        }
        return senses;
    }

    private Set<Word> getWords(Connection connection) throws SQLException {
        String QUERY = "SELECT w.id as id, w.lemma as lemma FROM dannet.words w";
        PreparedStatement statement = connection.prepareStatement(QUERY);
        Set<Word> words = new HashSet<>();

        ResultSet rs = statement.executeQuery();
        while (rs.next()){

            Long wid = rs.getLong("id");
            String lemma = rs.getString("lemma");

            Word w = new Word();
            w.setId(wid);
            w.setWord(lemma);

            words.add(w);

        }
        return words;
    }

    private List<Synset> getSynsets(Connection connection, final List<Example> examples, List<SynsetAttributes> synsetAttributes) throws SQLException {

        String QUERY = "SELECT s.id as id, s.gloss as gloss, s.usage as usg FROM dannet.syn_sets s";
        PreparedStatement statement = connection.prepareStatement(QUERY);

        List<pl.edu.pwr.wordnetloom.synset.model.Synset> synsets = new ArrayList<>();

        pl.edu.pwr.wordnetloom.lexicon.model.Lexicon l = new pl.edu.pwr.wordnetloom.lexicon.model.Lexicon();
        l.setId(2l);

        ResultSet rs = statement.executeQuery();
        while (rs.next()){
            Long id = rs.getLong("id");
            String def = rs.getString("gloss");
            String usage = rs.getString("usg");

            pl.edu.pwr.wordnetloom.synset.model.Synset synset = new pl.edu.pwr.wordnetloom.synset.model.Synset();
            synset.setId(new Long("888"+id));
            synset.setLexicon(l);
            synset.setSplit(1);

            SynsetAttributes sa = new SynsetAttributes();
            sa.setId(new Long("888"+id));
            sa.setDefinition(def);

            if (usage != null) {
                String[] usages = usage.split("||");

                Arrays.asList(usages).forEach(e -> {
                    Example ex = new Example(new Long("888"+id), e);
                    examples.add(ex);
                });
            }

            synsetAttributes.add(sa);
            synsets.add(synset);
        }

        return synsets;
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

    private void saveSynsets(List<pl.edu.pwr.wordnetloom.synset.model.Synset> synsets) throws SQLException {
        String INSERT_QUERY = "INSERT INTO wordnet.synset (id, split, lexicon_id) VALUES(?, ?, ?)";
        PreparedStatement insert = connection.prepareStatement(INSERT_QUERY);

        for (pl.edu.pwr.wordnetloom.synset.model.Synset synset : synsets) {
            insert.setLong(1, synset.getId());
            insert.setInt(2, synset.getSplit());
            insert.setLong(3, synset.getLexicon().getId());
            insert.executeUpdate();
        }
    }

    private void saveExample(List<Example> examples) throws SQLException {
        String INSERT_QUERY = "INSERT INTO wordnet.synset_examples (synset_attributes_id, example) VALUES(?, ?)";
        PreparedStatement insert = connection.prepareStatement(INSERT_QUERY);

        for (Example e : examples) {
            insert.setLong(1, e.id);
            insert.setString(2, e.example);
            insert.executeUpdate();
        }
    }

    private void saveAllowedLexicons(Map<String, RelTyp> relTypes) throws SQLException {
        String INSERT_QUERY = "INSERT INTO wordnet.relation_type_allowed_lexicons (relation_type_id, lexicon_id) VALUES(?, ?)";
        PreparedStatement insert = connection.prepareStatement(INSERT_QUERY);

        for (RelTyp rt : relTypes.values()) {
            insert.setLong(1, rt.id);
            insert.setLong(2, 1l);
            insert.executeUpdate();
        }
    }


    private void saveWord(Set<Word> words) throws SQLException {
        String INSERT_QUERY = "INSERT INTO wordnet.word (id, word) VALUES(?, ?)";
        PreparedStatement insert = connection.prepareStatement(INSERT_QUERY);

        for (Word w : words) {
            insert.setLong(1, w.getId());
            insert.setString(2, w.getWord());
            insert.executeUpdate();
        }
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

    private void saveSynsetAttributes(List<SynsetAttributes> attributes) throws SQLException {
        String INSERT_QUERY = "INSERT INTO wordnet.synset_attributes (synset_id, definition, abstract) VALUES(?, ?, ?)";
        PreparedStatement insert = connection.prepareStatement(INSERT_QUERY);

        for (SynsetAttributes a : attributes) {
            insert.setLong(1, a.getId());
            insert.setString(2, a.getDefinition());
            insert.setBoolean(3, false);
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
    }

    private class Example {
        Long id;
        String example;

        public Example(Long id, String example) {
            this.id = id;
            this.example = example;
        }
    }

    private class Allowed {
        Long id;
        Long value;

        public Allowed(Long id, Long value) {
            this.id = id;
            this.value = value;
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
        Long lexiconId = 1l;
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
        String color = "#000000";
        String node_position = "LEFT";

        public RelTyp(Long id, Long nameId, Long descId, Long dispTextId, Long shortDispId) {
            this.id = id;
            this.nameId = nameId;
            this.descId = descId;
            this.shortDispId = shortDispId;
            this.dispTextId = dispTextId;
        }
    }
}