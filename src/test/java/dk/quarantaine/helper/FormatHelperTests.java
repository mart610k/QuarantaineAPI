package dk.quarantaine.helper;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

import dk.quarantaine.api.application.helper.FomatHelper;


@ExtendWith(MockitoExtension.class)
public class FormatHelperTests {
    @ParameterizedTest
    @ValueSource(strings = {"","asdfwqet","r12sdawt","213452212","1242352","+4512324252","21 42 53 65","2e12ad35"})
    public void validatePhoneNumber_DosentValidate(String nonValidNumers){
        
        //ACT
        boolean actual = FomatHelper.validatePhoneNumber(nonValidNumers);

        //VERIFY
        assertFalse(actual);

    }

    @Test
    public void validatePhoneNumber_PhoneNumberNull(){
         //SETUP
         String nullLiteral = null;

         //ACT
         boolean actual = FomatHelper.validatePhoneNumber(nullLiteral);
 
         //VERIFY
         assertFalse(actual);
    }

    @Test
    public void validatePhoneNumber_ValidFormat(){
        //SETUP
        String validNumber = "52148634";

        //ACT
        boolean actual = FomatHelper.validatePhoneNumber(validNumber);

        //VERIFY
        assertTrue(actual);
    }


    @ParameterizedTest
    @ValueSource(strings= {"2%Y8NCpT%o7d$Ak%ZJm8","SJyLKP8UD@T4cETyBR5@","%9KuDTPs@#oNJUi@jUVq","Bwr5DG6%","jFt*39cj","cg$7Y$qG","G^Ro$v25","20BagerJensen%45"})
    public void validatePassword_ValidPassword(String validPasswords){
        //ACT
        boolean actual = FomatHelper.validatePassword(validPasswords);

        //VERIFY
        assertTrue(actual);
    }

    @ParameterizedTest
    @ValueSource(strings= {"3K@y","z&hrn28^b4r^8isrbuc%","BPDBYLD&VH2@LRKJ548C","sQioB&FXgpe$*FRvR*Sj","tvymWmqbcu6UHeJmy3PE","!&&^#79!59295%$33*^4","HxvLcGdGuCaENNUaGMZY","$^QW!JD!%GGFGD@SZTLG"})
    public void validatePassword_NotValid(String nonvalid){
        //ACT
        boolean actual = FomatHelper.validatePassword(nonvalid);

        //VERIFY
        assertFalse(actual);
    }

    @Test
    public void validatePassword_Null(){
        //ACT
        boolean actual = FomatHelper.validatePassword(null);

        //VERIFY
        assertFalse(actual);
    }

    @ParameterizedTest
    @ValueSource(strings= {"dsaiqw!\"", "NonVal!dUIsername2"})
    public void validateUsername_NotValid(String userNames){
        //ACT
        boolean actual = FomatHelper.validateUsername(userNames);


        //^(.*[A-Za-z\\d]){6,}$
        //VERIFY
        assertFalse(actual);
    }

    @ParameterizedTest
    @ValueSource(strings= {"UTKsH7vd","ETuvpfRU","empRKPBD","3YmRPGxH","RHNHvb3E"})
    public void validateUsername_Valid(String userNames){
        //ACT
        boolean actual = FomatHelper.validateUsername(userNames);

        //VERIFY
        assertTrue(actual);
    }

    @Test
    public void validateUsername_Null(){
        //ACT
        boolean actual = FomatHelper.validateUsername(null);

        //VERIFY
        assertFalse(actual);
    }
}
