package db.migration;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

import java.sql.*;
import java.util.*;

public class V3_7__Move_Emotions_And_Valuations implements JdbcMigration {
    private final String INPUT_EMOTIONAL_ANNOTATION_TABLE = "wordnet_work.emotion";
    private final String OUTPUT_EMOTIONAL_ANNOTATION_TABLE = "wordnet.emotional_annotations";
    private final String UNIT_EMOTIONS_TABLE = "wordnet.unit_emotions";
    private final String UNIT_VALUATIONS_TABLE = "wordnet.unit_valuations";
    private final String DICTIONARIES_TABLE = "wordnet.dictionaries";
    private final String LOCALISED_STRING_TABLE = "wordnet.application_localised_string";
    private final String USERS_TABLE = "wordnet.users";
    private final String EMOTION_TYPE = "Emotion";
    private final String VALUATION_TYPE = "Valuation";
    private final String SPLIT_REGEX = "[\\;\\.\\:]";

    private final String M_PLUS = "+m";
    private final String M_MINUS = "-m";
    private final String S_PLUS = "+s";
    private final String S_MINUS = "-s";
    private final String AMB = "amb";
    private final String EMPTY = "";

    private final String ANNOTATION_TABLE = "wordnet.emotional_annotations";
    private final String MARKEDNESS = "Markedness";

    @Override
    public void migrate(Connection connection) throws Exception {
        connection.setAutoCommit(false);
        List<Annotation> annotations = getAnnotations(connection);
        Map<String, Element> emotions = getEmotions(connection);
        Map<String, Element> valuation = getValuations(connection);
        Map<String, User> users = getUsers(connection);

        List<Annotation> filledAnnotations = setIds(annotations, emotions, valuation, users);
        save(connection, filledAnnotations);

        Map<String, Long> dictonaries = getDictionariesMarkednes(connection);
        List<Markedness> markednesses = getAnnotationsMarkedness(connection);
        save(markednesses, dictonaries, connection);

        connection.commit();
    }

    private Map<String, Long> getDictionariesMarkednes(Connection connection) throws SQLException {
        final String SELECT_STATEMENT = "SELECT DISTINCT D.id, S.value FROM " + DICTIONARIES_TABLE + " D JOIN " + LOCALISED_STRING_TABLE
                + " S ON D.name_id = S.id WHERE dtype = ?";
        final int ID = 1;
        final int VALUE = 2;
        PreparedStatement statement = connection.prepareStatement(SELECT_STATEMENT);
        statement.setString(1, MARKEDNESS);
        ResultSet resultSet = statement.executeQuery();
        Map<String, Long> markedness = new HashMap<>();
        while(resultSet.next()){
            Long id = resultSet.getLong(ID);
            String value = resultSet.getString(VALUE);
            String mark = getMarkedness(value);

            markedness.put(mark, id);
        }
        return markedness;
    }

    private String getMarkedness(String value) {
        final int FIRST_CHAR = 0;
        final int SECOND_CHAR = 1;
        value = value.replaceAll("\\s", "");
        char firstChar = value.charAt(FIRST_CHAR);
        if(firstChar == '+'){
            if(value.charAt(SECOND_CHAR) == 'm'){
                return M_PLUS;
            } else {
                return S_PLUS;
            }
        } else if(firstChar == '-') {
            if(value.charAt(SECOND_CHAR) == 'm'){
                return M_MINUS;
            } else {
                return S_MINUS;
            }
        } else if (firstChar == 'a'){
            return AMB;
        } else {
            return null;
        }
    }

