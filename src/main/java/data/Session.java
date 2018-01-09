package data;

import java.util.Date;

/**
 *
 */
public class Session {
    private String email;
    private Date loggedOn;

    public Session() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getLoggedOn() {
        return loggedOn;
    }

    public void setLoggedOn(Date loggedOn) {
        this.loggedOn = loggedOn;
    }
}
