package db.migration;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class V2_9__Add_Position_To_Relation_Type implements JdbcMigration {

    public void addPosition(Connection connection) throws SQLException {
        final String SYNSET_RELATION = "SYNSET_RELATION";
        final String SENSE_RELATION = "SENSE_RELATION";
        Map<Long, RelationType> synsetRelationTypes = getRelationTypes(connection, SYNSET_RELATION);
        Map<Long, RelationType> senseRelationTypes = getRelationTypes(connection, SENSE_RELATION);
        List<RelationType> positionedSynsetRelations = getPositionedRelationTypes(synsetRelationTypes);
        List<RelationType> positionedSenseRelations = getPositionedRelationTypes(senseRelationTypes);
        updateRelationsTypes(positionedSynsetRelations, connection);
        updateRelationsTypes(positionedSenseRelations, connection);
    }

    private Map<Long, RelationType> getRelationTypes(Connection connection, String relationType) throws SQLException {
        final Map<Long, RelationType> result = new LinkedHashMap<>();
        final String SELECT_STATEMENT = "SELECT id, parent_relation_type_id FROM relation_type WHERE relation_argument =? ORDER BY id";
        final int ID_POSITION = 1;
        final int PARENT_POSITION = 2;
        final PreparedStatement statement = connection.prepareStatement(SELECT_STATEMENT);
        statement.setString(1, relationType);
        final ResultSet resultSet = statement.executeQuery();
        Long id;
        Long parent_id;
        while (resultSet.next()) {
            id = resultSet.getLong(ID_POSITION);
            parent_id = resultSet.getLong(PARENT_POSITION);
            if (!resultSet.wasNull()) { //if parent_id != null
                result.get(parent_id).addChildren(id);
            } else if (!result.containsKey(id)) {
                result.put(id, new RelationType(id));
            }
        }
        return result;
    }

    private List<RelationType> getPositionedRelationTypes(Map<Long, RelationType> relationsTypes) {
        List<RelationType> resultRelationTypes = new ArrayList<>();
        int counter = 1;
        RelationType relationType;
        for (Map.Entry<Long, RelationType> entry : relationsTypes.entrySet()) {
            relationType = entry.getValue();
            relationType.setPosition(counter);
            counter++;
            resultRelationTypes.add(relationType);
            int childCounter = 1;
            for (RelationType child : relationType.getChildren()) {
                child.setPosition(childCounter);
                resultRelationTypes.add(child);
                childCounter++;
            }
        }
        return resultRelationTypes;
    }

    private void updateRelationsTypes(List<RelationType> relationTypes, Connection connection) throws SQLException {
        final String UPDATE_STATEMENT = "UPDATE relation_type SET priority=? WHERE id=?";
        final int POSITION_POSITION = 1;
        final int ID_POSITION = 2;
        final PreparedStatement updateStatement = connection.prepareStatement(UPDATE_STATEMENT);
        for (RelationType relationType : relationTypes) {
            updateStatement.setInt(POSITION_POSITION, relationType.getPosition());
            updateStatement.setLong(ID_POSITION, relationType.getId());
            updateStatement.executeUpdate();
        }
    }

    @Override
    public void migrate(Connection connection) throws Exception {
        addPosition(connection);
    }

    private class RelationType {
        private long id;
        private int position;
        private List<RelationType> children;

        public RelationType(long id) {
            this.id = id;
            children = new ArrayList<>();
        }

        public long getId() {
            return id;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public List<RelationType> getChildren() {
            return children;
        }

        void addChildren(long id) {
            children.add(new RelationType(id));
        }
    }
}
