package dk.quarantaine.api.application.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;

import dk.quarantaine.api.application.data.UserService;
import dk.quarantaine.api.application.helper.FormatHelper;
import dk.quarantaine.api.application.dto.RegisterUserDTO;
import dk.quarantaine.api.application.exception.FormatException;
import dk.quarantaine.api.application.exception.ObjectExistsException;

public class UserLogic {
    int strength = 12;
      
    @Autowired
    UserService userService;

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


    public void registerUser(RegisterUserDTO registerUserDTO) throws FormatException,ObjectExistsException{
        if(!FormatHelper.validatePassword(registerUserDTO.getPassword())){
            throw new FormatException("Password does not match password policy");
        }
        else if(!FormatHelper.validateUsername(registerUserDTO.getUsername())){
            throw new FormatException("Username contains illigal characters");
        }
        else if (!FormatHelper.validatePhoneNumber(registerUserDTO.getPhoneNumber())){
            throw new FormatException("Phone not a valid format");
        }
        else if (registerUserDTO.getName().isEmpty()){
            throw new FormatException("Name cannot be empty");
        }
        
        //Hash password
        registerUserDTO.setPassword(hashPassword(registerUserDTO.getPassword()));
        
        if(!userService.registerUser(registerUserDTO)){
            throw new ObjectExistsException("Could not create object");
        }
    }
}
