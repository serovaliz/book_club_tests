package tests.lombok.registration;

import lombok.Data;

@Data
public class RegistrationTestResponseLombokModel {
    Integer id;
    String username;
    String firstname;
    String lastName;
    String email;
    String remoteAddr;

}
