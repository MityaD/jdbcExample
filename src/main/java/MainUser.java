import java.util.UUID;

public class MainUser {

    public static void main(String[] args) throws Exception {

        DataBase dataBase = new DataBase();
        dataBase.dropTable("users"); // вызов метода удаления таблицы
        dataBase.createDbUserTable("users");     //вызов метода создания таблицы

        UUID uuidUser = UUID.randomUUID();
        UUID uuidUser1 = UUID.randomUUID();
        UUID uuidUser2 = UUID.randomUUID();
        UUID uuidUser5 = UUID.randomUUID();

        User user = new User(uuidUser, "Yakushyk", "Dmitri", 32);
        User user1 = new User(uuidUser1, "Disko", "Shurik", 29);
        User user2 = new User(uuidUser2, "Alex", "Sania", 35);
        User user5 = new User(uuidUser5, "Truhanov", "Stefan", 40);

        dataBase.addUser(user, "users"); // вызов метода добавления пользователя
        dataBase.addUser(user1, "users");// в таблицу (CREATE-операция) INSERT-SQL-оператор
        dataBase.addUser(user2, "users");

        //dataBase.getAllUsers("users"); //вызов метода чтения всех пользователей из таблицы (Read-операция) SELECT-SQL-оператор

        //dataBase.updateUserInDB("users", uuidUser2, user5); // вызов метода обновления (Редактирования) (Update-операция)

        //dataBase.deleteAll("users"); //вызов метода удаления всех пользователей по id (Delete-операция) DELETE-SQL-оператор

        //dataBase.deleteUser("users", uuidUser); //вызыв метода удаления пользователя по id (Delete-операция) DELETE-SQL-оператор

        //dataBase.getUserById("users", uuidUser1); //вызов метода чтение одного пользователя по id (Read-операция) SELECT-SQL-оператор
    }
}
