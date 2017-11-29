package pl.edu.pwr.wordnetloom.ui.application.flyway;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TempRelationsDirectionsUpdater {

    public static void run(final String relationsDisplayPath,Connection connection)
    {
        try {
//            updateRelationsDirections(relationsDisplayPath,connection);
            saveUpdateQuert(relationsDisplayPath, connection);
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
                    int result = updateRelationTypeDirection(id, direction, connection);
                    System.out.println(result);
                } else {
                    System.out.println();
                }
            }
        }
    }

    private static void saveUpdateQuert(String filepath, Connection connection) throws FileNotFoundException, SQLException{
        Scanner scanner = new Scanner(new FileReader(filepath));
        String line;
        int startIndex;
        String relations[];
        String direction;
        StringBuilder stringBuilder = new StringBuilder();
        while(scanner.hasNext()){
            line = scanner.nextLine();
            startIndex = line.indexOf("=");
            direction = line.substring(0, startIndex);
            relations = line.substring(startIndex+1, line.length()).split(",");
            List<Long> idsList = new ArrayList<>();
            stringBuilder.append("UPDATE wordnet.relation_type SET node_position = ");
            for(String relation : relations){
                Long id = getRelationUpdateId(relation, connection);
                idsList.add(id);
            }
            stringBuilder.append("'").append(direction).append("'");
            stringBuilder.append( " WHERE id IN(");
            for(int i =0; i< idsList.size(); i++){
                stringBuilder.append(idsList.get(i));
                if(i != idsList.size()-1){
                    stringBuilder.append(",");
                }
            }
            stringBuilder.append(" )");
            stringBuilder.append(" OR parent_relation_type_id IN (");
            for(int i =0; i< idsList.size(); i++){
                stringBuilder.append(idsList.get(i));
                if(i!=idsList.size()-1){
                    stringBuilder.append(",");
                }
            }
            stringBuilder.append( ");\n");
        }
        String query = stringBuilder.toString();
        System.out.println(query);
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

    private  static int updateRelationTypeDirection(final Long id, final String direction, Connection connection) throws SQLException {
        final String query = "UPDATE wordnet.relation_type SET node_position = ? WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1,direction);
        statement.setLong(2, id);
        return statement.executeUpdate();
    }
}
