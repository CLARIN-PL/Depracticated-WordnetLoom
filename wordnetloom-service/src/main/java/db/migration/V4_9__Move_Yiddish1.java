package db.migration;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

import java.sql.*;
import java.text.MessageFormat;
import java.util.*;

public class V4_9__Move_Yiddish1 implements JdbcMigration {

    @Override
    public void migrate(Connection connection) throws SQLException {
        moveWords(connection);
        moveDomains(connection);
        movePartsOfSpeech(connection);
    }

    private void moveWords(Connection connection) throws SQLException {
        System.out.println("moveWords");
        Map<String, Word> existingWords = getExistingWords(connection);
        Map<String, Word> newWords = getNewWords(connection); // TODO rename
        List<Word> toSave = filterWords(existingWords, newWords);
        toSave = generateUuids(toSave);
        saveWords(toSave, connection);
    }

    private Map<String, Word> getNewWords(Connection connection) throws SQLException {
        return getWords(connection, "plwordnet3-prod");
    }

    private Map<String, Word> getExistingWords(Connection connection) throws SQLException {
        return getWords(connection, null);
    }

    private List<Word> filterWords(Map<String, Word> existingWords, Map<String,Word> newWords){
        List<Word> result = new ArrayList<>(); // TODO move to method
        newWords.forEach((lemma, word) -> {
            if (!existingWords.containsKey(lemma)) {
                result.add(word);
            }
        });
        return result;
    }

    private List<Word> generateUuids(List<Word> words){
        words.forEach(word -> word.setUuid(new UUID(8,8)));
        return words;
    }

    private Map<String, Word> getWords(Connection connection, String database) throws SQLException {
        final String SELECT_QUERY_PATTERN = "SELECT id, word FROM {0}word";
        final String SELECT_QUERY = MessageFormat.format(SELECT_QUERY_PATTERN, database != null ? "`" + database + "`." : "");

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SELECT_QUERY);
        Map<String, Word> result = new HashMap<>();
        Word word;
        while (resultSet.next()) {
            word = new Word();
            word.setId(resultSet.getLong(1));
            word.setWord(resultSet.getString(2));
            result.put(word.getWord(), word);
        }

