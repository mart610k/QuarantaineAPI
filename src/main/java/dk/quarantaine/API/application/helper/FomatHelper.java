package dk.quarantaine.api.application.helper;

public class FomatHelper {



    public static boolean validatePassword(String toValidate){
        return toValidate.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%#^*?&])[A-Za-z\\d@$!#%^*?&]{8,}$");
        
    }


    public static boolean validatePhoneNumber(String toValidate) {
        return (toValidate == null ? false : toValidate.matches("^[0-9]{8}$"));
    }


}
