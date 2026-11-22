package tests;

import api.AuthApiClient;
import io.qameta.allure.*;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tests.lombok.login.LoginBodyModel;
import tests.lombok.logout.LogoutEmptyJsonBodyModel;
import tests.lombok.logout.LogoutRequestBodyModel;
import tests.lombok.logout.LogoutResponseBodyModel;

import static io.qameta.allure.Allure.step;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.logout.LogoutSpec.*;
import static tests.testData.TestData.*;

@Feature("Logout tests")
public class LogoutTests extends TestBase {

    AuthApiClient authApi = new AuthApiClient();

    @Test
    @Owner("serovaliz")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("[API] Успешный logout пользователя")
    public void successfulLogoutTest() {
        LoginBodyModel loginData = new LoginBodyModel();
        loginData.setUsername(LOGOUT_USERNAME);
        loginData.setPassword(LOGOUT_PASSWORD);

        String refreshToken = authApi.getTokenSuccessfully(loginData).getRefresh();

        String logoutData = format("{\"refresh\": \"%s\"}", refreshToken);

        authApi.logout(logoutData)
                .spec(successfullLogoutSpec);
    }

    @Test
    @Owner("serovaliz")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("[API] Logout без токена")
    public void logoutWithoutTokenTest() {
        LogoutRequestBodyModel emptyData = new LogoutRequestBodyModel();

        LogoutEmptyJsonBodyModel logoutResponse = authApi.logout(emptyData)
                .spec(logoutWithoutTokenTestSpec)
                .extract()
                .as(LogoutEmptyJsonBodyModel.class);

        step("Проверка текста ошибки для поля refresh", () -> {
            assertThat(logoutResponse.getRefresh().get(0)).isEqualTo(FIELD_NULL_ERROR);
        });
    }

    @Test
    @Owner("serovaliz")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("[API] Logout с невалидным токеном")
    public void logoutWithInvalidTokenTest() {
        LogoutRequestBodyModel invalidData = new LogoutRequestBodyModel();
        invalidData.setRefresh(INVALID_REFRESH_TOKEN);

        LogoutResponseBodyModel logoutResponse = authApi.logout(invalidData)
                .spec(logoutWithInvalidTokenTestSpec)
                .extract()
                .as(LogoutResponseBodyModel.class);

        step("Проверка detail и code в ответе об ошибке", () -> {
            assertThat(logoutResponse.getDetail()).isEqualTo(INVALID_TOKEN_DETAIL);
            assertThat(logoutResponse.getCode()).isEqualTo(INVALID_TOKEN_CODE);
        });
    }
}