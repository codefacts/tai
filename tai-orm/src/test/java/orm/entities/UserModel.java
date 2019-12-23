package orm.entities;

/**
 * Created by sohan on 6/30/2017.
 */
public interface UserModel extends BaseModel {
    String username = "username";
    String password = "password";
    String firstName = "firstName";
    String lastName = "lastName";
    String email = "email";
    String phone = "phone";
    String joinDate = "joinDate";
    String address = "address";
    String dateOfBirth = "dateOfBirth";
    String gender = "gender";
    String pictureUri = "pictureUri";
    String userType = "userType";
    String active = "active";
}
