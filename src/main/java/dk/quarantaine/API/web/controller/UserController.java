     package dk.quarantaine.api.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import dk.quarantaine.dto.RegisterUserDTO;

@RestController("")
public class UserController {
    
    @RequestMapping(value = "register", method = RequestMethod.POST)
    public ResponseEntity<?> RegisterUser(@RequestBody RegisterUserDTO registerUser)
    {
        return new ResponseEntity<>(registerUser, HttpStatus.OK) ;
        

    }


}
