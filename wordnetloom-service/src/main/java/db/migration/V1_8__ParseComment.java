package db.migration;

import db.migration.commentParser.CommentParser;
import db.migration.commentParser.ParserResult;
import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class V1_8__ParseComment implements JdbcMigration {

    private final String ATTRIBUTE_TABLE = "sense_attributes";

    private CommentParser parser = new CommentParser();

    private final String UPDATE_QUERY = "UPDATE " + ATTRIBUTE_TABLE +
            " SET comment = ?, " +
            "definition = ?," +
            "link = ?," +
            "register_id=?, " +
            "proper_name=? " +
            "WHERE sense_id = ?";
    final String INSERT_QUERY = "INSERT INTO sense_examples (sense_attribute_id, type, example) VALUES (?,?,?)";
    private PreparedStatement updateAttributeStatement;
    private PreparedStatement insertExampleStatement;

    @Override
    public void migrate(Connection connection) throws SQLException {
        updateAttributeStatement = connection.prepareStatement(UPDATE_QUERY);
        insertExampleStatement = connection.prepareStatement(INSERT_QUERY);

        List<Attribute> attributes = getAttributesList(connection);
        List<ParserResult> results;
        Attribute fixedAttribute;
        for (Attribute attribute : attributes) {
            results = parser.parse(attribute.getComment());
            fixedAttribute = setAttributes(attribute, results, connection);
            updateAttributes(fixedAttribute, connection);
        }
    }

    private Attribute setAttributes(Attribute attribute, List<ParserResult> results, Connection connection) throws SQLException {
        StringBuilder unknown = new StringBuilder();
        attribute.setComment(null);
        for (ParserResult result : results) {
            switch (result.getType()) {
                case COMMENT:
                    attribute.setComment(result.getValue());
                    break;
                case REGISTER:
                    Long registerId = getRegisterID(result.getValue(), connection);
                    attribute.setRegister(registerId);
                    break;
                case LINK:
                    attribute.setLink(result.getValue());
                    break;
                case DEFINITION:
                    attribute.setDefinition(result.getValue());
                    break;
                case EXAMPLE:
                    attribute.addExample(new Example(result.getSecondValue(), result.getValue()));
                    break;
                case UNKNOWN:
                    unknown.append(result.getValue()).append(" ");
                    break;
            }
        }
        if (unknown.length() > 0) {
            attribute.setComment(unknown.toString()); //TODO zobaczyć, czy tutaj sie nic nie straci
        }
        return attribute;
    }


    private List<Attribute> getAttributesList(Connection connection) throws SQLException {
        if (connection == null) {
            return null;
        }
        int ID = 1;
        int COMMENT = 2;
//        String query = "SELECT sense_id, comment FROM " + ATTRIBUTE_TABLE;
//        Statement statement = connection.createStatement();
//        ResultSet resultSet = statement.executeQuery(query);
        ResultSet resultSet;
        List<Attribute> resultAttributes = new ArrayList<>();
        Attribute attribute;
        int offset = 0;
        do {
            resultSet = getAttributesResultSet(connection, 1000, offset);
            while (resultSet.next()) {
                attribute = new Attribute();
                attribute.setId(resultSet.getLong(ID));
                attribute.setComment(resultSet.getString(COMMENT));
                resultAttributes.add(attribute);
                offset++;
            }
        } while (resultSet.first());
//        List<Attribute> resultAttributes = new ArrayList<>();
//        Attribute attribute;

        return resultAttributes;
    }

    private ResultSet getAttributesResultSet(Connection connection, int limit, int offset) throws SQLException {
        String query = "SELECT sense_id, comment FROM " + ATTRIBUTE_TABLE + " LIMIT " + limit + " OFFSET " + offset;
        Statement statement = connection.createStatement();
        return statement.executeQuery(query);
    }

    public void updateAttributes(Attribute attribute, Connection connection) throws SQLException {
        final int COMMENT = 1;
        final int DEFINITION = 2;
        final int LINK = 3;
        final int REGISTER = 4;
        final int PROPER_NAME = 5;
        final int ATTRIBUTE_ID = 6;
        assert connection != null;
        updateAttributeStatement.clearParameters();
        setStringToStatement(COMMENT, attribute.getComment(), updateAttributeStatement);
        setStringToStatement(DEFINITION, attribute.getDefinition(), updateAttributeStatement);
        setStringToStatement(LINK, attribute.getLink(), updateAttributeStatement);
        setLongToStatement(REGISTER, attribute.getRegister(), updateAttributeStatement);
        setBooleanToStatement(PROPER_NAME, attribute.isProperName(), updateAttributeStatement);
        setLongToStatement(ATTRIBUTE_ID, attribute.getId(), updateAttributeStatement);
        updateAttributeStatement.executeUpdate();
        for (Example example : attribute.getExamples()) {
            insertExample(example, attribute.getId(), connection);
        }
    }

    private void setBooleanToStatement(int position, Boolean value, PreparedStatement statement) throws SQLException {
        if (value != null) {
            statement.setBoolean(position, value);
        } else {
            statement.setNull(position, Types.BIT);
        }
    }

    private void setStringToStatement(int position, String value, PreparedStatement statement) throws SQLException {
        if (value != null) {
            statement.setString(position, value);
        } else {
            statement.setNull(position, Types.VARCHAR);
        }
    }

    private void setLongToStatement(int position, Long value, PreparedStatement statement) throws SQLException {
        if (value != null) {
            statement.setLong(position, value);
        } else {
            statement.setNull(position, Types.INTEGER);
        }
    }

    private void insertExample(Example example, Long attributeId, Connection connection) throws SQLException {
        final int ATTRIBUTE_ID = 1;
        final int TYPE = 2;
        final int EXAMPLE = 3;

        insertExampleStatement.clearParameters();
        setLongToStatement(ATTRIBUTE_ID, attributeId, insertExampleStatement);
        setStringToStatement(TYPE, example.getType(), insertExampleStatement);
        setStringToStatement(EXAMPLE, example.getContent(), insertExampleStatement);
        insertExampleStatement.executeUpdate();
    }


    private Long getRegisterID(String registerName, Connection connection) throws SQLException {
        String GET_ID_QUERY = "SELECT D.id FROM dictionaries D LEFT JOIN application_localised_string S ON D.name_id = S.id WHERE S.value = ?";
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

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        if (!definition.isEmpty() && !definition.replaceAll("\\s+", "").equals(".")) {
            this.definition = definition;
        }
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<Example> getExamples() {
        return examples;
    }

    public Long getRegister() {
        return register;
    }

    public void setRegister(Long register) {
        if (register != -1) {
            this.register = register;
        }
    }

    boolean isProperName() {
        return properName;
    }

    void setProperName(boolean isProperName) {
        properName = isProperName;
    }

    void addExample(Example example) {
        examples.add(example);
    }
}

class Example {
    private String type;
    private String content;

    public Example(String type, String content) {
        this.type = type.trim();
        this.content = content.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}