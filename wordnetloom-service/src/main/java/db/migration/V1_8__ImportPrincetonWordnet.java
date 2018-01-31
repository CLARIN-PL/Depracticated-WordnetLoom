package db.migration;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;
import pl.edu.pwr.wordnetloom.application.importers.lmf.LmfResourceImporter;
import pl.edu.pwr.wordnetloom.application.importers.lmf.model.LexicalResource;
import pl.edu.pwr.wordnetloom.application.importers.lmf.model.Lexicon;
import pl.edu.pwr.wordnetloom.synset.model.SynsetAttributes;
import pl.edu.pwr.wordnetloom.word.model.Word;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class V1_8__ImportPrincetonWordnet implements JdbcMigration {


    private Connection connection;


    @Override
    public void migrate(Connection connection) throws Exception {
        this.connection = connection;

        LmfResourceImporter importer = new LmfResourceImporter();
        LexicalResource lr = importer.getResourceFromFile("/opt/wn-eng-lmf.xml");
        Lexicon lexicon = lr.getLexicon().stream().findFirst().get();

        AtomicLong synsetId = new AtomicLong(1);
        AtomicLong senseId = new AtomicLong(1);
        AtomicLong wordId = new AtomicLong(1);
        AtomicLong loclaizedStringsId = new AtomicLong(7);
        AtomicLong relTypId = new AtomicLong(1);

        pl.edu.pwr.wordnetloom.lexicon.model.Lexicon l = new pl.edu.pwr.wordnetloom.lexicon.model.Lexicon();
        l.setId(1l);

        List<Relation> synsetRelations = new ArrayList<>();
        List<SynsetRelation> synsetRelationList = new ArrayList<>();

        Map<String, pl.edu.pwr.wordnetloom.synset.model.Synset> syn = new HashMap<>();

        List<SynsetAttributes> synsetAttributes = new ArrayList<>();

        List<Example> examples = new ArrayList<>();
        List<Sense> senses = new ArrayList<>();
        Set<Word> words = new HashSet<>();

        Set<String> relNames = new HashSet<>();
        Map<String, RelTyp> relTypes = new HashMap<>();

        List<LocalizedString> strings = new ArrayList<>();

        lexicon.getSynsets().forEach(s -> {

            pl.edu.pwr.wordnetloom.synset.model.Synset synset = new pl.edu.pwr.wordnetloom.synset.model.Synset();
            Long id = synsetId.getAndIncrement();
            synset.setId(id);
            synset.setLexicon(l);
            synset.setSplit(1);

            SynsetAttributes sa = new SynsetAttributes();
            sa.setId(id);
            sa.setPrincetonId(s.getId());
            sa.setDefinition(s.getDefinition() != null ? s.getDefinition().getGloss() : "");

            if (s.getDefinition() != null && s.getDefinition().getStatement() != null) {
                s.getDefinition().getStatement().forEach(e -> {
                    Example ex = new Example(id, e.getExample());
                    examples.add(ex);
                });
            }

            s.getSynsetRelations().getSynsetRelation().forEach(r -> {
                Relation rs = new Relation(id, r.getTargets(), r.getRelType());
                synsetRelations.add(rs);
                relNames.add(r.getRelType());
            });

            synsetAttributes.add(sa);
            syn.put(s.getId(), synset);

        });

        relNames.forEach(k -> {

            LocalizedString name = new LocalizedString(loclaizedStringsId.getAndIncrement(), k);
            LocalizedString shortDisp = new LocalizedString(loclaizedStringsId.getAndIncrement(), k);
            LocalizedString disp = new LocalizedString(loclaizedStringsId.getAndIncrement(), k);
            LocalizedString desc = new LocalizedString(loclaizedStringsId.getAndIncrement(), k);

            strings.add(name);
            strings.add(shortDisp);
            strings.add(disp);
            strings.add(desc);

            RelTyp rt = new RelTyp(relTypId.getAndIncrement(), name.id, desc.id, disp.id, shortDisp.id);
            relTypes.put(k, rt);
        });

        for (Relation sr : synsetRelations) {

            Long child = syn.get(sr.child).getId();
            Long relType = relTypes.get(sr.rel).id;
            SynsetRelation r = new SynsetRelation(sr.parent, child, relType);

            synsetRelationList.add(r);
        }

        saveSynsets(new ArrayList<>(syn.values()));
        saveSynsetAttributes(synsetAttributes);
        saveExample(examples);
        saveLocalisedString(strings);
        saveRelType(new ArrayList<>(relTypes.values()));
        saveAllowedLexicons(relTypes);
        saveSynsetRaltaion(synsetRelationList);

        lexicon.getLexicalEntries().forEach(entry -> {

            Long pos = getPos(entry.getLemma().getPartOfSpeech());
            String lemma = entry.getLemma().getWrittenForm();

            Word w = new Word();
            w.setId(wordId.getAndIncrement());
            w.setWord(lemma);

            words.add(w);

            entry.getSense().forEach(s -> {
                Sense sen = new Sense(senseId.getAndIncrement(), pos, w.getId(), syn.get(s.getSynset()).getId(), 0, 1);
                senses.add(sen);
            });

        });

        saveWord(words);
        saveSense(senses);
    }

    private Long getPos(String name) {
        switch (name) {
            case "n":
                return 2l;
            case "v":
                return 1l;
            case "a":
                return 4l;
            case "r":
                return 3l;
        }
        return 1l;
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
        String INSERT_QUERY = "INSERT INTO wordnet.sense (id, word_id, lexicon_id, part_of_speech_id, variant, synset_id, synset_position, domain_id) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement insert = connection.prepareStatement(INSERT_QUERY);

        for (Sense se : senses) {
            insert.setLong(1, se.id);
            insert.setLong(2, se.wordId);
            insert.setLong(3, se.lexiconId);
            insert.setLong(4, se.posId);
            insert.setInt(5, se.variant);
            insert.setLong(6, se.synsetId);
            insert.setInt(7, se.synset_position);
            insert.setLong(8, 1l);
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
        String INSERT_QUERY = "INSERT INTO wordnet.synset_attributes (synset_id, definition, princeton_id, abstract) VALUES(?, ?, ?, ?)";
        PreparedStatement insert = connection.prepareStatement(INSERT_QUERY);

        for (SynsetAttributes a : attributes) {
            insert.setLong(1, a.getId());
            insert.setString(2, a.getDefinition());
            insert.setString(3, a.getPrincetonId().replace("eng-10-", ""));
            insert.setBoolean(4, false);
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
        Long id;
        Long posId;
        Long wordId;
        Long synsetId;
        Long lexiconId = 1l;
        int synset_position = 0;
        int variant;

        public Sense(Long id, Long posId, Long wordId, Long synsetId, int synset_position, int variant) {
            this.id = id;
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