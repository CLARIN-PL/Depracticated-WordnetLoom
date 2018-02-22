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
                String[] usages = usage.split("\\|\\|");

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

    private void saveWord(Set<Word> words) throws SQLException {
        String INSERT_QUERY = "INSERT INTO wordnet.word (id, word) VALUES(?, ?)";
        PreparedStatement insert = connection.prepareStatement(INSERT_QUERY);

        for (Word w : words) {
            insert.setLong(1, w.getId());
            insert.setString(2, w.getWord());
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
}