        return result;
    }


    private void saveWords(List<Word> words, Connection connection) throws SQLException {
        // TODO pamiętać, aby wcześniej  dodać funkcję do bazy danych
        final String INSERT_QUERY = "INSERT INTO word (id, uuid, word) VALUES(?,UUID_TO_BIN(UUID()),?)";
        final int BATCH_SIZE = 5000;
        int count = 0;
        PreparedStatement statement = connection.prepareStatement(INSERT_QUERY);
        for (Word word : words) {
            statement.setLong(1, word.getId());
            statement.setString(2, word.getWord());
            statement.addBatch();
            if(count >= BATCH_SIZE){
                statement.executeBatch();
                statement.clearBatch();
                count = 0;
                System.out.println("Wstawiono " + BATCH_SIZE);
            }
            count++;
        }
        statement.executeBatch();
    }

    private void saveWords(List<Word> words,int startIndex, int endIndex, Connection connection) throws SQLException {
        // TODO pamiętać, aby wcześniej  dodać funkcję do bazy danych
        final String INSERT_QUERY = "INSERT INTO word (id, uuid, word) VALUES(?,UUID_TO_BIN(UUID()),?)";
        PreparedStatement statement = connection.prepareStatement(INSERT_QUERY);
        for (int i = startIndex; i < endIndex; i++) {
            Word word = words.get(i);
            statement.setLong(1, word.getId());
            statement.setString(2, word.getWord());
            statement.addBatch();
        }
        statement.executeBatch();
    }

    private void moveDomains(Connection connection) throws SQLException {
        System.out.println("moveDomains");
        List<Domain> newDomains = getWordnetDomain(connection);
        repairDomainValues(newDomains);
        Map<String, Long> localisedStrings = getLocalisedStrings(connection);
        Map<String, Long> extendedLocalisedStrings = insertDomainValuesToLocalisedString(newDomains, localisedStrings, connection);
        Map<String, Domain> wordnetDomains = getWordnetDomains(connection);
        Map<Long, Long> domainsMapping = insertAndMapDomains(newDomains, wordnetDomains, extendedLocalisedStrings, connection);
        createTempDomainsTable(connection);
        insertDomainsMapping(domainsMapping, connection);
    }

    private void movePartsOfSpeech(Connection connection) throws SQLException {
        System.out.println("movePartsOfSpeech");
        List<PartOfSpeech> newPartsOfSpeech = getPartOfSpeech(connection);
        Map<String, PartOfSpeech> existingPartsOfSpeech = getExistingPartOfSpeech(connection);
        Map<String, Long> localisedStrings = getLocalisedStrings(connection);
        Map<String, Long> extendedLocalisedStrings = insertPartOfSpeechLocalisedString(newPartsOfSpeech, localisedStrings, connection);
        insertPartOfSpeech(newPartsOfSpeech, existingPartsOfSpeech, extendedLocalisedStrings, connection);
    }

    // TODO sprawdzić, czy to nie powinno czegoś zwracać
    private void insertPartOfSpeech(List<PartOfSpeech> newPartOfSpeech, Map<String, PartOfSpeech> existingPartsOfSpeech, Map<String, Long> localisedStrings, Connection connection) throws SQLException {
        final String INSERT_QUERY = "INSERT INTO part_of_speech (name_id) VALUES (?)";
        PreparedStatement statement = connection.prepareStatement(INSERT_QUERY);
        for (PartOfSpeech partOfSpeech : newPartOfSpeech) {
            if (!existingPartsOfSpeech.containsKey(partOfSpeech.getName())) {
                Long nameId = localisedStrings.get(partOfSpeech.getName());
                statement.setLong(1, nameId);
                statement.execute();

                existingPartsOfSpeech.put(partOfSpeech.getName(), partOfSpeech);
            }
        }
    }

    private Map<String, Long> insertPartOfSpeechLocalisedString(List<PartOfSpeech> partsOfSpeech, Map<String, Long> localisedStrings, Connection connection) throws SQLException {
        final String INSERT_QUERY = "INSERT INTO application_localised_string(value, language) VALUES (?,?)";
        PreparedStatement statement = connection.prepareStatement(INSERT_QUERY, PreparedStatement.RETURN_GENERATED_KEYS);
        statement.setString(2, "pl");
        Map<String, Long> resultMap = localisedStrings;
        for (PartOfSpeech partOfSpeech : partsOfSpeech) {
            if(partOfSpeech.getName().equals("pronoun")){
                System.out.println();
            }
            if (!localisedStrings.containsKey(partOfSpeech.getName())) {
                statement.setString(1, partOfSpeech.getName());
                statement.executeUpdate();
                resultMap.put(partOfSpeech.getName(), getGeneratedKey(statement));
            }
        }
        return resultMap;
    }

    private Long getGeneratedKey(PreparedStatement statement) throws SQLException {
        ResultSet generatedKey = statement.getGeneratedKeys();
        generatedKey.next();
        Long id = generatedKey.getLong(1);
        generatedKey.close();
        return id;
    }

    private List<PartOfSpeech> getPartOfSpeech(Connection connection) throws SQLException {
        final String SELECT_QUERY = "SELECT id, uby_lmf_type FROM `plwordnet3-prod`.part_of_speech";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SELECT_QUERY);
        List<PartOfSpeech> result = new ArrayList<>();
        PartOfSpeech partOfSpeech;
        while (resultSet.next()) {
            partOfSpeech = new PartOfSpeech();
            partOfSpeech.setId(resultSet.getLong(1));
            partOfSpeech.setName(resultSet.getString(2));

            result.add(partOfSpeech);
        }
        return result;
    }

    private Map<String, PartOfSpeech> getExistingPartOfSpeech(Connection connection) throws SQLException {
        final String SELECT_QUERY = "SELECT P.id, P.name_id, A.value FROM part_of_speech P JOIN application_localised_string A ON A.language='en' AND P.name_id = A.id";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SELECT_QUERY);
        Map<String, PartOfSpeech> partOfSpeechMap = new HashMap<>();
        PartOfSpeech partOfSpeech;
        while (resultSet.next()) {
            partOfSpeech = new PartOfSpeech();
            partOfSpeech.setId(resultSet.getLong(1));
            partOfSpeech.setNameId(resultSet.getLong(2));
            partOfSpeech.setName(resultSet.getString(3));

            partOfSpeechMap.put(partOfSpeech.getName(), partOfSpeech);
        }
        return partOfSpeechMap;
    }


    private Map<String, Long> getLocalisedStrings(Connection connection) throws SQLException {
        final String SELECT_QUERY = "SELECT id, value FROM application_localised_string WHERE language = 'pl'";
        Map<String, Long> result = new HashMap<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SELECT_QUERY);
        while (resultSet.next()) {
            Long id = resultSet.getLong(1);
            String value = resultSet.getString(2);
            result.put(value, id);
        }
        return result;
    }

    private Map<Long, Long> insertAndMapDomains(List<Domain> newDomains, Map<String, Domain> allDomains, Map<String, Long> localisedString, Connection connection) throws SQLException {
        final String INSERT_QUERY = "INSERT INTO domain (description_id, name_id) VALUES (?,?)";

        PreparedStatement statement = connection.prepareStatement(INSERT_QUERY, PreparedStatement.RETURN_GENERATED_KEYS);
        Map<Long, Long> result = new HashMap<>();
        Long id;
        for (Domain domain : newDomains) {
            if (!allDomains.containsKey(domain.getName())) {
                Long description = localisedString.get(domain.getDescription());
                Long name = localisedString.get(domain.getName());
                statement.setLong(1, description);
                statement.setLong(2, name);
                statement.executeUpdate();

                id = getGeneratedKey(statement);
            } else {
                id = allDomains.get(domain.getName()).getId();
            }
            result.put(domain.getId(), id);
        }
        return result;

    }

    private void createTempDomainsTable(Connection connection) throws SQLException {
        final String CREATE_QUERY = "CREATE TABLE temp_domains (" +
                "newId INT NOT NULL," +
                "oldId INT NOT NULL " +
                ")";
        Statement statement = connection.createStatement();
        statement.execute(CREATE_QUERY);
    }

    private void insertDomainsMapping(Map<Long, Long> domainsMapping, Connection connection) throws SQLException {
        final String INSERT_QUERY = "INSERT INTO temp_domains (newId, oldId) VALUES (?,?)";
        PreparedStatement statement = connection.prepareStatement(INSERT_QUERY);
        domainsMapping.forEach((key, value) -> {
            try {
                statement.setLong(1, key);
                statement.setLong(2, value);
                statement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private Map<Long, Long> getDomainsMapping(List<Domain> newDomains, Map<String, Domain> wordnetDomains) {
        Map<Long, Long> result = new HashMap<>();
        for (Domain domain : newDomains) {
            Domain wordnetDomain = wordnetDomains.get(domain.getName());
            if (wordnetDomain != null) {
                result.put(domain.getId(), wordnetDomain.getId());
            }

        }
        return result;
    }

    private Map<String, Domain> getWordnetDomains(Connection connection) throws SQLException {
        final String SELECT_QUERY = "SELECT D.id, D.description_id, D.name_id, A1.value, A2.value FROM wordnet.domain D LEFT JOIN wordnet.application_localised_string A1 ON D.description_id = A1.id AND A1.language='pl' LEFT JOIN wordnet.application_localised_string A2 ON D.name_id = A2.id AND A2.language = 'pl'";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SELECT_QUERY);
        Map<String, Domain> result = new HashMap<>();
        Domain domain;
        while (resultSet.next()) {
            domain = new Domain();
            domain.setId(resultSet.getLong(1));
            domain.setDescriptionID(resultSet.getLong(2));
            domain.setNameID(resultSet.getLong(3));
            domain.setDescription(resultSet.getString(4));
            domain.setName(resultSet.getString(5));

            result.put(domain.getName(), domain);
        }
        return result;
    }

    private Map<String, Long> insertDomainValuesToLocalisedString(List<Domain> domainsToSave, Map<String, Long> localisedStrings, Connection connection) throws SQLException {
        final String INSERT_QUERY = "INSERT IGNORE INTO application_localised_string (value, language) VALUES (?, ?)";
        PreparedStatement statement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS);
        statement.setString(2, "pl");
        Map<String, Long> resultMap = localisedStrings;
        for (Domain domain : domainsToSave) {
            // insert name
            Long id = tryInsertLocalisedString(domain.getName(), resultMap, statement);
            if (id != null) {
                resultMap.put(domain.getName(), id);
            }
            // insert description
            id = tryInsertLocalisedString(domain.getDescription(), resultMap, statement);
            if (id != null) {
                resultMap.put(domain.getDescription(), id);
            }
        }
        return resultMap;
    }

    private Long tryInsertLocalisedString(String value, Map<String, Long> localisedString, PreparedStatement statement) throws SQLException {
        if (!localisedString.containsKey(value)) {
            statement.setString(1, value);
            statement.execute();
            return getGeneratedKey(statement);
        }
        return null;
    }

    private void repairDomainValues(List<Domain> domains) {
        for (Domain domain : domains) {
            String name = getRepairedDomainName(domain.getName());
            String desctription = getRepairedDomainDescription(domain.getDescription());
            domain.setName(name);
            domain.setDescription(desctription);
        }
    }

    private String getRepairedDomainName(String domainName) {
        if (domainName.startsWith("plWN_")) {
            return domainName.substring(5);
        }
        if (domainName.startsWith("PWN_")) {
            return domainName.substring(4);
        }
        return domainName;
    }

    private String getRepairedDomainDescription(String domainDescription) {
        if (domainDescription.startsWith("plWN")) {
            return domainDescription.substring(5);
        }
        if (domainDescription.startsWith("PWN")) {
            String description = domainDescription.substring(4);
            if (description.startsWith(":")) {
                description = description.substring(2);
            }
            return description;
        }
        return domainDescription;
    }

    private List<Domain> getWordnetDomain(Connection connection) throws SQLException {
        final String SELECT_QUERY = "SELECT D.id, description, name, T1.text, T2.text " +
                "FROM `plwordnet3-prod`.domain D " +
                "LEFT JOIN `plwordnet3-prod`.text T1 ON D.description = T1.id " +
                "LEFT JOIN `plwordnet3-prod`.text T2 ON D.name = T2.id";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SELECT_QUERY);
        List<Domain> resultList = new ArrayList<>();
        Domain domain;
        while (resultSet.next()) {
            domain = new Domain();
            domain.setId(resultSet.getLong(1));
            domain.setDescriptionID(resultSet.getLong(2));
            domain.setNameID(resultSet.getLong(3));
            domain.setDescription(resultSet.getString(4));
            domain.setName(resultSet.getString(5));
            resultList.add(domain);
        }
        return resultList;
    }

    private class Word {

        private Long id;
        //        private byte[] uuid;
        private UUID uuid;
        private String word;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public UUID getUuid() {
            return uuid;
        }

        public void setUuid(UUID uuid) {
            this.uuid = uuid;
        }

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            // set with removing space at the end
            this.word = word.replaceAll("\\s+$", "");
        }
    }

    private class Domain {
        private Long id;
        private Long description_id;
        private Long name_id;
        private String description;
        private String name;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getDescriptionID() {
            return description_id;
        }

        public void setDescriptionID(Long description_id) {
            this.description_id = description_id;
        }

        public Long getName_id() {
            return name_id;
        }

        public void setNameID(Long name_id) {
            this.name_id = name_id;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    private class PartOfSpeech {

        private Long id;
        private Long nameId;
        private String name;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getNameId() {
            return nameId;
        }

        public void setNameId(Long nameId) {
            this.nameId = nameId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
