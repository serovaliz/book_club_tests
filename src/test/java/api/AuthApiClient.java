package api;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import tests.lombok.login.LoginBodyModel;
import tests.lombok.login.ResponseBodyModel;
import tests.lombok.update.UpdateResponseModel;

import static io.restassured.RestAssured.given;
import static java.util.Objects.requireNonNullElse;
import static specs.login.LoginSpec.loginRequestSpec;
import static specs.login.LoginSpec.successfulLoginResponseSpec;
import static specs.logout.LogoutSpec.logoutRequestSpec;
import static specs.update.GetUserSpec.getResponseSpec;
import static specs.update.GetUserSpec.getUserInfoSpec;

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
    @Step("Получение данных по клиенту")
    public UpdateResponseModel getUser(String accessToken) {
        return given(getUserInfoSpec)
                .header("Authorization", "Bearer " + accessToken)
                .when()
                .get("/users/me/")
                .then()
                .spec(getResponseSpec)
                .extract()
                .as(UpdateResponseModel.class);
    }
    @Step("Получить готовую строку авторизации для localStorage")
    public String getAuthLocalStorage(String username, String password) {
        LoginBodyModel loginData = new LoginBodyModel();
        loginData.setUsername(username);
        loginData.setPassword(password);

        ResponseBodyModel responseBodyModel = getTokenSuccessfully(loginData);
        UpdateResponseModel userInfo = getUser(responseBodyModel.getAccess());

        return """
            {
              "user": {
                "id": %d,
                "username": "%s",
                "firstName": "%s",
                "lastName": "%s",
                "email": "%s",
                "remoteAddr": "%s"
              },
              "accessToken": "%s",
              "refreshToken": "%s",
              "isAuthenticated": true
            }
            """.formatted(
                userInfo.getId(),
                requireNonNullElse(userInfo.getUsername(), ""),
                requireNonNullElse(userInfo.getFirstName(), ""),
                requireNonNullElse(userInfo.getLastName(), ""),
                requireNonNullElse(userInfo.getEmail(), ""),
                requireNonNullElse(userInfo.getRemoteAddr(), ""),
                responseBodyModel.getAccess(),
                responseBodyModel.getRefresh()
        );
    }
}
