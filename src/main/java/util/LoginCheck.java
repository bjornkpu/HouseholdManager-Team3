package util;

import data.LoginData;
import data.User;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;

public class LoginCheck {


    /**
     * Method used for checking the if the password of a LoginData matches the
     * unhashed password of the user.
     * @param data The (@code LoginData) object with the user-input password.
     * @param user The (@code User) object from the database.
     * @return Returns true if the password matches.
     */
    public static boolean correctLogin(LoginData data, User user){
        if(user == null && data.getEmail() != null) {
            return false;
        }else if(!user.getEmail().equals(data.getEmail())){
            return false;
        }
        //Get users salt
        String salt = user.getSalt();
        //Concatenate the login password and the users salt, then hash it.
        String saltedLogin = data.getPassword()+salt;
        String hashedLogin = getHash(saltedLogin);

        return (hashedLogin.equals(user.getPassword()));
    }

    /** Encrypts a password.
     *
     * @param saltedPassword The password String to be encrypted.
     * @return Returns the encrypted password as a String.
     */
    //Hashes a password
    public static String getHash(String saltedPassword) {
        String algorithm = "SHA-256";
        String hashValue = "";
        byte[] inputBytes = saltedPassword.getBytes();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            messageDigest.update(inputBytes);
            byte[] digestedBytes = messageDigest.digest();
            hashValue = DatatypeConverter.printHexBinary(digestedBytes).toLowerCase();
        } catch (Exception e) {
            System.out.println("Password hashing failed");
        }
        return hashValue;
    }
}
