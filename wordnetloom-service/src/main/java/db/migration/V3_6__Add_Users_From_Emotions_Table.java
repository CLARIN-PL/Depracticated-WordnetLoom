package db.migration;

import org.flywaydb.core.api.migration.jdbc.JdbcMigration;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class V3_6__Add_Users_From_Emotions_Table implements JdbcMigration {

    private final String USERS_TABLE = "users";
    private final String EMOTION_TABLE = "wordnet_work.emotion";

    @Override
    public void migrate(Connection connection) throws Exception {
        List<User> users = getUsers(connection);
        setFirstCapitalLetterAndSave(connection, users);

        List<String> usersFromEmotionTable = getUsersFromEmotionTable(connection);
        List<User> newUsers = getUsers(usersFromEmotionTable);
        saveNewUsers(connection, newUsers);
    }

    private List<User> getUsers(Connection connection) throws SQLException {
        final String SELECT_STATEMENT = "SELECT id, firstname, lastname FROM " + USERS_TABLE;
        final int ID = 1;
        final int FIRST_NAME = 2;
        final int LAST_NAME = 3;
        List<User> usersList = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SELECT_STATEMENT);
        User user;
        while (resultSet.next()) {
            user = new User();
            user.setId(resultSet.getLong(ID));
            user.setFirstName(resultSet.getString(FIRST_NAME));
            user.setLastName(resultSet.getString(LAST_NAME));

            usersList.add(user);
        }

        return usersList;
    }

    private void setFirstCapitalLetterAndSave(Connection connection, List<User> users) throws SQLException {
        final String UPDATE_STATEMENT = "UPDATE " + USERS_TABLE + " SET firstname = ?, lastname=? WHERE id =?";
        final int ID = 3;
        final int FIRST_NAME = 1;
        final int LAST_NAME = 2;
        PreparedStatement statement = connection.prepareStatement(UPDATE_STATEMENT);
        for (User user : users) {
            user.setFirstName(getStringWithFirstCapital(user.getFirstName()));
            user.setLastName(getStringWithFirstCapital(user.getLastName()));

            statement.setString(FIRST_NAME, user.getFirstName());
            statement.setString(LAST_NAME, user.getLastName());
            statement.setLong(ID, user.getId());

            statement.executeUpdate();
        }
        statement.close();
    }

    private String getStringWithFirstCapital(String str) {
        if (str == null || str.isEmpty()) {
            return "";
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private List<String> getUsersFromEmotionTable(Connection connection) throws SQLException {
        final String SELECT_STATEMENT = "SELECT DISTINCT owner FROM " + EMOTION_TABLE;
        List<String> usersList = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SELECT_STATEMENT);
        while (resultSet.next()) {
            String user = resultSet.getString(1);
            usersList.add(user);
        }
        return usersList;
    }

    private List<User> getUsers(List<String> users) {
        List<User> resultUsers = new ArrayList<>();
        User user;
        for (String userName : users) {
            user = new User();
            String[] nameParts = userName.split("\\.");
            user.setFirstName(nameParts[0]);
            if (nameParts.length > 1) {
                user.setLastName(nameParts[1]);
            }
            resultUsers.add(user);
        }

        return resultUsers;
    }

    private void saveNewUsers(Connection connection, List<User> users) throws SQLException {
        final String INSERT_STATEMENT = "INSERT INTO " + USERS_TABLE + " (firstname, lastname, email,password) VALUES(?,?,'','')";
        final int FIRST_NAME = 1;
        final int LAST_NAME = 2;
        PreparedStatement statement = connection.prepareStatement(INSERT_STATEMENT);
        for (User user : users) {
            if (!exists(user, connection)) {
                statement.setString(FIRST_NAME, user.getFirstName());
                statement.setString(LAST_NAME, user.getLastName());

                statement.execute();
            }
        }
    }

    private boolean exists(User user, Connection connection) throws SQLException {
        final String SELECT_STATEMENT = "SELECT id FROM " + USERS_TABLE + " WHERE firstname=? AND lastname=?";
        final int FIRST_NAME = 1;
        final int LAST_NAME = 2;
        PreparedStatement statement = connection.prepareStatement(SELECT_STATEMENT);
        statement.setString(FIRST_NAME, user.getFirstName());
        statement.setString(LAST_NAME, user.getLastName());
        ResultSet resultSet = statement.executeQuery();
        return resultSet.next();
    }

    private class User {
        private Long id;
        private String firstName;
        private String lastName;
        private String name;

        String getName() {
            return name;
        }

        void setName(String name) {
            this.name = name;
        }

        Long getId() {
            return id;
        }

        void setId(Long id) {
            this.id = id;
        }

        String getFirstName() {
            return firstName;
        }

        void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        String getLastName() {
            if (lastName == null) {
                return "";
            }
            return lastName;
        }

        void setLastName(String lastName) {
            this.lastName = lastName;
        }
    }
}
