package db.migration;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class V1_8__ParseComment implements JdbcMigration {

    private final String ATTRIBUTE_TABLE = "wordnet.sense_attributes";

    private final int NORMAL_MARKER = 0;
    private final int EXAMPLE_MARKER = 1;
    private final int LINK_MARKER = 2;
    private final int UNKNOWN_MARKER = 3;

    private int currentPosition;
    private int secondIndex;

    private Connection connection;

    @Override
    public void migrate(Connection connection) throws Exception {
        this.connection = connection;
        System.out.println("start parser");
        System.out.println("getAttributesList()");
        List<Attribute> attributes = getAttributesList();
        if (attributes == null) {
            return;
        }
        System.out.println("parse()");
        List<Attribute> parsedAttribute = parse(attributes);
        System.out.println("saveAttributes()");
        saveAttributes(parsedAttribute);
    }

    private List<Attribute> getAttributesList() throws SQLException {
        if (connection == null) {
            return null;
        }
        int ID = 1;
        int COMMENT = 2;
        String query = "SELECT sense_id, comment FROM " + ATTRIBUTE_TABLE;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        List<Attribute> resultAttributes = new ArrayList<>();
        Attribute attribute;
        while (resultSet.next()) {
            attribute = new Attribute();
            attribute.setId(resultSet.getLong(ID));
            attribute.setComment(resultSet.getString(COMMENT));
            resultAttributes.add(attribute);
        }
        return resultAttributes;
    }

    public void saveAttributes(List<Attribute> attributes) throws SQLException {
        if (connection == null) {
            return;
        }

        String UPDATE_QUERY = "UPDATE " + ATTRIBUTE_TABLE +
                " SET comment = ?, " +
                "definition = ?," +
                "link = ?," +
                "register_id=?, " +
                "proper_name=? " +
                "WHERE sense_id = ?";

        String DELETE_QUERY = "DELETE FROM " + ATTRIBUTE_TABLE + " WHERE sense_id = ?";
        String INSERT_EXAMPLE_QUERY = "INSERT INTO wordnet.sense_examples (sense_attribute_id, type, example) VALUES(?, ?, ?)";
        PreparedStatement updateStatement = connection.prepareStatement(UPDATE_QUERY);
        PreparedStatement deleteStatement = connection.prepareStatement(DELETE_QUERY);
        PreparedStatement insertExampleStatement = connection.prepareStatement(INSERT_EXAMPLE_QUERY);
        for (Attribute attribute : attributes) {
            // sprawdzamy, czy atrybut posiada jakiekolwiek informacje. Jeżeli nie, usuwamy go
            if (attribute.getComment() == null && attribute.getDefinition() == null && attribute.getLink() == null && attribute.getRegister() == null && (attribute.getExamples() == null || attribute.getExamples().isEmpty())) {
                deleteStatement.setLong(1, attribute.getId());
                deleteStatement.executeUpdate();

            } else { // aktualizacja komentarza
                updateStatement.setString(1, attribute.getComment());
                updateStatement.setString(2, attribute.getDefinition());
                updateStatement.setString(3, attribute.getLink());
                if (attribute.getRegister() != null) {
                    updateStatement.setLong(4, attribute.getRegister());
                } else {
                    updateStatement.setNull(4, Types.INTEGER);
                }
                updateStatement.setBoolean(5, attribute.isProperName());
                updateStatement.setLong(6, attribute.getId());
                updateStatement.executeUpdate();
            }

            for (Example example : attribute.getExamples()) {
                insertExampleStatement.setLong(1, attribute.getId());
                insertExampleStatement.setString(2, example.getType());
                insertExampleStatement.setString(3, example.getContent());
                insertExampleStatement.executeUpdate();
            }
        }
    }

    private void serveNormalMarker(String marker, String comment, int currentIndex, Attribute attributeRef, int startMarker, StringBuilder commentBuilderRef) throws SQLException {
        final String REGISTER = "K";
        final String DEFINITION = "D";
        final String EXAMPLE = "P";

        String value;
        if (marker.contains("A") || marker.equals("3")) {
            secondIndex = getIndex("#", comment, currentPosition) - 1;
        } else {
            switch (marker) {
                case REGISTER:
                    value = getRegister(comment, currentPosition);
                    if (!value.isEmpty()) {
                        Long registerId = getRegisterID(value, connection);
                        attributeRef.setRegister(registerId);
                    }
                    break;
                case DEFINITION:
                    value = getDefinition(comment, currentPosition);
                    attributeRef.setDefinition(value.trim());
                    break;
                case EXAMPLE: //dodajemy w tym miejscu przykład, aby obsłużyć sytuacje w których przykład nie ma nawiasu otwierającego
                    serveExampleMarker(marker, comment, attributeRef);
                    break;
                default:
                    serveUnknownMarker(comment, currentIndex, startMarker, commentBuilderRef);
            }
        }
    }

    private void serveExampleMarker(String marker, String comment, Attribute attributeRef) {
        String value;
        value = getExample(comment, currentPosition);
        if (!value.isEmpty() && !value.equals(" ")) {
            attributeRef.addExample(new Example(marker, value.trim()));
        }
    }

    private void saveLinkMarker(String marker, String comment, Attribute attributeRef) {
        String value;
        secondIndex = getIndex("}", comment, currentPosition);
        if (secondIndex > currentPosition) { // obsługa sytuacji niezamkniętego nawiasu na końcu lini
            value = comment.substring(currentPosition, secondIndex).replaceAll("^\\s+", ""); // usunięcie spacji z lewej strony
            if (!value.isEmpty() && !value.equals(" ")) {
                // sprawdzamy, czy wartość rzeczywiście jest linkiem. Jeżeli nie jest prawdopodobnie jest to przykład
                if (value.startsWith("http") || value.startsWith("www") || value.startsWith("pl")) {
                    attributeRef.setLink(value.trim());
                    secondIndex++;
                } else {
                    serveExampleMarker(marker, comment, attributeRef);
                }
            } else {
                secondIndex++;
            }
        }
    }

    private void serveUnknownMarker(String comment, int currentIndex, int startMarker, StringBuilder commentBuilderRef) {
        String value = getUnknown(comment, currentIndex, startMarker);
        commentBuilderRef.append(value).append(" ");
    }

    private List<Attribute> parse(List<Attribute> attributes) throws SQLException {
        if (connection == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        int markerType;
        int startMarker;
        String marker;
        String comment;
        for (Attribute attribute : attributes) {
            comment = attribute.getComment().replace("\n", " "); // usuwanie znaku nowej lini
            if (comment == null) {
                continue;
            }
            // resetowanie liczników
            startMarker = 0;
            currentPosition = 0;
            secondIndex = 0;
            while ((currentPosition = getNext(comment, currentPosition)) != -1) {
                if (startMarker == 0 && currentPosition != 0) { // sprawdzamy czy nie ma nic przed pierwszym znacznikiem
                    stringBuilder.append(comment.substring(0, currentPosition)).append(" ");
                    startMarker = currentPosition;
                }
                markerType = getMarkerType(comment, currentPosition);
                marker = getMarker(currentPosition + 1, comment);
                if (marker == null) {
                    serveUnknownMarker(comment, currentPosition, startMarker, stringBuilder);
                    secondIndex++;
                } else
                    switch (markerType) {
                        case NORMAL_MARKER:
                            serveNormalMarker(marker, comment, currentPosition, attribute, startMarker, stringBuilder);
                            break;
                        case EXAMPLE_MARKER:
                            serveExampleMarker(marker, comment, attribute);
                            break;
                        case LINK_MARKER:
                            saveLinkMarker(marker, comment, attribute);
                            break;
                        case UNKNOWN_MARKER:
                            serveUnknownMarker(comment, currentPosition, startMarker, stringBuilder);
                            break;
                    }
                startMarker = secondIndex;
                currentPosition = secondIndex;
            }

            if (secondIndex < comment.length() - 1) {  // sprawdzamy, czy zostało coś niewyorzystanego na końcu lini
                stringBuilder.append(comment.substring(secondIndex, comment.length()));
            }

            String remainingComment = stringBuilder.toString()
                    .replaceAll("^\\s+", "") //usunięcie spacji z lewej strony
                    .replace("\\s+$", ""); // usunięcie spacji z prawej strony
            if (!remainingComment.isEmpty()) {
                attribute.setComment(remainingComment);
                //wprawdzenie, czy komentarz zawiera NP - nazwa wlasna
                if (remainingComment.startsWith("NP")) {
                    attribute.setProperName(true);
                }
            } else {
                attribute.setComment(null);
            }
            stringBuilder.setLength(0);
        }
        return attributes;
    }

    private String getUnknown(String comment, int startIndex, int startMarkerIndex) {
        secondIndex = getNext(comment, startIndex);
        if (secondIndex == -1) {
            secondIndex = comment.length();
        }
        return comment.substring(startMarkerIndex, secondIndex);
    }

    private String getRegister(String comment, int startIndex) {
        secondIndex = getNext(comment, startIndex);
        if (secondIndex == -1) {
            secondIndex = comment.length();
        }
        String register = comment.substring(startIndex, secondIndex).replaceAll("\\s+", "");
        secondIndex--;
        return register;
    }

    private String getDefinition(String comment, int startIndex) {
        secondIndex = getNext(comment, startIndex);
        if (secondIndex == -1) {
            secondIndex = comment.length();
        }
        return comment.substring(startIndex, secondIndex);
    }

    int getMarkerType(String text, int markerPosition) {
        switch (text.charAt(markerPosition)) {
            case '#':
                return NORMAL_MARKER;
            case '[':
                return EXAMPLE_MARKER;
            case '{':
                return LINK_MARKER;
            case '<':
                return UNKNOWN_MARKER;
            default:
                return -1;
        }
    }

    // szuka rozpoczęcia następnego elementu
    private int getNext(String text, int startIndex) {
        int index = startIndex;
        char currentChar = ' ';
        boolean foundFirst = false;
        while (index < text.length()) {
            currentChar = text.charAt(index);

            switch (currentChar) {
                case '#':
                    if (foundFirst) {
                        return index - 1; // zwracamy indeks do początku znacznika, czyli do pierwszego znalezionego znaku
                    }
                case '{':
                case '[':
                case '<':
                    foundFirst = true;
                    break;
                default:
                    foundFirst = false;

            }
            index++;
        }
        return -1;
    }

    private String getExample(String comment, int startExampleIndex) {
        secondIndex = getIndex("]", comment, startExampleIndex);
        String value = comment.substring(startExampleIndex, secondIndex);
        secondIndex++;
        return value;
    }

    private int getIndex(String pattern, String comment, int startIndex) {
        int index = comment.indexOf(pattern, startIndex);
        if (index == -1) {
            index = comment.length();
        }
        return index;
    }

    private String getMarker(int startIndex, String comment) {
        char[] delimiters = {':', ';', ' ', '>'};
        int index = startIndex;
        StringBuilder markerBuilder = new StringBuilder();
        char currentChar;
        while (index < comment.length()) {
            currentChar = comment.charAt(index);
            if (currentChar != '#') {
                for (char delimiter : delimiters) {
                    if (delimiter == currentChar) {
                        if (delimiter != ' ' || markerBuilder.length() != 0) { // sprawdzamy czy spacja poprzedza znacznik
                            currentPosition = index + 1;
                            return markerBuilder.toString();
                        }
                    }
                }
                if (currentChar != ' ')
                    markerBuilder.append(currentChar);
            }
            index++;
        }
        return null;
    }

    private Long getRegisterID(String registerName, Connection connection) throws SQLException {
//        String GET_ID_QUERY = "SELECT R.id FROM wordnet.register_types R LEFT JOIN wordnet.application_localised_string S ON R.name_id = S.id WHERE S.value = ?";
        String GET_ID_QUERY = "SELECT D.id FROM wordnet.dictionaries D LEFT JOIN wordnet.application_localised_string S On D.name_id = S.id WHERE S.value = ?";
        PreparedStatement statement = connection.prepareStatement(GET_ID_QUERY);
        statement.setString(1, registerName);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.first()) {
            return resultSet.getLong(1);
        }
        return -1L;
    }
}

class Attribute {
    private Long id;
    private String comment;
    private String definition;
    private String link;
    private List<Example> examples;
    private Long register;
    private boolean properName;

    public Attribute() {
        examples = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public String getComment() {
        return comment;
    }

    public String getDefinition() {
        return definition;
    }

    public String getLink() {
        return link;
    }

    public List<Example> getExamples() {
        return examples;
    }

    public Long getRegister() {
        return register;
    }

    boolean isProperName() {
        return properName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setDefinition(String definition) {
        if (!definition.isEmpty() && !definition.replaceAll("\\s+", "").equals(".")) {
            this.definition = definition;
        }
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setRegister(Long register) {
        if (register != -1) {
            this.register = register;
        }
    }

    void addExample(Example example) {
        examples.add(example);
    }

    void setProperName(boolean isProperName) {
        properName = isProperName;
    }
}

class Example {
    private String type;
    private String content;

    public Example(String type, String content) {
        this.type = type;
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setContent(String content) {
        this.content = content;
    }
}