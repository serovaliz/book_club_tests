package tests;

import org.junit.jupiter.api.Test;
import tests.lombok.login.LoginBodyModel;
import tests.lombok.logout.LogoutEmptyJsonBodyModel;
import tests.lombok.logout.LogoutRequestBodyModel;
import tests.lombok.logout.LogoutResponseBodyModel;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.login.LoginSpec.loginRequestSpec;
import static specs.login.LoginSpec.successfulLoginResponseSpec;
import static specs.logout.LogoutSpec.*;


public class LogoutTests extends TestBase {

    String username = "qaguru";
    String password = "qaguru123";

    @Test
    public void successfulLogoutTest() {
        LoginBodyModel loginData = new LoginBodyModel();
        loginData.setUsername(username);
        loginData.setPassword(password);


        String refreshToken = given(loginRequestSpec)
                .body(loginData)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successfulLoginResponseSpec)
                .extract().path("refresh");

        String logoutData = format("{\"refresh\": \"%s\"}", refreshToken);

        given()
                .log().all()
                .contentType(JSON)
                .body(logoutData)
                .basePath("/api/v1")
                .when()
                .post("/auth/logout/")
                .then()
                .spec(successfullLogoutSpec)
                .extract();
    }

    @Test
    public void logoutWithoutTokenTest() {
        LogoutRequestBodyModel emptyData = new LogoutRequestBodyModel();

        LogoutEmptyJsonBodyModel logoutResponse = given(logoutRequestSpec)
                .body(emptyData)
                .when()
                .post("/auth/logout/")
                .then()
                .spec(logoutWithoutTokenTestSpec)
                .extract()
                .as(LogoutEmptyJsonBodyModel.class);
        String expectedRefresh = "This field may not be null.";
        assertThat(logoutResponse.getRefresh().get(0)).isEqualTo(expectedRefresh);


    }

    @Test
    public void logoutWithInvalidTokenTest() {
        LogoutRequestBodyModel invalidData = new LogoutRequestBodyModel();
        invalidData.setRefresh("totally.invalid.token.123");

        LogoutResponseBodyModel logoutResponse = given(logoutRequestSpec)
                .body(invalidData)
                .when()
                .post("/auth/logout/")
                .then()
                .spec(logoutWithInvalidTokenTestSpec)
                .extract()
                .as(LogoutResponseBodyModel.class);
        String expectedDetail = "Token is invalid";
        String expectedCode = "token_not_valid";
        assertThat(logoutResponse.getDetail()).isEqualTo(expectedDetail);
        assertThat(logoutResponse.getCode()).isEqualTo(expectedCode);
    }
}