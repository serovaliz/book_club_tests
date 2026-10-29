package api;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import tests.lombok.login.LoginBodyModel;
import tests.lombok.login.ResponseBodyModel;

import static io.restassured.RestAssured.given;
import static specs.login.LoginSpec.loginRequestSpec;
import static specs.login.LoginSpec.successfulLoginResponseSpec;
import static specs.logout.LogoutSpec.logoutRequestSpec;

public class AuthApiClient {

    @Step("Отправка запроса на получение токена")
    public ValidatableResponse getToken(Object body) {
        return given(loginRequestSpec)
                .body(body)
                .when()
                .post("/auth/token/")
                .then();
    }

    @Step("Успешная авторизация и получение токенов")
    public ResponseBodyModel getTokenSuccessfully(LoginBodyModel loginData) {
        return getToken(loginData)
                .spec(successfulLoginResponseSpec)
                .extract()
                .as(ResponseBodyModel.class);
    }

    @Step("Отправка запроса на logout")
    public ValidatableResponse logout(Object body) {
        return given(logoutRequestSpec)
                .body(body)
                .when()
                .post("/auth/logout/")
                .then();
    }
}
