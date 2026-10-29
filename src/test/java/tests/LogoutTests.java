package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tests.lombok.login.LoginBodyModel;
import tests.lombok.logout.LogoutEmptyJsonBodyModel;
import tests.lombok.logout.LogoutRequestBodyModel;
import tests.lombok.logout.LogoutResponseBodyModel;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.login.LoginSpec.loginRequestSpec;
import static specs.login.LoginSpec.successfulLoginResponseSpec;
import static specs.logout.LogoutSpec.*;


public class LogoutTests extends TestBase {

    String username = "qaguru";
    String password = "qaguru123";

    @Test
    @DisplayName("Успешный logout пользователя")
    public void successfulLogoutTest() {
        LoginBodyModel loginData = new LoginBodyModel();
        loginData.setUsername(username);
        loginData.setPassword(password);

        String refreshToken = step("Авторизация и получение токена", () -> {
            return given(loginRequestSpec)
                    .body(loginData)
                    .when()
                    .post("/auth/token/")
                    .then()
                    .spec(successfulLoginResponseSpec)
                    .extract().path("refresh");


        });

        step("Отправка запроса с refresh токеном и проверка ответа", () -> {
            String logoutData = format("{\"refresh\": \"%s\"}", refreshToken);

            given(logoutRequestSpec)
                    .body(logoutData)
                    .when()
                    .post("/auth/logout/")
                    .then()
                    .spec(successfullLogoutSpec)
                    .extract();
        });

    }

    @Test
    @DisplayName("Logout без токена")
    public void logoutWithoutTokenTest() {
        LogoutRequestBodyModel emptyData = new LogoutRequestBodyModel();

        LogoutEmptyJsonBodyModel logoutResponse = step("Отправка запроса на logout без refresh токена", () ->
                given(logoutRequestSpec)
                        .body(emptyData)
                        .when()
                        .post("/auth/logout/")
                        .then()
                        .spec(logoutWithoutTokenTestSpec)
                        .extract()
                        .as(LogoutEmptyJsonBodyModel.class)
        );

        step("Проверка текста ошибки для поля refresh", () -> {
            String expectedRefresh = "This field may not be null.";
            assertThat(logoutResponse.getRefresh().get(0)).isEqualTo(expectedRefresh);
        });
    }

    @Test
    @DisplayName("Logout с невалидным токеном")
    public void logoutWithInvalidTokenTest() {
        LogoutRequestBodyModel invalidData = new LogoutRequestBodyModel();
        invalidData.setRefresh("totally.invalid.token.123");

        LogoutResponseBodyModel logoutResponse = step("Отправка запроса на logout с невалидным токеном", () ->
                given(logoutRequestSpec)
                        .body(invalidData)
                        .when()
                        .post("/auth/logout/")
                        .then()
                        .spec(logoutWithInvalidTokenTestSpec)
                        .extract()
                        .as(LogoutResponseBodyModel.class)
        );

        step("Проверка detail и code в ответе об ошибке", () -> {
            String expectedDetail = "Token is invalid";
            String expectedCode = "token_not_valid";
            assertThat(logoutResponse.getDetail()).isEqualTo(expectedDetail);
            assertThat(logoutResponse.getCode()).isEqualTo(expectedCode);
        });
    }
}
