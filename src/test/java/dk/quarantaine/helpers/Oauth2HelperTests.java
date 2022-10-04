package dk.quarantaine.helpers;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

import dk.quarantaine.commons.dto.ClientIDAndSecret;
import dk.quarantaine.api.application.dto.OauthRequestDTO;
import dk.quarantaine.api.application.helpers.Oauth2Helper;
import dk.quarantaine.commons.exceptions.FormatException;

@ExtendWith(MockitoExtension.class)
public class Oauth2HelperTests {
    

    @ParameterizedTest
    @ValueSource(strings = {"", "malformedHeader", "Basic AlmostCorrect", "not correct"})
    public void decodeAuthorizationHeader_ThrowsExceptionMalformedHeader(String wrongheader){
        try{
            FormatException exception = assertThrows(FormatException.class,() -> Oauth2Helper.decodeAuthorizationHeader(wrongheader));       
            assertEquals("Header not valid", exception.getMessage());
        }
        catch(Exception e){
            fail();
        }
    }

    @Test
    public void decodeAuthorizationHeader_ValidHeader(){
        //SETUP
        String expectedClientID = "clientID";
        String expectedClientSecret = "clientSecret";
        String validHeader = "basic Y2xpZW50SUQ6Y2xpZW50U2VjcmV0";
        
        try{

            //ACT
            ClientIDAndSecret actual = Oauth2Helper.decodeAuthorizationHeader(validHeader);

            
            
        //VERIFY
        assertEquals(expectedClientID, actual.getClientID());
        assertEquals(expectedClientSecret, actual.getClientSecret());
        

        }
        catch(Exception e){
            fail();
        }
        
    }


    @ParameterizedTest
    @ValueSource(strings = {"basic M3Z4UEZINU5UYmV0aGI2Z3JHJE0=", "basic M3Z4UEZINU5UYmV0aGI2Z3JHJE06WDJLQiV6VypYRkZ0ZiFrWThqXmU6aCZUYlUmZTlVWkVnSnlpdkhtRkA="})
    public void decodeAuthorizationHeader_ValidHeaderButTooManyEntries(String tooManyEntries){
        try{
            FormatException exception = assertThrows(FormatException.class,() -> Oauth2Helper.decodeAuthorizationHeader(tooManyEntries));       
            assertEquals("clientid or clientsecret ambiguous", exception.getMessage());
        }
        catch(Exception e){
            fail();
        }
        
    }


//        //"grant_type=password&username=dark&password=dark"

    @ParameterizedTest
    @ValueSource(strings = {"","Not valid","Still not valid","closeTo=valid","almostvalid=pasword&username=butNotQuite&password=nograntType"})
    public void decodeOauthBodyRequest_badFormat(String nonValidFormat){
        try{
            FormatException exception = assertThrows(FormatException.class,() -> Oauth2Helper.decodeOauthBodyRequest(nonValidFormat));       
            assertEquals("Body is not valid format", exception.getMessage());
        }
        catch(Exception e){
            fail();
        }
    }


    @ParameterizedTest
    @ValueSource(strings = {"grant_type=pasword&username=notValidGrant&password=grant","grant_type=nonValidGrant&username=wrongGrant&password=grant"})
    public void decodeOauthBodyRequest_unsupportedGrantType(String nonValidFormat){
        try{
            FormatException exception = assertThrows(FormatException.class,() -> Oauth2Helper.decodeOauthBodyRequest(nonValidFormat));       
            assertEquals("grant_type not valid or implmented", exception.getMessage());
        }
        catch(Exception e){
            fail();
        }
    }

    @Test
    public void decodeOauthBodyRequest_decodeUrlEncodings(){
        //SETUP
        String containsUrlEncoded = "grant_type=password&username=notValidGrant%26&password=grant%26%26";
        String expectedPassword = "grant&&";
        String expectedUserName = "notValidGrant&";

        try{
            //ACT
            OauthRequestDTO actual = Oauth2Helper.decodeOauthBodyRequest(containsUrlEncoded);

            
            //VERIFY
            assertEquals(expectedPassword, actual.getPassword());
            assertEquals(expectedUserName, actual.getUsername());

        }
        catch(Exception e){
            fail();
        }


    }

    @Test
    public void decodeOauthBodyRequest_success(){
        //SETUP
        String containsUrlEncoded = "grant_type=password&username=hitchhiker&password=whatis42";
        String expectedPassword = "whatis42";
        String expectedUserName = "hitchhiker";

        try{
            //ACT
            OauthRequestDTO actual = Oauth2Helper.decodeOauthBodyRequest(containsUrlEncoded);

            
            //VERIFY
            assertEquals("password", actual.getGrant_type());
            assertEquals(expectedPassword, actual.getPassword());
            assertEquals(expectedUserName, actual.getUsername());

        }
        catch(Exception e){
            fail();
        }


    }
}
