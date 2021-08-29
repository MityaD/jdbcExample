import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.logging.Logger;

public class DataBase {

    public Logger logger = Logger.getLogger(DataBase.class.getName());

    public Connection getDBConnection() {
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
        } catch (Exception ex) {
            System.out.println("Driver not found");
        }
        String connectionString = "jdbc:postgresql://" + Config.HOST.getConfigDB() + ":" + Config.PORT.getConfigDB() + "/" + Config.DBNAME.getConfigDB();
        try {
            connection = DriverManager.getConnection(connectionString, Config.DB_USER.getConfigDB(), Config.DB_PASS.getConfigDB());
        } catch (SQLException throwables) {
            throwables.getMessage(); //
        }
        return connection;
    }

    public void createTableAddresses() throws SQLException {
        Connection connectionCTA = null;
        Statement statement = null;
        String sglCodeTasks = "CREATE TABLE user_address ("
                + "id VARCHAR PRIMARY KEY , "
                + "city VARCHAR, "
                + "street VARCHAR, "
                + "house VARCHAR "
                +  ");";
        try {
            connectionCTA = getDBConnection();
            statement = connectionCTA.createStatement();
            statement.execute(sglCodeTasks);
            logger.info(dataTime() + " Таблица user_address создана!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (connectionCTA != null) {
                connectionCTA.close();
            }
        }
    }

    public void createTable() throws SQLException {
        Connection connection = null;
        Statement statement = null;
        String sglCodeTasks = "CREATE TABLE users("
                + "id VARCHAR PRIMARY KEY , "
                + "first_name VARCHAR, "
                + "last_name VARCHAR, "
                + "age INT, "
                + "id_address VARCHAR, "
                + "FOREIGN KEY (id_address) REFERENCES user_address (id) ON DELETE CASCADE"
                + ");";
        try {
            connection = getDBConnection();
            statement = connection.createStatement();
            statement.execute(sglCodeTasks);
            logger.info(dataTime() +"Таблица users создана!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    public void addRecord(User user, Address address) {
        String sqlCodeTasksAddress = "INSERT INTO user_address (id, city, street, house)  VALUES  (?,?,?,?)";
        String sqlCodeTasksUser = "INSERT INTO users (id, first_name, last_name, age, id_address)  VALUES  (?,?,?,?,?)";
        try {
            addUser(sqlCodeTasksAddress, address);
            logger.info(dataTime() +"Адрес добавлен в таблицу user_address");
            addAddress(sqlCodeTasksUser, user, address);
            logger.info(dataTime() +"Пользователь добавлен в таблицу users");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void addAddress(String sql, User user, Address address) throws SQLException {
        PreparedStatement prST = getDBConnection().prepareStatement(sql);
        prST.setString(1, String.valueOf(user.getId()));
        prST.setString(2, user.getFirst_name());
        prST.setString(3, user.getLast_name());
        prST.setInt(4, user.getAge());
        prST.setString(5, String.valueOf(address.getId()));
        prST.addBatch();
        prST.executeUpdate();
    }

    //
    public void addUser(String sql, Address address) throws SQLException {
        PreparedStatement prST2 = getDBConnection().prepareStatement(sql);
        prST2.setString(1, String.valueOf(address.getId()));
        prST2.setString(2, address.getCity());
        prST2.setString(3, address.getStreet());
        prST2.setString(4, address.getHouse());
        prST2.addBatch();
        prST2.executeUpdate();
    }
    public String getUsers(String id_address) throws SQLException {
        String sqlCodeTasks = "SELECT * FROM users ORDER BY " + id_address + " DESC";
        ResultSet resultSet = null;
        resultSet = getDBConnection().createStatement().executeQuery(sqlCodeTasks);
            return String.valueOf(resultSet);
    }

//    public void getAllUsers(String house) throws SQLException {
//        String sglCodeTasks = "select * from user_address order by " + house + " desc";
//        ResultSet resultSet = null;
//        try {
//            resultSet = getDBConnection().createStatement().executeQuery(sglCodeTasks);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        while (resultSet.next()) {
//            System.out.println(resultSet.getObject("id") + " "
//                    + resultSet.getString("first_name") + " "
//                    + resultSet.getString("last_name") + " "
//                    + resultSet.getInt("age")) + " "
//                    + getUsers();
//        }
//    }

    //удаление пользователя по id (Delete-операция) DELETE-SQL_INFO-оператор
    public void deleteUser(String nameTable, UUID uuidUser, String nameTableAddress, UUID uuidAdress) {
        String sglCodeTasks = "DELETE FROM " + nameTable + " WHERE id =?";
        String sglCodeTasks2 = "DELETE FROM " + nameTableAddress + " WHERE id =?";
        try {
            PreparedStatement psSt = getDBConnection().prepareStatement(sglCodeTasks);
            psSt.setString(1, uuidUser.toString());
            psSt.executeUpdate();
            PreparedStatement psSt2 = getDBConnection().prepareStatement(sglCodeTasks2);
            psSt2.setString(1, uuidAdress.toString());
            psSt2.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    // чтение одного пользователя по id (Read-операция) SELECT-SQL_INFO-оператор
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
            logger.info(dataTime() + "Таблица: " + nameTable + " удалена");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String  dataTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("d MMMM, yyyy HH:mm:ss"));
    }
}
