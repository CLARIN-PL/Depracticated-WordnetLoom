package db.migration;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class V1_9__SetRelationTypeAllowedPartsOfSpeech implements JdbcMigration {

    @Override
    public void migrate(Connection connection) throws Exception {
/*        List<RelationType> relationTypes = getRelationType(connection);
        List<AllowedPartOfSpeech> allowedPartsOfSpeech = parse(relationTypes);
        save(allowedPartsOfSpeech, connection);*/
    }

    private List<RelationType> getRelationType(Connection connection) throws SQLException {
        // IN w zapytaniu ma pozbyć się typów relacji, które nie były przerzucone ze startej bazy
        final String SELECT_QUERY = "SELECT id, CASE WHEN PARENT_ID IS NULL THEN posstr ELSE (SELECT posstr FROM wordnet_work.relationtype WHERE id = R.PARENT_ID) END\n" +
                "posstr FROM wordnet_work.relationtype R WHERE id IN (SELECT id FROM wordnet.relation_type)\n" +
                "ORDER BY id";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SELECT_QUERY);
        List<RelationType> relationTypes = new ArrayList<>();
        long id;
        String partsOfSpeech;
        while (resultSet.next()) {
            id = resultSet.getLong(1);
            partsOfSpeech = resultSet.getString(2);
            relationTypes.add(new RelationType(id, partsOfSpeech));
        }
        return relationTypes;
    }

    private List<AllowedPartOfSpeech> parse(List<RelationType> relationTypes) {
        final String NOUN = "rzeczownik";
        final String VERB = "czasownik";
        final String ADJECTIVE = "przymiotnik";
        final String ADVERB = "przysłówek";

        final int NOUN_ID = 1;
        final int VERB_ID = 2;
        final int ADVERB_ID = 3;
        final int ADJECTIVE_ID = 4;

        List<AllowedPartOfSpeech> allowedPartOfSpeeches = new ArrayList<>();

        String[] partsElements;
        for (RelationType relationType : relationTypes) {
            partsElements = relationType.getPartsOfSpeech().split(",");
            for (String partOfSpeech : partsElements) {
                switch (partOfSpeech.trim()) {
                    case NOUN:
                        allowedPartOfSpeeches.add(new AllowedPartOfSpeech(relationType.getId(), NOUN_ID));
                        break;
                    case VERB:
                        allowedPartOfSpeeches.add(new AllowedPartOfSpeech(relationType.getId(), VERB_ID));
                        break;
                    case ADVERB:
                        allowedPartOfSpeeches.add(new AllowedPartOfSpeech(relationType.getId(), ADVERB_ID));
                        break;
                    case ADJECTIVE:
                        allowedPartOfSpeeches.add(new AllowedPartOfSpeech(relationType.getId(), ADJECTIVE_ID));
                        break;
                }
            }
        }
        return allowedPartOfSpeeches;
    }

    private void save(List<AllowedPartOfSpeech> allowedPartsOfSpeech, Connection connection) throws SQLException {
        if (connection == null) {
            return;
        }
        final String INSERT_QUERY = "INSERT INTO wordnet.relation_type_allowed_parts_of_speech (relation_type_id, part_of_speech_id) " +
                "VALUES (?,?)";
        PreparedStatement statement = connection.prepareStatement(INSERT_QUERY);
        for (AllowedPartOfSpeech partOfSpeech : allowedPartsOfSpeech) {
            statement.setLong(1, partOfSpeech.getRelationTypeId());
            statement.setLong(2, partOfSpeech.getPartOfSpeechId());
            statement.executeUpdate();
        }
    }

    private class RelationType {
        private long id;
        private String partsOfSpeech;

        public RelationType(long id, String partsOfSpeech) {
            this.id = id;
            this.partsOfSpeech = partsOfSpeech;
        }

        long getId() {
            return id;
        }

        String getPartsOfSpeech() {
            return partsOfSpeech;
        }
    }

    private class AllowedPartOfSpeech {
        private long relationTypeId;
        private long partOfSpeechId;

        AllowedPartOfSpeech(long relationTypeId, long partOfSpeechId) {
            this.relationTypeId = relationTypeId;
            this.partOfSpeechId = partOfSpeechId;
        }

        long getRelationTypeId() {
            return relationTypeId;
        }

        long getPartOfSpeechId() {
            return partOfSpeechId;
        }
    }
}