    private List<Markedness> getAnnotationsMarkedness(Connection connection) throws SQLException {
        final String SELECT_STATEMENT = "SELECT id, markedness FROM " + OUTPUT_EMOTIONAL_ANNOTATION_TABLE;
        final int ID = 1;
        final int MARKEDNESS = 2;
        List<Markedness> markedness = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SELECT_STATEMENT);
        Markedness mark;
        while(resultSet.next()){
            mark = new Markedness();
            mark.setId(resultSet.getLong(ID));
            mark.setMarkedness(resultSet.getString(MARKEDNESS));

            markedness.add(mark);
        }
        return markedness;
    }

    private void save(List<Markedness> markednesses, Map<String, Long> dictionary, Connection connection) throws SQLException {
        final String UPDATE_STATEMENT = "UPDATE " + OUTPUT_EMOTIONAL_ANNOTATION_TABLE + " SET markedness_id=? WHERE id=?";
        final int MARKEDNESS = 1;
        final int ID = 2;
        PreparedStatement statement = connection.prepareStatement(UPDATE_STATEMENT);
        for(Markedness markedness : markednesses){
            if(markedness.getMarkedness() != null){
                Long dictionaryId = dictionary.get(markedness.getMarkedness());
                statement.setLong(MARKEDNESS, dictionaryId);
                statement.setLong(ID, markedness.getId());
                statement.executeUpdate();
            }
        }
    }

    private class Markedness {

        private Long id;
        private String markedness;
        private Long markednessId;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getMarkedness() {
            return markedness;
        }

        public void setMarkedness(String markedness) {
            if(markedness == null || markedness.isEmpty()){
                this.markedness = null;
            } else {
                this.markedness = markedness.replaceAll("\\s", "");
            }
        }

        public Long getMarkednessId() {
            return markednessId;
        }

        public void setMarkednessId(Long markednessId) {
            this.markednessId = markednessId;
        }
    }

    private Map<String, User> getUsers(Connection connection) throws SQLException {
        final String SELECT_QUERY = "SELECT id, firstname, lastname FROM " + USERS_TABLE;
        final int ID = 1;
        final int FIRSTNAME = 2;
        final int LASTNAME = 3;
        Map<String, User> resultMap = new HashMap<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SELECT_QUERY);
        User user;
        while (resultSet.next()) {
            user = new User();
            user.setId(resultSet.getLong(ID));
            user.setFirstName(resultSet.getString(FIRSTNAME));
            user.setLastName(resultSet.getString(LASTNAME));

            resultMap.put(user.getName(), user);
        }

        return resultMap;
    }

    private List<Annotation> getAnnotations(Connection connection) throws SQLException {
        final String SELECT_QUERY = "SELECT DISTINCT id, unitStatus, emotions, valuations, owner FROM " + INPUT_EMOTIONAL_ANNOTATION_TABLE;
        final int ID = 1;
        final int HAS_EMOTIONAL = 2;
        final int EMOTIONS = 3;
        final int VALUATIONS = 4;
        final int OWNER = 5;
        List<Annotation> resultList = new ArrayList<>();
        Statement selectStatement = connection.createStatement();
        ResultSet resultSet = selectStatement.executeQuery(SELECT_QUERY);
        Annotation annotation;
        while (resultSet.next()) {
            annotation = new Annotation();
            annotation.setId(resultSet.getLong(ID));
            annotation.setEmotionalCharacteristic(resultSet.getBoolean(HAS_EMOTIONAL));
            annotation.setEmotions(resultSet.getString(EMOTIONS));
            annotation.setValuations(resultSet.getString(VALUATIONS));
            annotation.setUserName(resultSet.getString(OWNER));
            resultList.add(annotation);
        }
        resultSet.close();
        return resultList;
    }

    private Map<String, Element> getEmotions(Connection connection) throws SQLException {
        return getElements(connection, EMOTION_TYPE);
    }

    private Map<String, Element> getValuations(Connection connection) throws SQLException {
        return getElements(connection, VALUATION_TYPE);
    }

    private Map<String, Element> getElements(Connection connection, String type) throws SQLException {
        final String SELECT_QUERY =
                "SELECT D.id, name_id, S.value FROM " + DICTIONARIES_TABLE + " D JOIN " + LOCALISED_STRING_TABLE + " S ON D.name_id = S.id AND S.language='pl' " +
                        "WHERE dtype=?";
        final int ID = 1;
        final int NAME = 2;
        final int VALUE = 3;
        Map<String, Element> resultMap = new HashMap<>();
        PreparedStatement statement = connection.prepareStatement(SELECT_QUERY);
        statement.setString(1, type);
        ResultSet resultSet = statement.executeQuery();
        Element element;
        while (resultSet.next()) {
            element = new Element();
            element.setId(resultSet.getLong(ID));
            element.setName(resultSet.getString(VALUE));
            element.setNameId(resultSet.getLong(NAME));

            resultMap.put(element.getName(), element);
        }
        resultSet.close();
        return resultMap;
    }

    private void save(Connection connection, List<Annotation> annotations) throws SQLException {
        System.out.println("Saving");
        final String INSERT_EMOTIONS_STATEMENT = "INSERT INTO " + UNIT_EMOTIONS_TABLE + "(annotation_id, emotion) VALUES(?,?)";
        final String INSERT_VALUATIONS_STATEMENT = "INSERT INTO " + UNIT_VALUATIONS_TABLE + "(annotation_id, valuation) VALUES(?,?)";
        final String UPDATE_USER_STATEMENT = "UPDATE " + OUTPUT_EMOTIONAL_ANNOTATION_TABLE + " SET owner=? WHERE id = ?";
        PreparedStatement emotionsStatement = connection.prepareStatement(INSERT_EMOTIONS_STATEMENT);
        PreparedStatement valuationsStatement = connection.prepareStatement(INSERT_VALUATIONS_STATEMENT);
        PreparedStatement usersStatement = connection.prepareStatement(UPDATE_USER_STATEMENT);
        for (Annotation annotation : annotations) {
            save(annotation.getEmotionsIds(), annotation.getId(), emotionsStatement);
            save(annotation.getValuationsIds(), annotation.getId(), valuationsStatement);
            saveUser(annotation.getId(), annotation.getUser(), usersStatement);
        }

        emotionsStatement.close();
        valuationsStatement.close();
        usersStatement.close();
    }

    private void saveUser(Long annotationId, Long userId, PreparedStatement statement) throws SQLException {
        assert userId != null;
        statement.setLong(1, userId);
        statement.setLong(2, annotationId);
        statement.executeUpdate();
    }

    private void save(Set<Long> ids, Long annotationId, PreparedStatement statement) throws SQLException {
        if (ids.isEmpty()) {
            return;
        }
        for (Long id : ids) {
            statement.setLong(1, annotationId);
            statement.setLong(2, id);
            statement.executeUpdate();
        }
    }

    private List<Annotation> setIds(List<Annotation> annotations, Map<String, Element> emotionsMap
            , Map<String, Element> valuationsMap, Map<String, User> users) {
        for (Annotation annotation : annotations) {
            setUsers(annotation, users);
            if (annotation.isEmotionalCharacteristic()) {
                setEmotions(annotation, emotionsMap, valuationsMap);
                setValuations(annotation, emotionsMap, valuationsMap);
            }
        }
        return annotations;
    }

    private void setUsers(Annotation annotation, Map<String, User> usersMap) {
        if (annotation.getUserName() == null || annotation.getUserName().isEmpty()) {
            return;
        }
        String userName = annotation.getUserName();
        User user = usersMap.get(userName);
        if (user != null) {
            annotation.setUser(user.getId());
        } else {
            String mostSimilar = getMostSimilar(annotation.getUserName(), usersMap.keySet());
            user = usersMap.get(mostSimilar);
            if (user != null) {
                annotation.setUser(user.getId());
            } else {
                throw new RuntimeException("Not found user");
            }
        }
    }

    private void setEmotions(Annotation annotation, Map<String, Element> emotionsMap, Map<String, Element> valuationMap) {
        if (annotation.getEmotions() == null || annotation.getEmotions().isEmpty()) {
            return;
        }

        String[] emotions = annotation.getEmotions().split(SPLIT_REGEX);
        for (String emotion : emotions) {
            if (isIncorrectValue(emotion)) {
                continue;
            }
            Long id = getElementId(emotion, emotionsMap);
            if (id != null) {
                annotation.getEmotionsIds().add(id);
            } else {
                id = getElementId(emotion, valuationMap);
                if (id != null) {
                    annotation.getValuationsIds().add(id);
                } else {
                    throw new RuntimeException("Not found emotion");
                }
            }
        }

    }

    private Long getElementId(String elementName, Map<String, Element> elementMap) {
        Element element = elementMap.get(elementName);
        if (element != null) {
            return element.getId();
        } else {
            String mostiSimilar = getMostSimilar(elementName, elementMap.keySet());
            if (mostiSimilar != null) {
                element = elementMap.get(mostiSimilar);
                return element.getId();
            }
        }
        return null;
    }

    private boolean isIncorrectValue(String value) {
        final List<String> incorrectValues = Arrays.asList("błąd", "-", "");
        return incorrectValues.contains(value);

    }

    private void setValuations(Annotation annotation, Map<String, Element> emotionMap, Map<String, Element> valuationsMap) {
        if (annotation.getValuations() == null || annotation.getValuations().isEmpty()) {
            return;
        }

        String[] valuations = annotation.getValuations().split(SPLIT_REGEX);
        for (String valuation : valuations) {
            if (isIncorrectValue(valuation)) {
                continue;
            }

            if (valuation.equals("szczęście użyteczność")) {
                annotation.getValuationsIds().add(getElementId("szczęście", valuationsMap));
                annotation.getValuationsIds().add(getElementId("użyteczność", valuationsMap));
                continue;
            }

            Long id = getElementId(valuation, valuationsMap);
            if (id != null) {
                annotation.getValuationsIds().add(id);
            } else {
                id = getElementId(valuation, emotionMap);
                if (id != null) {
                    annotation.getEmotionsIds().add(id);
                } else {
                    throw new RuntimeException("Not found valuation");
                }
            }

        }

    }

    private String getMostSimilar(String name, Set<String> elements) {
        final double SIMILARITY_THRESHOLD = 0.4;

        double bestResult = 0.0;
        String bestWord = null;

        for (String element : elements) {
            double result = calculateSimilarity(name, element);
            if (result > bestResult) {
                bestResult = result;
                bestWord = element;
            }
        }
        if (bestResult > SIMILARITY_THRESHOLD) {
            return bestWord;
        }

        switch (name) {
            case "zaskoczenie":
                return "zaskoczenie czymś nieprzewidywanym";
            case "dobro drugiego człowieka":
                return "dobro";
            default:
                return null;
        }
    }


    private double calculateSimilarity(String word1, String word2) {
        String longerWord = word1;
        String shorterWord = word2;
        if (word1.length() < word2.length()) {
            longerWord = word2;
            shorterWord = word1;
        }
        int longerLength = longerWord.length();
        if (longerLength == 0) {
            return 1.0;
        }

        return (longerLength - editDistance(longerWord, shorterWord)) / (double) longerLength;
    }

    private int editDistance(String word1, String word2) {
        word1 = word1.toLowerCase();
        word2 = word2.toLowerCase();

        int[] costs = new int[word2.length() + 1];
        for (int i = 0; i < word1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= word2.length(); j++) {
                if (i == 0) {
                    costs[j] = j;
                } else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (word1.charAt(i - 1) != word2.charAt(j - 1)) {
                            newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
                        }
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0) {
                costs[word2.length()] = lastValue;
            }
        }
        return costs[word2.length()];
    }

    private class Element {
        private Long id;
        private String name;
        private Long nameId;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {

            this.name = name.replaceAll("^\\s+", "");
            ;
        }

        public Long getNameId() {
            return nameId;
        }

        void setNameId(Long nameId) {
            this.nameId = nameId;
        }
    }

    private class User {
        private Long id;
        private String firstName;
        private String lastName;

        public String getName() {
            if (lastName != null && !lastName.isEmpty()) {
                return firstName + "." + lastName;
            }
            return firstName;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getFirstName() {
            return firstName;
        }

        void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        String getLastName() {
            return lastName;
        }

        void setLastName(String lastName) {
            this.lastName = lastName;
        }
    }

    private class Annotation {
        private Long id;
        private String emotions;
        private Set<Long> emotionsIds;
        private String valuations;
        private Set<Long> valuationsIds;
        private boolean isEmotionalCharacteristic;
        private String userName;
        private Long user;

        Annotation() {
//            emotionsIds = new ArrayList<>();
//            valuationsIds = new ArrayList<>();
            emotionsIds = new LinkedHashSet<>();
            valuationsIds = new LinkedHashSet<>();
        }

        String getUserName() {
            return userName;
        }

        void setUserName(String userName) {
            this.userName = userName;
        }

        public Long getUser() {
            return user;
        }

        public void setUser(Long user) {
            this.user = user;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        String getEmotions() {
            return emotions;
        }

        void setEmotions(String emotions) {
            this.emotions = emotions;
        }

        Set<Long> getEmotionsIds() {
            return emotionsIds;
        }

        public void setEmotionsIds(Set<Long> emotionsIds) {
            this.emotionsIds = emotionsIds;
        }

        String getValuations() {
            return valuations;
        }

        void setValuations(String valuations) {
            this.valuations = valuations;
        }

        Set<Long> getValuationsIds() {
            return valuationsIds;
        }

        public void setValuationsIds(Set<Long> valuationsIds) {
            this.valuationsIds = valuationsIds;
        }

        boolean isEmotionalCharacteristic() {
            return isEmotionalCharacteristic;
        }

        void setEmotionalCharacteristic(boolean emotionalCharacteristic) {
            isEmotionalCharacteristic = emotionalCharacteristic;
        }
    }
}
