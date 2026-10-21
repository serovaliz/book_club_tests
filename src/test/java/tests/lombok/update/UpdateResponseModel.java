package tests.lombok.update;

import lombok.Data;

@Data
public class UpdateResponseModel {

    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private int id;
    private String remoteAddr;
}
