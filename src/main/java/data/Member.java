package data;
/**
 * -Description of the class-
 *
 * @author
 */
public class Member extends User{
    private double balance;
    private int status;

    public Member() {}

    public Member(String email, String name, String phone, String password, double balance, int status) {
        super(email, name, phone, password);
        this.balance = balance;
        this.status = status;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}