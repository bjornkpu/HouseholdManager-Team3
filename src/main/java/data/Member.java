package data;
/**
 * -Description of the class-
 *
 * @author
 * Martin Wangen
 */

public class Member extends User{
    private double balance;
    private int status;

    /**
     * User is pending authorization to join the group he wishes to access.
     *
     */
    public static final int PENDING_STATUS = 0;

    /**
     * The user is a member of the group
     *
     */
    public static final int ACCEPTED_STATUS = 1;

    /**
     * The user is a member of the group and the admin of the group.
     *
     */
    public static final int ADMIN_STATUS = 2;

    /**
     * The user account is a member of the group but have had his access disabled.
     */
    public static final int DISABLED_STATUS = 3;

    /**
     * The user is banned from the group.
     *
     */
    public final static int BANNED_STATUS = 4;

    public static final int BLOCKED_STATUS = 3;

    public Member() {}

    public Member(String email, String name, String phone, String password, String salt, double balance, int status) {
        super(email, name, phone, password, salt);
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