import java.util.UUID;

public class MainUser {
    private static final String ADDRESS = "user_address";
    private static final String USERS = "users";

    public static void main(String[] args) throws Exception {
        DataBase dataBase = new DataBase();
        UUID uuidUser = UUID.randomUUID();
        UUID uuidUser1 = UUID.randomUUID();
        UUID uuidUser2 = UUID.randomUUID();
        UUID uuidUser3 = UUID.randomUUID();
        UUID uuidUser4 = UUID.randomUUID();

        UUID uuidUserA = UUID.randomUUID();
        UUID uuidUserA1 = UUID.randomUUID();
        UUID uuidUserA2 = UUID.randomUUID();
        UUID uuidUserA3 = UUID.randomUUID();
        UUID uuidUserA4 = UUID.randomUUID();

        dataBase.dropTable(USERS); // вызов метода удаления таблицы
        dataBase.dropTable(ADDRESS); // вызов метода удаления таблицы

        dataBase.createTableAddresses(); //вызов метода создания таблицы
        dataBase.createTable(); //вызов метода создания таблицы

        Address address = new Address(uuidUserA, "Bereza", "Lubarskogo", "30A");
        Address address1 = new Address(uuidUserA1, "Bereza", "S.Gorodok", "20B");
        Address address2 = new Address(uuidUserA2, "Moscov", "Tuturina", "201");
        Address address3 = new Address(uuidUserA3, "Moscov", "Tuturina", "201");
        Address address4 = new Address(uuidUserA4, "Moscov", "Tuturina", "201");

        User user = new User(uuidUser, "Yakushyk", "Dmitri", 32);
        User user1 = new User(uuidUser1, "Disko", "Shurik", 29);
        User user2 = new User(uuidUser2, "Lazik", "Sania", 35);
        User user3 = new User(uuidUser3, "Lazik1", "Sania1", 35);
        User user4 = new User(uuidUser4, "Lazik2", "Sania2", 35);

        dataBase.addRecord(user, address);        // вызов метода добавления пользователя
        dataBase.addRecord(user1, address1);
        dataBase.addRecord(user2, address2);
        dataBase.addRecord(user3, address3);
        dataBase.addRecord(user4, address4);

        //dataBase.getAllUsers("30A");
    }
}
