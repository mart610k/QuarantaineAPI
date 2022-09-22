package dk.quarantaine.api.application.logic;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class UserLogic {

    int strength = 12;
      

    /**
     * Hashes the password with Bcrypt algorithm
     * @param plainPassword Plain text password
     * @return Hashed version of the password
     */
    public String hashPassword(String plainPassword){
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(strength));
        //return "";
    }


    /**
     * Verifies the incoming password with the hash given
     * @param plainPassword the plain text password to verify
     * @param hash hashed version of the password
     * @return wether or not the password was able to be verified
     */
    public boolean verifyPassword(String plainPassword, String hash){
        return BCrypt.checkpw(plainPassword, hash);
    }

    public boolean validatePassword(String toValidate){
        return toValidate.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%#^*?&])[A-Za-z\\d@$!#%^*?&]{8,}$");
        
    }


    public boolean validatePhoneNumber(String toValidate) {
        return (toValidate == null ? false : toValidate.matches("^[0-9]{8}$"));
    }


}
