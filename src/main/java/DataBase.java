import java.sql.*;
import java.util.UUID;

public class DataBase {
    private String nameTable;//todo а нах это поле здесь? не понимаю...
    private String nameTableAddress;
    // метод подключения
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

    //создание таблицы адресов
    public void createTableAddresses(String nameTable) throws SQLException {
        Connection connectionCTA = null;
        Statement statement = null;
        String sglCodeTasks = "CREATE TABLE " + nameTable + "("
                + "id VARCHAR PRIMARY KEY , "
                + "city VARCHAR, "
                + "street VARCHAR, "
                + "house VARCHAR, "
                + nameTableAddress + "Id VARCHAR REFERENCES " + nameTable + "(id)" + ");";

        try {
            connectionCTA = getDBConnection();
            statement = connectionCTA.createStatement();

            statement.execute(sglCodeTasks);
            System.out.println("Таблица \"" + nameTable + "\" создана!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (connectionCTA != null) {
                connectionCTA.close();
            }
        }
    }

    // метод создания таблицы пользователей
    public void createTable(String nameTable) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        String sglCodeTasks = "CREATE TABLE " + nameTable + "("
                + "id VARCHAR PRIMARY KEY , "
                + "first_name VARCHAR, "
                + "last_name VARCHAR, "
                + "age INT "
                + ");";

        try {
            connection = getDBConnection();
            statement = connection.createStatement();

            statement.execute(sglCodeTasks);
            System.out.println("Таблица \"" + nameTable + "\" создана!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    //Методы CRUD:
    //добавление пользователя в таблицу (CREATE-операция) INSERT-SQL-оператор
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
            System.out.println("Пользователь добавлен в таблицу: " + nameTable);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //добавление пользователя в таблицу (CREATE-операция) INSERT-SQL-оператор
    public void addAddress(Address address, String nameTable) {
        String sglCodeTasks = "INSERT INTO " + nameTable + " (id, city, street, house)  VALUES  (?,?,?,?)";
        try {
            PreparedStatement prST = getDBConnection().prepareStatement(sglCodeTasks);
            prST.setString(1, String.valueOf(address.getId()));
            prST.setString(2, address.getCity());
            prST.setString(3, address.getStreet());
            prST.setString(4, address.getHouse());
            prST.addBatch();
            prST.executeUpdate();
            System.out.println("Адрес добавлен в таблицу: " + nameTable);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    //чтение всех пользователей из таблицы (Read-операция) SELECT-SQL-оператор
    public void getAllUsers(String nameTable) throws SQLException {
        String sglCodeTasks = "select * from " + nameTable + " order by id desc";
        ResultSet resultSet = null;
        try {
            resultSet = getDBConnection().createStatement().executeQuery(sglCodeTasks);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        while (resultSet.next()) {
            System.out.println(resultSet.getObject("id") + " "
                    + resultSet.getString("first_name") + " "
                    + resultSet.getString("last_name") + " "
                    + resultSet.getInt("age"));
        }
    }

    // Обновление (Редактирование) (Update-операция)
    //todo гавно название. просто userUpdate  id здесь не нужен ты его м юзера можешь достать
    public void userUpdate(String nameTable, UUID uuid, User user) throws SQLException { //нужно в методе getUserById
        User newUserUpdate = getUserById(nameTable, uuid);
        newUserUpdate.setFirst_name(user.getFirst_name());
        newUserUpdate.setLast_name(user.getLast_name());
        deleteUser(nameTable, uuid);
        addUser(newUserUpdate, "users");
    }

    //удаление всех пользователей по id (Delete-операция) DELETE-SQL-оператор
    public void deleteAll(String nameTable) {
        String sglCodeTasks = "DELETE FROM " + nameTable;
        try {
            PreparedStatement prSTDelet = getDBConnection().prepareStatement(sglCodeTasks);
            prSTDelet.addBatch();
            prSTDelet.executeUpdate();
            System.out.println("Таблица пуста");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //удаление пользователя по id (Delete-операция) DELETE-SQL-оператор
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

    // чтение одного пользователя по id (Read-операция) SELECT-SQL-оператор
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
            //удаление таблицы полностью
    public void dropTable(String nameTable) {
        try {
            getDBConnection().createStatement().execute("DROP TABLE " + nameTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
