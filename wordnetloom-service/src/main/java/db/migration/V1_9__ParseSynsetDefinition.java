package db.migration;

import db.migration.commentParser.CommentParser;
import db.migration.commentParser.ParserResult;
import db.migration.commentParser.PrincetonDefinitionParser;
import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class V1_9__ParseSynsetDefinition implements JdbcMigration {

    private CommentParser parser = new CommentParser();
    private PrincetonDefinitionParser princetonParser = new PrincetonDefinitionParser();

    @Override
    public void migrate(Connection connection) throws SQLException {

        List<Attribute> attributes = getAttributesList(connection);

        Attribute fixedAttribute;

        for(Attribute attribute : attributes) {
            fixedAttribute = null;
            if(Objects.equals(attribute.getDefinition(), "brak danych")) {
                fixedAttribute = attribute;
                fixedAttribute.setDefinition(null);
            } else {
                if(attribute.isPrinceton()){
                    if(isInHashFormat(attribute.getDefinition())){
                        fixedAttribute = saveWithNormalFormat(attribute);
                    } else {
                        fixedAttribute = saveWithPrincetonFormat(attribute);
                    }
                } else {
                    if(isInHashFormat(attribute.getDefinition())){
                        fixedAttribute = saveWithNormalFormat(attribute);
                    }
                }
            }
            if(fixedAttribute != null){
                updateAttribute(fixedAttribute, connection);
            }
        }
    }

    private Attribute saveWithNormalFormat(Attribute attribute) {
        List<ParserResult> results = parser.parse(attribute.getDefinition());
        attribute.setDefinition(null);
        return setAttributes(attribute, results);
    }

    private Attribute saveWithPrincetonFormat(Attribute attribute) {
        List<ParserResult> results = princetonParser.parse(attribute.getDefinition());
        attribute.setDefinition(null);
        return setAttributes(attribute, results);
    }

    private Attribute setAttributes(Attribute attribute, List<ParserResult> results){
        for(ParserResult result : results){
            //TODO obsłużyć to
            switch (result.getType()){
                case DEFINITION:
                    attribute.setDefinition(result.getValue());
                    break;
                case EXAMPLE:
                    attribute.addExample(new Example(result.getValue(), result.getSecondValue()));
                    break;
                case UNKNOWN:
                    break;
                default:
            }
        }
        return attribute;
    }

    private void updateAttribute(Attribute attribute, Connection connection) throws SQLException {
        final int DEFINITION_INDEX = 1;
        final int ID_INDEX = 2;
        final String UPDATE_QUERY = "UPDATE synset_attributes SET definition = ? WHERE synset_id = ?";

        PreparedStatement updateAttributeStatement = connection.prepareStatement(UPDATE_QUERY);
        if(attribute.getDefinition() != null){
            updateAttributeStatement.setString(DEFINITION_INDEX, attribute.getDefinition().trim());
        } else {
            updateAttributeStatement.setNull(DEFINITION_INDEX, Types.VARCHAR);
        }
        updateAttributeStatement.setLong(ID_INDEX, attribute.getId());
        updateAttributeStatement.executeUpdate();

        for(Example example : attribute.getExamples()){
            saveExample(example,attribute.getId(), connection);
        }
    }

    private void saveExample(Example example, Long synsetId,  Connection connection) throws SQLException {
        final int ID_INDEX = 1;
        final int EXAMPLE_INDEX = 2;
        final int TYPE_INDEX = 3;
        final String INSERT_QUERY = "INSERT INTO synset_examples(synset_attributes_id, example,type)  VALUES(?,?,?)";

        PreparedStatement insertExampleStatement = connection.prepareStatement(INSERT_QUERY);
        insertExampleStatement.setLong(ID_INDEX, synsetId);
        insertExampleStatement.setString(EXAMPLE_INDEX, example.getExample());
        insertExampleStatement.setString(TYPE_INDEX, example.getType());

        insertExampleStatement.execute();

    }


    private List<Attribute> getAttributesList (Connection connection) throws SQLException {

        final String SELECT_ATTRIBUTES_QUERY = "SELECT A.synset_id, A.definition, S.lexicon_id " +
                "FROM wordnet.synset_attributes A " +
                "JOIN wordnet.synset S ON A.synset_id = S.id " +
                "WHERE A.definition IS NOT NULL AND A.definition != '' AND A.definition != 'brak danych'" ;

        final long PRINCETON_ID = 2L;

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SELECT_ATTRIBUTES_QUERY);
        List<Attribute> result = new ArrayList<>();
        Attribute attribute;

        while(resultSet.next()){
            attribute = new Attribute();
            attribute.setId(resultSet.getLong(1));
            attribute.setDefinition(resultSet.getString(2));
            attribute.setPrinceton(resultSet.getLong(3) == PRINCETON_ID);
            result.add(attribute);
        }
        return result;
    }

    private boolean isInHashFormat(String definition) {
        return definition.charAt(0) == '#' || definition.charAt(1) == '#';
    }

    private class Attribute {

        private long id;
        private String definition;
        private boolean princeton;
        private List<Example> examples;

        public Attribute(){
            examples = new ArrayList<>();
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getDefinition() {
            return definition;
        }

        public void setDefinition(String definition) {
            this.definition = definition;
        }

        public boolean isPrinceton() {
            return princeton;
        }

        public void setPrinceton(boolean princeton) {
            this.princeton = princeton;
        }

        public void addExample(Example example){
            examples.add(example);
        }

        public List<Example> getExamples(){
            return examples;
        }
    }

    private class Example {
        private Long id;

        private String example;

        private String type;

        public Example(final String example, final String type){
            this.example = example;
            this.type = type;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getExample() {
            return example;
        }

        public void setExample(String example) {
            this.example = example;
        }

        public String getType() {
            return type;
        }
        public void setType(String type) {
            this.type = type;
        }
    }
}
