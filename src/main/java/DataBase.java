import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public void getUserByHouseNumber(String house) throws SQLException {
        String sqlCodeTasks = "SELECT id_address, first_name, last_name, age, city, street, house " +
                "FROM users INNER JOIN user_address " +
                "ON id_address = user_address.id " +
                "WHERE house = '" + house + "';";
        ResultSet resultSet = getDBConnection().createStatement().executeQuery(sqlCodeTasks);
        while (resultSet.next()) {
            logger.info(dataTime() + " " + resultSet.getObject("id_address") + " "
                    + resultSet.getString("first_name") + " "
                    + resultSet.getString("last_name") + " "
                    + resultSet.getInt("age") + " "
                    + resultSet.getString("city") + " "
                    + resultSet.getString("street") + " "
                    + resultSet.getString("house"));
        }
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
