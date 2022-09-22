package dk.quarantaine.api.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import dk.quarantaine.api.application.dto.RegisterUserDTO;
import dk.quarantaine.api.application.logic.UserLogic;

@RestController("")
public class UserController {
    
    @Autowired
    UserLogic userLogic;

    

    @RequestMapping(value = "register", method = RequestMethod.POST)
    public ResponseEntity<?> RegisterUser(@RequestBody RegisterUserDTO registerUser)
    {
        try{
            userLogic.registerUser(registerUser);
            return new ResponseEntity<>(HttpStatus.OK);

        }
        catch(Exception e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }
    }
}