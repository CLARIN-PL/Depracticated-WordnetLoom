package db.migration;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class V2_3__Adding_Missing_Attributes implements JdbcMigration {

    @Override
    public void migrate(Connection connection) throws Exception {
        final String SELECT_SYNSET_WITHOUT_ATTRIBUTES = "SELECT id FROM synset WHERE id NOT IN (SELECT synset_id FROM synset_attributes)";
        final String SELECT_SENSE_WITHOUT_ATTRIBUTES = "SELECT id FROM sense WHERE id NOT IN (SELECT sense_id FROM sense_attributes)";
        final String INSERT_SYNSET_ATTRIBUTE = "INSERT INTO synset_attributes (synset_id) VALUES (?)";
        final String INSERT_SENSE_ATTRIBUTE = "INSERT INTO sense_attributes (sense_id) VALUES (?)";

        insertMissingAttributes(connection, SELECT_SYNSET_WITHOUT_ATTRIBUTES, INSERT_SYNSET_ATTRIBUTE);
        insertMissingAttributes(connection, SELECT_SENSE_WITHOUT_ATTRIBUTES, INSERT_SENSE_ATTRIBUTE);
    }

    private List<Long> insertMissingAttributes(Connection connection, String selectQuery, String insertQuery) throws SQLException {
        List<Long> result = getMissingAttributeIds(connection, selectQuery);
        insertAttributes(connection, insertQuery, result);
        return result;
    }

    private List<Long> getMissingAttributeIds(Connection connection, String selectQuery) throws SQLException {
        Statement selectStatement = connection.createStatement();
        ResultSet resultSet = selectStatement.executeQuery(selectQuery);
        resultSet.last();
        resultSet.beforeFirst();
        List<Long> result = new ArrayList<>();
        while (resultSet.next()) {
            result.add(resultSet.getLong(1));
        }
        resultSet.close();
        return result;
    }

    private void insertAttributes(Connection connection, String insertQuery, List<Long> result) throws SQLException {
        PreparedStatement insertAttributeStatement = connection.prepareStatement(insertQuery);

        for (Long id : result) {
            insertAttributeStatement.setLong(1, id);
            insertAttributeStatement.addBatch();
        }
        insertAttributeStatement.executeBatch();
        insertAttributeStatement.close();
    }
}
