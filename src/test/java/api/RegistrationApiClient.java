package api;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static specs.registration.RegistrationSpec.registrationSpec;

public class RegistrationApiClient {


    @Step("Отправка запроса на регистрацию")
    public ValidatableResponse register(Object body) {
        return given(registrationSpec)
                .body(body)
                .when()
                .post("/users/register/")
                .then();
    }
}
