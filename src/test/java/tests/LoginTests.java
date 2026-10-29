package tests;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tests.lombok.login.*;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.login.LoginSpec.*;


public class LoginTests extends TestBase {
    @Test
    @DisplayName("Успешный логин пользователя")
    public void successfulTestLogin() {

        LoginBodyModel loginData = new LoginBodyModel();
        loginData.setUsername("misha");
        loginData.setPassword("misha");

        ResponseBodyModel responseBodyModel = step("Отправка запроса на логин с валидными данными", () ->
                given(loginRequestSpec)
                        .body(loginData)
                        .when()
                        .post("/auth/token/")
                        .then()
                        .spec(successfulLoginResponseSpec)
                        .extract().as(ResponseBodyModel.class)
        );

        step("Проверка access и refresh токенов в ответе", () -> {
            String expectedTokenPath = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
            String actualAccess = responseBodyModel.getAccess();
            String actualRefresh = responseBodyModel.getRefresh();
            assertThat(actualAccess).startsWith(expectedTokenPath);
            assertThat(actualRefresh).startsWith(expectedTokenPath);
            assertThat(actualAccess).isNotEqualTo(actualRefresh);
        });
    }

    @Test
    @DisplayName("Логин с пустым паролем возвращает 400")
    public void emptyPasswordReturns400() {

        LoginBodyModel loginData = new LoginBodyModel();
        loginData.setUsername("misha");
        loginData.setPassword("");

        EmptyPasswordResponseModel responseModel = step("Отправка запроса на логин с пустым паролем", () ->
                given(loginRequestSpec)
                        .body(loginData)
                        .when()
                        .post("/auth/token/")
                        .then()
                        .spec(emptyUsernameOrPasswordReturns400Spec)
                        .extract().as(EmptyPasswordResponseModel.class)
        );

        step("Проверка текста ошибки для поля password", () -> {
            String expectedError = "This field may not be blank.";
            String actualPassword = responseModel.getPassword().get(0);
            assertThat(actualPassword).isEqualTo(expectedError);
        });
    }

    @Test
    @DisplayName("Логин с пустым username возвращает 400")
    public void emptyUsernameReturns400() {

        LoginBodyModel loginData = new LoginBodyModel();
        loginData.setUsername("");
        loginData.setPassword("misha");

        EmptyLoginResponseModel responseModel = step("Отправка запроса на логин с пустым username", () ->
                given(loginRequestSpec)
                        .body(loginData)
                        .when()
                        .post("/auth/token/")
                        .then()
                        .spec(emptyUsernameOrPasswordReturns400Spec)
                        .extract().as(EmptyLoginResponseModel.class)
        );

        step("Проверка текста ошибки для поля username", () -> {
            String expectedError = "This field may not be blank.";
            String actualLogin = responseModel.getUsername().get(0);
            assertThat(actualLogin).isEqualTo(expectedError);
        });
    }

    @Test
    @DisplayName("Логин с невалидным username возвращает 401")
    public void invalidUsernameReturns401() {

        LoginBodyModel loginData = new LoginBodyModel();
        loginData.setUsername("#$#$^^&");
        loginData.setPassword("misha");

        InvalidUsernameOrPasswordResposeModel resposeModel = step("Отправка запроса на логин с невалидным username", () ->
                given(loginRequestSpec)
                        .body(loginData)
                        .when()
                        .post("/auth/token/")
                        .then()
                        .spec(invalidUsernameReturns401Spec)
                        .extract().as(InvalidUsernameOrPasswordResposeModel.class)
        );

        step("Проверка текста ошибки авторизации", () -> {
            String expectedError = "Invalid username or password.";
            String actualResult = resposeModel.getDetail();
            assertThat(actualResult).isEqualTo(expectedError);
        });
    }
}
