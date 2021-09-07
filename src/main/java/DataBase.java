import java.sql.*;
import java.util.UUID;
import java.util.logging.Logger;

public class DataBase {

    Logger logger = Logger.getLogger(DataBase.class.getName());

    public Connection getDBConnection() {
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
        } catch (Exception ex) {
            System.out.println("Driver not found");
        }
        String connectionString = "jdbc:postgresql://" + Config.HOST + ":" + Config.PORT + "/" + Config.DBNAME;
        try {
            connection = DriverManager.getConnection(connectionString, Config.DB_USER.getConfigDB(), Config.DB_PASS.getConfigDB());
        } catch (SQLException throwables) {
             throwables.getMessage(); //
        }
        return connection;
    }

    public void createTable(String nameTable) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        String sglCodeTasks = "CREATE TABLE " + nameTable + "("
                + "id VARCHAR , "
                + "first_name VARCHAR, "
                + "last_name VARCHAR, "
                + "age INT " + ");";

        try {
            connection = getDBConnection();
            statement = connection.createStatement();

            statement.execute(sglCodeTasks);
            logger.info ("Таблица \"" + nameTable + "\" создана!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    public void addUser(User user, String nameTable) {
        String sglCodeTasks = "INSERT INTO " + nameTable + " (id, first_name, last_name, age)  VALUES  (?,?,?,?)";
        try {
            PreparedStatement prST = getDBConnection().prepareStatement(sglCodeTasks);
            prST.setString(1, String.valueOf(user.getId()));
            prST.setString(2, user.getFirst_name());
            prST.setString(3, user.getLast_name());
            prST.setInt(4, user.getAge());
            prST.addBatch();
            prST.executeUpdate();
            logger.info ("Пользователь добавлен в таблицу: " + nameTable);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void getAllUsers(String nameTable) throws SQLException {
        String sglCodeTasks = "select * from " + nameTable + " order by id desc";
        ResultSet resultSet = null;
        try {
            resultSet = getDBConnection().createStatement().executeQuery(sglCodeTasks);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        while (resultSet.next()) {
            logger.info (resultSet.getObject("id") + " "
                    + resultSet.getString("first_name") + " "
                    + resultSet.getString("last_name") + " "
                    + resultSet.getInt("age"));
        }
    }

    public void userUpdate(String nameTable, UUID uuid, User user) throws SQLException {
        User userDouble = getUserById(nameTable, uuid);
        userDouble.setFirst_name(user.getFirst_name());
        userDouble.setLast_name(user.getLast_name());
        deleteUser(nameTable, uuid);
        addUser(userDouble, "users");
    }

    public void deleteAll(String nameTable) {
        String sglCodeTasks = "DELETE FROM " + nameTable;
        try {
            PreparedStatement prSTDelet = getDBConnection().prepareStatement(sglCodeTasks);
            prSTDelet.addBatch();
            prSTDelet.executeUpdate();
            logger.info ("Таблица пуста");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(String nameTable, UUID uuid) {
        String sglCodeTasks = "DELETE FROM " + nameTable + " WHERE id =?";
        try {
            PreparedStatement psSt = getDBConnection().prepareStatement(sglCodeTasks);
            psSt.setString(1, uuid.toString());
            psSt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public User getUserById(String nameTable, UUID uuid) throws SQLException {
        String sglCodeTasks = "select * from " + nameTable + " WHERE id =?";
        ResultSet resultId = null;
        User user = null;
        try {
            PreparedStatement psSt = getDBConnection().prepareStatement(sglCodeTasks);
            psSt.setString(1, uuid.toString());
            resultId = psSt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        while (resultId.next()) {
            user = new User();
            user.setId(UUID.fromString(resultId.getString(1)));
            user.setFirst_name(resultId.getString(2));
            user.setLast_name(resultId.getString(3));
            user.setAge(resultId.getInt(4));
        }
        return user;
    }

    public void dropTable(String nameTable) {
        try {
            getDBConnection().createStatement().execute("DROP TABLE " + nameTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
