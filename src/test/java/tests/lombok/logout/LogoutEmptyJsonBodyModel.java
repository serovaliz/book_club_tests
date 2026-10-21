package tests.lombok.logout;

import lombok.Data;

import java.util.List;


@Data
public class LogoutEmptyJsonBodyModel {
    List<String> refresh;
}
