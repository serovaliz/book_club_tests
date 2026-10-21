package tests.lombok.login;

import lombok.Data;

import java.util.List;

@Data
public class EmptyLoginResponseModel {
    List<String> username;
}
