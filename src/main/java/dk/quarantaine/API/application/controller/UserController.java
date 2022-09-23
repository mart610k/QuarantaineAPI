package dk.quarantaine.api.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import dk.quarantaine.commons.dto.RegisterUserDTO;
import dk.quarantaine.commons.dto.ExceptionMessage;
import dk.quarantaine.commons.exceptions.FormatException;
import dk.quarantaine.commons.exceptions.ObjectExistsException;
import lombok.extern.log4j.Log4j2;
import dk.quarantaine.api.application.logic.UserLogic;

@RestController("")
@Log4j2
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
        //THERE must be a better way to acheive what i want here.... -MBA
        catch(FormatException fe){
            ExceptionMessage em = new ExceptionMessage();

            em.setData(fe.getData());
            em.setField(fe.getField());

            em.setMessage(fe.getMessage());

            log.info(fe.getLocalizedMessage());
            if(log.isInfoEnabled()){
                fe.printStackTrace();
            }

            return new ResponseEntity<>(em, HttpStatus.BAD_REQUEST);
        }
        catch(ObjectExistsException oee){
            ExceptionMessage em = new ExceptionMessage();

            em.setData(registerUser.getUsername());
            em.setField("Username");

            em.setMessage("User already existing");

            log.info(oee.getLocalizedMessage());
            if(log.isInfoEnabled()){
                oee.printStackTrace();
            }

            return new ResponseEntity<>(em, HttpStatus.BAD_REQUEST);
        }
        catch(Exception e)
        {
            log.error(e.getLocalizedMessage());
            if(log.isErrorEnabled()){
                e.printStackTrace();
            }
            ExceptionMessage em = new ExceptionMessage();
            em.setData("Unknown data");
            em.setField("Unknown field");

            em.setMessage("Unknown error");
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);

        }
    }
}