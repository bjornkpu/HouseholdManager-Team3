package util;
import data.Group;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * -Used for all functionality involving email service-
 *
 * @author
 * matsed
 */
public class EmailSender {
    /**
     * This method connects with GMail's servers and sends out mail.
     *
     * @param toEmail is the email of the recipient of the email.
     * @param mailText is the password which is is now registered to the user.
     */
    private static void sendMail(String toEmail, String mailText) {
        final String username = "info.householdmanager@gmail.com";  //The Email of the sender.
        final String password = "emailpassword";

        //Properties of the used SMTP server.
        Properties props = new Properties();
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(toEmail));
            message.setSubject("New Password - Household Manager");
            //Message to the user
            message.setText(mailText);

            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Generates a random String containing 8 characters.
     *
     * @return a random String of numbers, upper- and lowercase letters.
     */
    private static String genString(){
        final String ALPHA_NUMERIC_STRING =
                "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int pwLength = 14;      //The longer the password, the more likely the user is to change it.
        StringBuilder pw = new StringBuilder();
        while (pwLength-- != 0) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            pw.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return pw.toString(); //
    }

    /**
     * This method uses the 'sendEmail()' and 'genString()' to create and send a password to a user profile.
     *
     * @param toMail The mail of the user who is getting a new password.
     * @return the new password.
     */
    public static String generateNewPassword(String toMail){
        String newPassword = genString();       //generate a new password
        String newPasswordText = "Hello, your new password for Household Manager is: "+newPassword+
                "\n Please log in and change your password. You can do this by pressing 'My Profile'->'Edit Profile'";
        sendMail(toMail,newPasswordText);           //Send the new password to the users email.
        return newPassword;                     //return the new password
    }

    public static void sendConfirmationMail(String toMail){
        String confirmationText = "Welcome as a new user of Household Manager." +
                "\n You can log in with "+toMail+ " and the password you chose." +
                "If you've forgotten your password you can get a new one sent from our Login page.";
        sendMail(toMail,confirmationText);
    }
    public static void sendInvitationMail(String toMail, Group group){
        String invitationText = "Hello!\n\n You have been invited to join a group on Household Manager. " +
                "Login to accept the invitation \n" +
                "Invited to group: "+group.getName()+ "\n Invited by: "+group.getAdmin();
        sendMail(toMail,invitationText);
    }

}
