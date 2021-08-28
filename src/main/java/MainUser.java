import java.util.UUID;

public class MainUser {
    private static final String ADDRESS = "user_address";
    private static final String USERS = "users";
    public static void main(String[] args) throws Exception {

        DataBase dataBase = new DataBase();
        dataBase.dropTable(USERS); // вызов метода удаления таблицы
        dataBase.dropTable(ADDRESS); // вызов метода удаления таблицы
        dataBase.createTable(USERS); //вызов метода создания таблицы
        dataBase.createTableAddresses(ADDRESS);

        UUID uuidUser = UUID.randomUUID();
        UUID uuidUser1 = UUID.randomUUID();
        UUID uuidUser2 = UUID.randomUUID();
        UUID uuidUser5 = UUID.randomUUID();

        User user = new User(uuidUser, "Yakushyk", "Dmitri", 32);
        Address address = new Address(uuidUser, "Bereza", "Lubarskogo", "30A");
        User user1 = new User(uuidUser1, "Disko", "Shurik", 29);
        Address address1 = new Address(uuidUser1, "Bereza", "S.Gorodok", "20B");
        User user2 = new User(uuidUser2, "Lazik", "Sania", 35);
        Address address2 = new Address(uuidUser2, "Moscov", "Tuturina", "201");
        //User user5 = new User(uuidUser5, "Truhanov", "Stefan", 40);

        dataBase.addUser(user, "users"); // вызов метода добавления пользователя
        dataBase.addAddress(address, ADDRESS);
        dataBase.addUser(user1, "users");// в таблицу (CREATE-операция) INSERT-SQL-оператор
        dataBase.addAddress(address1, ADDRESS);
        dataBase.addUser(user2, "users");
        dataBase.addAddress(address2, ADDRESS);

        //dataBase.getAllUsers("users"); //вызов метода чтения всех пользователей из таблицы (Read-операция) SELECT-SQL-оператор

        //dataBase.updateUserInDB("users", uuidUser2, user5); // вызов метода обновления (Редактирования) (Update-операция)

        //dataBase.deleteAll("users"); //вызов метода удаления всех пользователей по id (Delete-операция) DELETE-SQL-оператор

        //dataBase.deleteUser("users", uuidUser); //вызыв метода удаления пользователя по id (Delete-операция) DELETE-SQL-оператор

        //dataBase.getUserById("users", uuidUser1); //вызов метода чтение одного пользователя по id (Read-операция) SELECT-SQL-оператор
    }
}
