package dk.quarantaine.api.application.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import dk.quarantaine.api.application.data.UserService;
import dk.quarantaine.commons.helpers.FormatHelper;
import dk.quarantaine.commons.exceptions.FormatException;
import dk.quarantaine.commons.exceptions.ObjectExistsException;
import dk.quarantaine.commons.dto.RegisterUserDTO;

@Component
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


    /**
     * Registers the user sent in throws exceptions when not successfull.
     * @param registerUserDTO the user object to register.
     * @throws FormatException exception thrown when format does not match.
     * @throws ObjectExistsException Thrown when user are not created.
     */
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
