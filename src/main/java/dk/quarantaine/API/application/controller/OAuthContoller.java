package dk.quarantaine.api.application.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import dk.quarantaine.commons.dto.OauthRequestDTO;
import dk.quarantaine.api.application.helpers.Oauth2Helper;
import dk.quarantaine.api.application.logic.UserLogic;
import dk.quarantaine.commons.dto.ClientIDAndSecret;
import dk.quarantaine.commons.dto.ExceptionMessage;
import dk.quarantaine.commons.dto.OauthTokenResponseDTO;
import dk.quarantaine.commons.exceptions.FormatException;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("api/token")
@Log4j2
public class OAuthContoller {

    @Autowired
    UserLogic userLogic;

    /**
     * Generates an Access token for the user if valid
     * @param authorization HTTP authorization Header as a string
     * @param body HTTP Body as a string
     * @return Responds with a valid token or error based on if the user could log in
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> GenerateAccessToken(@RequestHeader String authorization,@RequestBody String body){
        //Decode Request into Useable values
       
        try{
            ClientIDAndSecret clientIDAndSecret = Oauth2Helper.decodeAuthorizationHeader(authorization);
            OauthRequestDTO oauthRequestDTO = Oauth2Helper.decodeOauthBodyRequest(body);

            OauthTokenResponseDTO bodyresponse = userLogic.authorizeUser(clientIDAndSecret, oauthRequestDTO);

            return new ResponseEntity<>(bodyresponse,HttpStatus.OK);
        }
        catch(FormatException fe){
            ExceptionMessage message = new ExceptionMessage();
            message.setMessage(fe.getMessage());
            log.info(message);

            return new ResponseEntity<>(message ,HttpStatus.BAD_REQUEST);


        }
        catch(Exception e){
            ExceptionMessage message = new ExceptionMessage();
            message.setMessage("Unknown exception");
            log.error(message);
            log.error(e.getCause());

            return new ResponseEntity<>(message ,HttpStatus.BAD_REQUEST);
        }
    }
}