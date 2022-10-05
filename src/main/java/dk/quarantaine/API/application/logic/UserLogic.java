package dk.quarantaine.api.application.logic;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import dk.quarantaine.api.application.data.TokenService;
import dk.quarantaine.api.application.data.UserService;
import dk.quarantaine.commons.helpers.FormatHelper;
import dk.quarantaine.commons.exceptions.FormatException;
import dk.quarantaine.commons.exceptions.ObjectExistsException;
import dk.quarantaine.commons.dto.OauthRequestDTO;
import dk.quarantaine.commons.dto.ClientIDAndSecret;
import dk.quarantaine.commons.dto.OauthTokenResponseDTO;
import dk.quarantaine.commons.dto.RegisterUserDTO;

@Component
public class UserLogic {
    int strength = 12;
      
    @Autowired
    UserService userService;

    @Autowired
    TokenService tokenService;

    @Value("${application.clientID}")
    String clientID;

    @Value("${application.clientSecret}")
    String clientSecret;


    @Value("${access.token.validity.seconds:3600}")
    int tokenValiditySeconds;

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
     * Authorizes the user given the Credentials and Clint credentials
     * @param clientidAndsecret the client credentials
     * @param userCredintials the user credentials
     * @return the access token which can be used for loggin in
     * @throws Exception called when user is not authorized or failed to save within in the database.
     */
    public OauthTokenResponseDTO authorizeUser(ClientIDAndSecret clientidAndsecret, OauthRequestDTO userCredintials) throws Exception{
        if(!clientID.equals(clientidAndsecret.getClientID()) || !clientSecret.equals(clientidAndsecret.getClientSecret())){
            throw new Exception("ClientID or ClientSecret are not correct");
        }
        
        RegisterUserDTO userinfo = userService.getUserInformation(userCredintials.getUsername());
        
        if(userinfo == null || !verifyPassword(userCredintials.getPassword(), userinfo.getPassword())){
            throw new Exception("User or password are not correct");
        }
        
        OauthTokenResponseDTO token = new OauthTokenResponseDTO();
        token.setToken_type("Bearer");
        token.setAccess_token(UUID.randomUUID().toString());
        token.setRefresh_token(UUID.randomUUID().toString());
        token.setValidity(tokenValiditySeconds);


        if(tokenService.save(token, userinfo.getUsername())){
            return token;
        }

        throw new Exception("failed to save the token in the databse");
    }

    /**
     * Registers the user sent in throws exceptions when not successfull.
     * @param registerUserDTO the user object to register.
     * @throws FormatException exception thrown when format does not match.
     * @throws ObjectExistsException Thrown when user are not created.
     */
    public void registerUser(RegisterUserDTO registerUserDTO) throws FormatException,ObjectExistsException{
        if(!FormatHelper.validatePassword(registerUserDTO.getPassword())){
            throw new FormatException("Password does not match password policy","Password","");
        }
        else if(!FormatHelper.validateUsername(registerUserDTO.getUsername())){
            throw new FormatException("Contains illigal characters","Username",registerUserDTO.getUsername());
        }
        else if (!FormatHelper.validatePhoneNumber(registerUserDTO.getPhoneNumber())){
            throw new FormatException("Not a valid format","PhoneNumber",registerUserDTO.getPhoneNumber());
        }
        else if (registerUserDTO.getName().isEmpty()){
            throw new FormatException("Cannot be empty","Name",registerUserDTO.getName());
        }
        
        //Hash password
        registerUserDTO.setPassword(hashPassword(registerUserDTO.getPassword()));
        
        if(!userService.registerUser(registerUserDTO)){
            throw new ObjectExistsException("Could not create object");
        }
    }
}
