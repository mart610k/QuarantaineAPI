package dk.quarantaine.api.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import dk.quarantaine.api.application.data.TestService;
import dk.quarantaine.api.application.dto.RegisterUserDTO;

@RestController("")
public class UserController {
    
    @Autowired
    TestService testService;

    

    @RequestMapping(value = "register", method = RequestMethod.POST)
    public ResponseEntity<?> RegisterUser(@RequestBody RegisterUserDTO registerUser)
    {
        

        System.out.println(testService.TestConnection());

        return new ResponseEntity<>(registerUser, HttpStatus.OK) ;
        

    }


}
