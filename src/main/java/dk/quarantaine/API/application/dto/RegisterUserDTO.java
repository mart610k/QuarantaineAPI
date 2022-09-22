package dk.quarantaine.api.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserDTO {
    String username;

    String password;

    String phoneNumber;

    String name;


    public RegisterUserDTO() {
    }
}
