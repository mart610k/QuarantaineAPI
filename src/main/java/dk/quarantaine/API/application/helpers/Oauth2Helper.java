package dk.quarantaine.api.application.helpers;

import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import dk.quarantaine.api.application.dto.OauthRequestDTO;
import dk.quarantaine.commons.dto.ClientIDAndSecret;
import dk.quarantaine.commons.exceptions.FormatException;

public class Oauth2Helper {

    public static ClientIDAndSecret decodeAuthorizationHeader(String authorizationHeader) throws FormatException{
        String decodedString ;
        if(!authorizationHeader.toLowerCase().startsWith("basic ")){
            throw new FormatException("Header not valid");
        }
        String clientIdSecret = authorizationHeader.substring("basci ".length());
        try{
            byte[] decodedBytes = Base64.getDecoder().decode(clientIdSecret);
            decodedString= new String(decodedBytes);
            
        }
        catch(IllegalArgumentException iae){
            throw new FormatException("Header not valid");
        }

        String[] array = decodedString.split(":");
        
        if(array == null  || array != null &&  array.length != 2){
            throw new FormatException("clientid or clientsecret ambiguous");
        }

        ClientIDAndSecret clientidAndsecret = new ClientIDAndSecret();
        
        clientidAndsecret.setClientID(array[0]);
        clientidAndsecret.setClientSecret(array[1]);

        return clientidAndsecret;
    }


    public static OauthRequestDTO decodeOauthBodyRequest(String authBodyRequest) throws FormatException{
        if(authBodyRequest == null){
            throw new FormatException("body Cant be null");
        }

        if(!authBodyRequest.toLowerCase().contains("grant_type")){
            throw new FormatException("Body is not valid format");
        }

        String[] elements = authBodyRequest.split("&");
        Map<String, String> map = new HashMap<>();

        for (String string : elements) {
            String[] values = string.split("=");
            if(values.length == 2){
                map.put(values[0],URLDecoder.decode(values[1], StandardCharsets.UTF_8));

            }
            else{
                //NOTE Something is way off. there should only be one "=" sign in this substring....
            }
        }

        if(!map.get("grant_type").equals("password")){
            throw new FormatException("grant_type not valid or implmented");
        }


        OauthRequestDTO oauthRequestDTO = new OauthRequestDTO();

        oauthRequestDTO.setGrant_type(map.get("grant_type"));
        oauthRequestDTO.setUsername(map.get("username"));
        oauthRequestDTO.setPassword(map.get("password"));

        return oauthRequestDTO;

    }

    public static byte[] convertUUIDToBinary(UUID uuid){
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }
    
}
