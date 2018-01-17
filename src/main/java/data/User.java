package data;
/**
 * Class for the User-object
 *
 * @author BK
 * @author jmska
 */
public class User {
    private String email; //id
    private String name;
    private String phone;
    private String password;
    private String salt;

    public User() {
    }

    public User(String email, String name, String phone, String password, String salt) {
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.salt = salt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                '}';
    }
}
