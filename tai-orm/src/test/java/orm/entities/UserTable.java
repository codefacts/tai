package orm.entities;

/**
 * Created by sohan on 6/30/2017.
 */
public interface UserTable extends BaseTable {
    String username = "username";
    String email = "email";
    String phone = "phone";
    String address = "address";
    String password = "password";
    String date_of_birth = "date_of_birth";
    String first_name = "first_name";
    String last_name = "last_name";
    String gender = "gender";
    String picture_uri = "picture_uri";
    String join_date = "join_date";
    String user_type = "user_type";
    String active = "active";
}
