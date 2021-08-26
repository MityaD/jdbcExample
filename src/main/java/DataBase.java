import java.sql.*;
import java.util.UUID;

public class DataBase {
    private String name_table;

    // метод подключения
    public Connection getDBConnection() {
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
        } catch (Exception ex) {
            System.out.println("Driver not found");
        }
        String connectionString = "jdbc:postgresql://" + Config.HOST + ":" + Config.PORT + "/" + Config.DBNAME;
        try {
            connection = DriverManager.getConnection(connectionString, Config.DB_USER, Config.DB_PASS);
        } catch (SQLException throwables) {
            switch (throwables.getMessage()) {//todo то что за хуйня?
            }
        }
        return connection;
    }

    // метод создания таблицы
    public void createDbUserTable(String name_table) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        String createTableUsers = "CREATE TABLE " + name_table + "("
                + "id VARCHAR , "
                + "FIRST_NAME VARCHAR, "
                + "LAST_NAME VARCHAR, "
                + "AGE INT " + ");";

        try {
            connection = getDBConnection();
            statement = connection.createStatement();

            statement.execute(createTableUsers);
            System.out.println("Таблица \"" + name_table + "\" создана!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    //Методы CRUD:
    //добавление пользователя в таблицу (CREATE-операция) INSERT-SQL-оператор
    //todo addUser
    public void addDatabase(User user, String name_table) {
        String insert = "INSERT INTO " + name_table + " (id, first_name, last_name, age)  VALUES  (?,?,?,?)";
        try {
            PreparedStatement prST = getDBConnection().prepareStatement(insert);
            prST.setString(1, String.valueOf(user.getId()));
            prST.setString(2, user.getFirst_name());
            prST.setString(3, user.getLast_name());
            prST.setInt(4, user.getAge());
            prST.addBatch();
            prST.executeUpdate();
            System.out.println("Пользователь добавлен в таблицу: " + name_table);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //чтение всех пользователей из таблицы (Read-операция) SELECT-SQL-оператор
    //todo не select а get
    public void selectAllUsers(String name_table) throws SQLException {
        String sglSelectTasks = "select * from " + name_table + " order by id desc";
        ResultSet resultSet = null;
        try {
            resultSet = getDBConnection().createStatement().executeQuery(sglSelectTasks);
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
    //todo гавно название. напиши ты ж user обновляешь, а метод хуй чего называется
    public void reUserTable(String name_table, UUID uuid, User user) throws SQLException {
        User userDo = selectUserById(name_table, uuid);
        userDo.setFirst_name(user.getFirst_name());
        userDo.setLast_name(user.getLast_name());
        deleteUser(name_table, uuid);
        addDatabase(userDo, "users");
    }

    //удаление всех пользователей по id (Delete-операция) DELETE-SQL-оператор
    public void deleteAll(String name_table) {
        String deleteTableSQL = "DELETE FROM " + name_table;
        try {
            PreparedStatement prSTDelet = getDBConnection().prepareStatement(deleteTableSQL);
            prSTDelet.addBatch();
            prSTDelet.executeUpdate();
            System.out.println("Таблица пуста");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //удаление пользователя по id (Delete-операция) DELETE-SQL-оператор
    public void deleteUser(String name_table, UUID uuid) {
        String sqlDelete = "DELETE FROM " + name_table + " WHERE id =?";
        try {
            PreparedStatement psSt = getDBConnection().prepareStatement(sqlDelete);
            psSt.setString(1, uuid.toString());
            psSt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    // чтение одного пользователя по id (Read-операция) SELECT-SQL-оператор
    //todo не select а get
    public User selectUserById(String name_table, UUID uuid) throws SQLException {
        String selectId = "select * from " + name_table + " WHERE id =?";
        ResultSet resultId = null;
        User user = null;
        try {
            PreparedStatement psSt = getDBConnection().prepareStatement(selectId);
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
        while (resultId.next()) {
            System.out.println(UUID.fromString(resultId.getString("id")) + " "
                    + resultId.getString("first_name") + " "
                    + resultId.getString("last_name") + " "
                    + resultId.getInt("age"));
        }
        return user;
    }

    //удаление таблицы полностью
    public void dropTable(String name_table) {
        String dropTab = "DROP TABLE " + name_table;
        try {
            getDBConnection().createStatement().execute(dropTab);
            System.out.println("Таблица \"" + name_table + "\" удалена!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
