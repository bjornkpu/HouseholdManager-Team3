package data;
/**
 * -Description of the class-
 *
 * @author
 */
public class Admin extends User{
    public Admin(){

    }

    public Admin(String email, String name, String phone, String password){
        super(email, name, phone, password);
    }

    public boolean addUserToGroup(int userId){
        return true;
    }

    public boolean kickUserFromGroup(int userId){
        return true;
    }
}
