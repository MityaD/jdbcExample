import java.util.UUID;

public class MainUser {
    private static final String ADDRESS = "user_address";
    private static final String USERS = "users";

    public static void main(String[] args) throws Exception {
        DataBase dataBase = new DataBase();
        UUID uuidUser = UUID.randomUUID();
        UUID uuidUser1 = UUID.randomUUID();
        UUID uuidUser2 = UUID.randomUUID();
        UUID uuidUser5 = UUID.randomUUID();

        dataBase.dropTable(USERS); // вызов метода удаления таблицы
        dataBase.dropTable(ADDRESS); // вызов метода удаления таблицы

        dataBase.createTableAddresses(); //вызов метода создания таблицы
        dataBase.createTable(); //вызов метода создания таблицы

        Address address = new Address(uuidUser, "Bereza", "Lubarskogo", "30A");
        Address address1 = new Address(uuidUser1, "Bereza", "S.Gorodok", "20B");
        Address address2 = new Address(uuidUser2, "Moscov", "Tuturina", "201");

        User user = new User(uuidUser, "Yakushyk", "Dmitri", 32);
        User user1 = new User(uuidUser1, "Disko", "Shurik", 29);
        User user2 = new User(uuidUser2, "Lazik", "Sania", 35);

        dataBase.addUser(user, address);        // вызов метода добавления пользователя
        dataBase.addUser(user1, address1);
        dataBase.addUser(user2, address2);

        //dataBase.deleteUser(USERS, uuidUser, ADDRESS, uuidUser); //вызыв метода удаления пользователя по id (Delete-операция) DELETE-SQL-оператор

    }
}
