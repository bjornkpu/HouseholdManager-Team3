package data;

public class Admin extends User{
    public Admin(){

    }

    public Admin(int id, String email, String name, String phone, String password){
        super(id, email, name, phone, password);
    }

    public boolean addUserToGroup(int userId){
        return true;
    }

    public boolean kickUserFromGroup(int userId){
        return true;
    }
}
