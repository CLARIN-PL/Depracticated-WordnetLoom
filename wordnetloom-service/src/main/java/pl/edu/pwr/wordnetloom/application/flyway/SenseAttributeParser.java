package pl.edu.pwr.wordnetloom.application.flyway;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SenseAttributeParser {

    private final int NORMAL_MARKER = 0;
    private final int EXAMPLE_MARKER = 1;
    private final int LINK_MARKER = 2;
    private final int UNKNOWN_MARKER = 3;

    private int caretIndex;
    private int secondIndex;

    public void run(Connection connection) throws SQLException {
        List<Attribute> attributes = getAttributesList(connection);
        parse(attributes, connection);
        for (Attribute attribute : attributes) {

            System.out.println(attribute.getComment());
        }
    }

    public void saveAtrributes(final List<Attribute> attributes, Connection connection) throws SQLException {
        //TODO zrobić zapisywanie atrybutów
        final String UPDATE_QUERY = "UPDATE sense_atrributes " +
                "SET comment = ?, " +
                "definition = ?," +
                "link = ?," +
                "register=?" +
                "properName=?" +
                "WHERE sense_id = ?";

        PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY);
        for (Attribute attribute : attributes) {
            statement.setString(1, attribute.getComment());
            statement.setString(2, attribute.getDefinition());
            statement.setString(3, attribute.getLink());
            statement.setLong(4, attribute.getRegister());
            statement.setBoolean(5, attribute.isProperName());
            statement.setLong(6, attribute.getId());
        }
        statement.executeUpdate(); // TODO może być pobranie widoku
    }

    public List<Attribute> parse(List<Attribute> attributes, Connection connection) throws SQLException {
        final String REGISTER = "K";
        final String DEFINITION = "D";
        final String EXAMPLE = "P";
        String value;
        StringBuilder stringBuilder = new StringBuilder();
        int markerType;
        int startMarker;
        String marker;
        String comment;

        for (Attribute attribute : attributes) {
            comment = attribute.getComment().replace("\n", " "); // usuwanie znaku nowej lini
            startMarker = 0;
            caretIndex = 0;
            secondIndex = 0;

            while ((caretIndex = getNext(comment, caretIndex)) != -1) {

                if (secondIndex == 0 && caretIndex != 0) {
                    stringBuilder.append(comment.substring(secondIndex, caretIndex)).append(" ");
                }
                markerType = getMarkerType(comment, caretIndex);
                marker = getMarker(caretIndex + 1, comment);
                if (marker == null) {
                    try {
                        secondIndex = getNext(comment, caretIndex + 1);
                        if (secondIndex == -1) {
                            secondIndex = comment.length();
                        }
                        value = comment.substring(startMarker, secondIndex);
                        stringBuilder.append(value).append(" ");
//                        System.out.println(value);
                    } catch (Exception e) {
                        System.out.println();
                    }

                } else
                    switch (markerType) {
                        case NORMAL_MARKER:
                            if (marker.contains("A") || marker.equals("3")) {
                                secondIndex = getIndex("#", comment, caretIndex) - 1;
                            } else {
                                switch (marker) {
                                    case REGISTER:
                                        try {
                                            secondIndex = getNext(comment, caretIndex);
                                            if (secondIndex == -1) {
                                                secondIndex = comment.length();
                                            }
                                            value = comment.substring(caretIndex, secondIndex).replaceAll("\\s+", "");
                                            if (!value.isEmpty()) {
                                                Long registerId = getRegisterID(value, connection);
                                                if (registerId != -1) {
                                                    attribute.setRegister(registerId);
                                                }
                                            }
                                            secondIndex--;
                                            break;
                                        } catch (Exception e) {

                                            System.out.println();
                                        }

                                    case DEFINITION:
//                                    secondIndex = getIndex("#", comment, caretIndex );
//                                    secondIndex = getIndex(".", comment, caretIndex - 3);
                                        secondIndex = getNext(comment, caretIndex);
                                        if (secondIndex == -1) {
                                            secondIndex = comment.length();
                                        }
                                        value = comment.substring(caretIndex, secondIndex);
                                        if (!value.isEmpty() && !value.replaceAll("\\s+", "").equals(".")) //sprawdzenie czy definicja jest pusta oraz, czy nie składa się z samej kropki
                                        {
                                            attribute.setDefinition(value);
                                        }
                                        break;
                                    case EXAMPLE: //dodajemy w tym miejscu przykład, aby obsłużyć sytuacje w których przykład nie ma nawiasu otwierającego
                                        value = getExample(comment, caretIndex);
                                        if (!value.isEmpty() && !value.equals(" ")) {
                                            attribute.addExample(new Example(marker, value));
                                        }
                                        break;
                                    default:
                                        //TODO wyciągnąć to do jakieś metody
                                        secondIndex = getNext(comment, caretIndex);
                                        if (secondIndex == -1) {
                                            secondIndex = comment.length();
                                        }
                                        value = comment.substring(startMarker, secondIndex);
                                        stringBuilder.append(value).append(" ");
                                        //TODO dołączyć wartość do pozostałych komentarzy
                                }
                            }
                            break;
                        case EXAMPLE_MARKER:
                            value = getExample(comment, caretIndex);
                            if (!value.isEmpty() && !value.equals(" ")) {
                                attribute.addExample(new Example(marker, value));
                            }
                            break;
                        case LINK_MARKER:
                            try {
                                if (attribute.getId() == 26897) {
                                    System.out.println(attribute.getId());
                                }
                                secondIndex = getIndex("}", comment, caretIndex);
                                if (secondIndex > caretIndex) { // obsługa sytuacji niezamkniętego nawiasu na końcu
                                    value = comment.substring(caretIndex, secondIndex);
                                    if (!value.isEmpty() && !value.equals(" ")) {
                                        attribute.setLink(value);
                                    }
                                }
                            } catch (Exception e) {
                                System.out.println("Oj");
                            }
                            secondIndex++;
                            break;
                        case UNKNOWN_MARKER:
                            try {
                                secondIndex = getNext(comment, caretIndex);
                                if (secondIndex == -1) {
                                    secondIndex = comment.length();
                                }
                                value = comment.substring(startMarker, secondIndex);
                                stringBuilder.append(value).append(" ");
                            } catch (Exception e) {
                                System.out.println(e);
                            }
                            break;
                    }
                startMarker = secondIndex;
                caretIndex = secondIndex;
            }

            if (secondIndex < comment.length() - 1) {
                stringBuilder.append(comment.substring(secondIndex, comment.length()));
            }

            if (stringBuilder.length() > 0) {
                String remainingComment = stringBuilder.toString()
                        .replaceAll("^\\s+", "") //usunięcie spacji z lewej strony
                        .replace("\\s+$", ""); // usunięcie spacji z prawej strony
                if (!remainingComment.isEmpty()) {
                    System.out.println(remainingComment);
                    attribute.setComment(remainingComment);
                    //wprawdzenie, czy komentarz zawiera NP - nazwa wlasna
                    if (remainingComment.startsWith("NP")) {
                        attribute.setProperName(true);
                    }
                }
            }
            stringBuilder.setLength(0);
        }
        return attributes;
    }

    public int getMarkerType(String text, int markerPosition) {
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

    private int getNext(String text, int startIndex) {
        int index = startIndex;
        char currentChar = ' ';
        boolean foundFirst = false;
        while (index < text.length()) {
            try {
                currentChar = text.charAt(index);
            } catch (Exception e) {
                System.out.println();
            }

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
        try {
            secondIndex = getIndex("]", comment, caretIndex);
            String value = comment.substring(caretIndex, secondIndex);
            //TODO można dodać sprawdzanie czy przykład jest pusty
            secondIndex++;
            return value;
        } catch (Exception e) {
            System.out.println("Oj");
        }
        return null;
    }

    private int getIndex(String pattern, String comment, int startIndex) {
        int index = comment.indexOf(pattern, startIndex);
        if (index == -1) {
            index = comment.length();
        }
        return index;
    }

    private String getMarker(int startIndex, String comment) {
        final char[] delimiters = {':', ';', ' ', '>'};
        int index = startIndex;
        StringBuilder markerBuilder = new StringBuilder();
        char currentChar;
        while (index < comment.length()) {
            currentChar = comment.charAt(index);
            if (currentChar != '#') {
                for (char delimiter : delimiters) {
                    if (delimiter == currentChar) {
                        if (delimiter == ' ' && markerBuilder.length() == 0) { // sprawdzamy czy spacja poprzedza znacznik
                            continue;
                        } else {
                            caretIndex = index + 1;
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

    private Long getRegisterID(final String registerName, Connection connection) throws SQLException {
        final String GET_ID_QUERY = "SELECT R.id FROM register_types R LEFT JOIN localised L ON R.name_id = L.id " +
                "LEFT JOIN localised_strings S ON L.id = S.id WHERE S.strings = ?";
        PreparedStatement statement = connection.prepareStatement(GET_ID_QUERY);
        statement.setString(1, registerName);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.first()) {
            return resultSet.getLong(1);
        }
        return -1L;
    }

    private List<Attribute> getAttributesList(Connection connection) throws SQLException {
        final int ID = 1;
        final int COMMENT = 2;
        final String query = "SELECT sense_id, comment FROM sense_attributes";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        List<Attribute> resultAttributes = new ArrayList<>();
        if (resultSet.first()) {
            Attribute attribute;
            while (resultSet.next()) {
                attribute = new Attribute();
                attribute.setId(resultSet.getLong(ID));
                attribute.setComment(resultSet.getString(COMMENT));
                resultAttributes.add(attribute);
            }
        }
        return resultAttributes;
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

    public boolean isProperName() {
        return properName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    ;

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setRegister(Long register) {
        this.register = register;
    }

    public void addExample(Example example) {
        this.examples.add(example);
    }

    public void setProperName(boolean isProperName) {
        this.properName = isProperName;
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

    ;

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