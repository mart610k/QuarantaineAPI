package dk.quarantaine.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserDTO {
    String username;

    String password;


    public RegisterUserDTO() {
    }
}
