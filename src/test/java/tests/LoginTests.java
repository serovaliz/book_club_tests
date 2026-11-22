package tests;

import api.AuthApiClient;
import io.qameta.allure.*;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tests.lombok.login.*;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.login.LoginSpec.emptyUsernameOrPasswordReturns400Spec;
import static specs.login.LoginSpec.invalidUsernameReturns401Spec;
import static tests.testData.TestData.*;

@Feature("Login tests")
public class LoginTests extends TestBase {

    AuthApiClient authApi = new AuthApiClient();

    @Test
    @Owner("serovaliz")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("[API] Успешный логин пользователя")
    public void successfulTestLogin() {
        LoginBodyModel loginData = new LoginBodyModel();
        loginData.setUsername(USERNAME);
        loginData.setPassword(PASSWORD);

        ResponseBodyModel responseBodyModel = authApi.getTokenSuccessfully(loginData);

        step("Проверка access и refresh токенов в ответе", () -> {
            String actualAccess = responseBodyModel.getAccess();
            String actualRefresh = responseBodyModel.getRefresh();
            assertThat(actualAccess).startsWith(JWT_TOKEN_PREFIX);
            assertThat(actualRefresh).startsWith(JWT_TOKEN_PREFIX);
            assertThat(actualAccess).isNotEqualTo(actualRefresh);
        });
    }

    @Test
    @Owner("serovaliz")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("[API] Логин с пустым паролем возвращает 400")
    public void emptyPasswordReturns400() {
        LoginBodyModel loginData = new LoginBodyModel();
        loginData.setUsername(USERNAME);
        loginData.setPassword("");

        EmptyPasswordResponseModel responseModel = authApi.getToken(loginData)
                .spec(emptyUsernameOrPasswordReturns400Spec)
                .extract().as(EmptyPasswordResponseModel.class);

        step("Проверка текста ошибки для поля password", () -> {
            String actualPassword = responseModel.getPassword().get(0);
            assertThat(actualPassword).isEqualTo(FIELD_BLANK_ERROR);
        });
    }

    @Test
    @Owner("serovaliz")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("[API] Логин с пустым username возвращает 400")
    public void emptyUsernameReturns400() {
        LoginBodyModel loginData = new LoginBodyModel();
        loginData.setUsername("");
        loginData.setPassword(PASSWORD);

        EmptyLoginResponseModel responseModel = authApi.getToken(loginData)
                .spec(emptyUsernameOrPasswordReturns400Spec)
                .extract().as(EmptyLoginResponseModel.class);

        step("Проверка текста ошибки для поля username", () -> {
            String actualLogin = responseModel.getUsername().get(0);
            assertThat(actualLogin).isEqualTo(FIELD_BLANK_ERROR);
        });
    }

    @Test
    @Owner("serovaliz")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("[API] Логин с невалидным username возвращает 401")
    public void invalidUsernameReturns401() {
        LoginBodyModel loginData = new LoginBodyModel();
        loginData.setUsername(INVALID_USERNAME);
        loginData.setPassword(PASSWORD);

        InvalidUsernameOrPasswordResposeModel responseModel = authApi.getToken(loginData)
                .spec(invalidUsernameReturns401Spec)
                .extract().as(InvalidUsernameOrPasswordResposeModel.class);

        step("Проверка текста ошибки авторизации", () -> {
            String actualResult = responseModel.getDetail();
            assertThat(actualResult).isEqualTo(INVALID_LOGIN_ERROR);
        });
    }
}