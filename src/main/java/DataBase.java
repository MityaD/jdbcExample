import java.sql.*;
import java.util.UUID;

public class DataBase {//todo после того как исправил мои коменты удаляй
    private String name_table;//todo а нах это поле здесь? не понимаю...

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
            connection = DriverManager.getConnection(connectionString, Config.DB_USER.getConfigDB(), Config.DB_PASS.getConfigDB());
        } catch (SQLException throwables) {
             throwables.getMessage(); //todo то что за хуйня?
        }
        return connection;
    }

    // метод создания таблицы
    //todo вы с рыжим с одного места копировали? ты метод назвал как будет он создает таблицу пользователей, хотя он может создатьь любу таблицу которую передашь параметром. Логично его назвать createTable
    public void createDbUserTable(String name_table) throws SQLException { //todo переменные должны называться по конвенции Java camelCase. везде глянь
        Connection connection = null;
        Statement statement = null;
        String createTableUsers = "CREATE TABLE " + name_table + "(" //todo почему в каждом методе переменная которая делает одно и тоже называется по разному? назови как-нибудь обще и одинаково
                + "id VARCHAR , "
                + "FIRST_NAME VARCHAR, " //todo имена колонок в бд должны называться first_name
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
            if (statement != null) {//todo а че два раза?
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
    public void addUser(User user, String name_table) {
        String insert = "INSERT INTO " + name_table + " (id, first_name, last_name, age)  VALUES  (?,?,?,?)"; //todo чет не понял создаешь с таким именем а добавляешь колонки по другому названы... это как?
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
    public void getAllUsers(String name_table) throws SQLException {
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
    //todo гавно название. просто userUpdate  id здесь не нужен ты его м юзера можешь достать
    public void updateUserInDB(String name_table, UUID uuid, User user) throws SQLException {
        User userDo = getUserById(name_table, uuid);//todo дибильное название переменной
        userDo.setFirst_name(user.getFirst_name());
        userDo.setLast_name(user.getLast_name());
        deleteUser(name_table, uuid);
        addUser(userDo, "users");
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
    public User getUserById(String name_table, UUID uuid) throws SQLException {
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
            System.out.println(UUID.fromString(resultId.getString("id")) + " "//todo нах вывод в консоль здесь? хочешь выводить выводи в маине
                    + resultId.getString("first_name") + " "
                    + resultId.getString("last_name") + " "
                    + resultId.getInt("age"));
        }
        return user;
    }

    //удаление таблицы полностью
    public void dropTable(String name_table) {
        String dropTab = "DROP TABLE " + name_table; //todo гавно название. И короткие запросы можно сразу в параметрах писать, норм будет и гавно название не будет
            getDBConnection().createStatement().execute(dropTab);
            System.out.println("Таблица \"" + name_table + "\" удалена!"); //todo нах вывод в консоль здесь? хочешь выводить выводи в маине
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
