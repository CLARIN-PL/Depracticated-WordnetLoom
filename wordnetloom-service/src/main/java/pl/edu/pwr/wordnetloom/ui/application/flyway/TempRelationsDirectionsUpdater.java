package pl.edu.pwr.wordnetloom.ui.application.flyway;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class TempRelationsDirectionsUpdater {

    public static void run(final String relationsDisplayPath,Connection connection)
    {
        try {
            updateRelationsDirections(relationsDisplayPath,connection);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateRelationsDirections(String filepath,Connection connection) throws FileNotFoundException, SQLException {
        Scanner scanner = new Scanner(new FileReader(filepath));
        String line;
        int startIndex;
        String relations[];
        String direction;
        while(scanner.hasNext()){
            line = scanner.nextLine();
            startIndex = line.indexOf("=");
            direction = line.substring(0, startIndex);
            relations = line.substring(startIndex+1, line.length()).split(",");
            for(String relation : relations){
                Long id = getRelationUpdateId(relation, connection);
                if(id > 0){
                    updateRelationTypeDirection(id, direction, connection);
                } else {
                    System.out.println();
                }
            }
        }
    }

    private static Long getRelationUpdateId(final String name, Connection connection) throws SQLException {
        final String query = "SELECT R.id FROM wordnet.relation_type R JOIN wordnet.application_localised_string L ON R.name_id =L.id \n" +
                "WHERE value =? AND language = 'pl'";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, name);
        ResultSet resultSet = statement.executeQuery();
        if(resultSet.first()){
            return resultSet.getLong(1);
        }
        return 0L;
    }

    private  static void updateRelationTypeDirection(final Long id, final String direction, Connection connection) throws SQLException {
        final String query = "UPDATE wordnet.relation_type SET node_position = ? WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1,direction);
        statement.setLong(2, id);
        statement.executeUpdate();
    }
}
