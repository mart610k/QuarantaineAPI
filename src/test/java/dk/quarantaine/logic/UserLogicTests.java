package dk.quarantaine.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dk.quarantaine.api.application.data.UserService;
import dk.quarantaine.commons.exceptions.FormatException;
import dk.quarantaine.commons.exceptions.ObjectExistsException;
import dk.quarantaine.commons.dto.RegisterUserDTO;
import dk.quarantaine.api.application.logic.UserLogic;


@ExtendWith(MockitoExtension.class)
public class UserLogicTests {
    
    @InjectMocks
    UserLogic userLogic;

    @Mock
    UserService userService;


    /**
     * Tests if the password does actually hash and checks it towards a Regex
     */
    @Test
    public void hashPassword_Hashes(){
        
        //SETUP
        String passwordPlain = "W8x$YPMZxBkb&Qsb4&V3";


        //ACT
        String actual = userLogic.hashPassword(passwordPlain);

        //^\$2a\$[0-9]{1,2}\$

        //VERIFY
        assertTrue(actual.matches("^\\$2a\\$[0-9]{1,2}\\$.{0,}$"));

    }

    /**
     * Verifies a previous given hash with known password
     */
    @Test
    public void verifyPassword_Verifies(){
        //SETUP
        
        String passwordPlain = "TUh4b&Qq@CwehWb3XLvc";
        String passwordHash = "$2a$12$Dn4bKKXtfARfcygNq8bUmeVpHSTKPBJA1cljmzUJzeix/sCXU0WQO";


        //ACT
        boolean actual = userLogic.verifyPassword(passwordPlain, passwordHash);

        //VERIFY
        assertEquals(true, actual);
    }

    /**
     * Checks that internal Logic can hash and verify password hashes generated by itself
     */
    @Test
    public void verifyPassword_HashedPasswordCanBeVerified(){
        //SETUP
        String passwordPlain = "CKtTa4MUf#v$YscqRB9$";

        //ACT
        String actual = userLogic.hashPassword(passwordPlain);
        
        //VERIFY
        assertTrue(userLogic.verifyPassword(passwordPlain, actual));
    }


    @Test
    public void registerUser_ThrowsExceptionPasswordNotValid(){
        //SETUP
        RegisterUserDTO userDTO = new RegisterUserDTO();
        userDTO.setName("Name Name");
        userDTO.setPassword(" ");
        userDTO.setPhoneNumber("48392038");
        userDTO.setUsername("test242");

        //ACT + VERIFY
        FormatException exception = assertThrows(FormatException.class,() -> userLogic.registerUser(userDTO));

        String expectedMessage = "Password does not match password policy";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void registerUser_ThrowsExceptionUsernameNotValid(){

        //SETUP
        RegisterUserDTO userDTO = new RegisterUserDTO();
        userDTO.setName("Name Name");
        userDTO.setPassword("LgkHaTv7e6GZm4Z2!EtN");
        userDTO.setPhoneNumber("48392038");
        userDTO.setUsername("P!AS42");

        //ACT + VERIFY
        FormatException exception = assertThrows(FormatException.class,() -> userLogic.registerUser(userDTO));

        String expectedMessage = "Contains illigal characters";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }
    

    @Test
    public void registerUser_ThrowsExceptionPhoneNumberNotValid(){
         //SETUP
         RegisterUserDTO userDTO = new RegisterUserDTO();
         userDTO.setName("Name Name");
         userDTO.setPassword("LgkHaTv7e6GZm4Z2!EtN");
         userDTO.setPhoneNumber("483f2038");
         userDTO.setUsername("test32");
 
         //ACT + VERIFY
         FormatException exception = assertThrows(FormatException.class,() -> userLogic.registerUser(userDTO));
 
         String expectedMessage = "Not a valid format";
         String actualMessage = exception.getMessage();
         assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void registerUser_ThrowsExceptionNameEmpty(){
       //SETUP
       RegisterUserDTO userDTO = new RegisterUserDTO();
       userDTO.setName("");
       userDTO.setPassword("LgkHaTv7e6GZm4Z2!EtN");
       userDTO.setPhoneNumber("48392038");
       userDTO.setUsername("test32");

       //ACT + VERIFY
       FormatException exception = assertThrows(FormatException.class,() -> userLogic.registerUser(userDTO));

       String expectedMessage = "Cannot be empty";
       String actualMessage = exception.getMessage();
       assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void registerUser_Success(){
        //SETUP
        RegisterUserDTO userDTO = new RegisterUserDTO();
        userDTO.setName("Anders Andersen");
        userDTO.setPassword("LgkHaTv7e6GZm4Z2!EtN");
        userDTO.setPhoneNumber("48392038");
        userDTO.setUsername("test32");

        
        try{
            when(userService.registerUser(userDTO)).thenReturn(true);
            //ACT
            userLogic.registerUser(userDTO);
            //VERIFY
            verify(userService,times(1)).registerUser(userDTO);
        }
        catch(Exception e){
            fail("Register User threw an exception when it was not supposed to");
        }   
    }


    @Test
    public void registerUser_UserNameExisting(){
        //SETUP
        RegisterUserDTO userDTO = new RegisterUserDTO();
        userDTO.setName("Anders Andersen");
        userDTO.setPassword("LgkHaTv7e6GZm4Z2!EtN");
        userDTO.setPhoneNumber("48392038");
        userDTO.setUsername("test32");
        try{
            when(userService.registerUser(any())).thenReturn(false);

            //ACT + VERIFY
            ObjectExistsException exception = assertThrows(ObjectExistsException.class,() -> userLogic.registerUser(userDTO));
    
            String expectedMessage = "Could not create object";
            String actualMessage = exception.getMessage();
            assertEquals(actualMessage, expectedMessage);   
        }
        catch(Exception e){
            fail("Unexpected Exception called");
        }

       
    }

}
