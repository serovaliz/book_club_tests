package tests;


import org.junit.jupiter.api.Test;
import tests.lombok.login.*;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.login.LoginSpec.*;


public class LoginTests extends TestBase {
    @Test
    public void successfulTestLogin() {

        LoginBodyModel loginData = new LoginBodyModel();
        loginData.setUsername("misha");
        loginData.setPassword("misha");

        ResponseBodyModel responseBodyModel = given(loginRequestSpec)
                .body(loginData)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successfulLoginResponseSpec)
                .extract().as(ResponseBodyModel.class);

        String expectedTokenPath = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
        String actualAccess = responseBodyModel.getAccess();
        String actualRefresh = responseBodyModel.getRefresh();
        assertThat(actualAccess).startsWith(expectedTokenPath);
        assertThat(actualRefresh).startsWith(expectedTokenPath);
        assertThat(actualAccess).isNotEqualTo(actualRefresh);

    }

    @Test
    public void emptyPasswordReturns400() {

        LoginBodyModel loginData = new LoginBodyModel();
        loginData.setUsername("misha");
        loginData.setPassword("");

        EmptyPasswordResponseModel responseModel = given(loginRequestSpec)
                .body(loginData)
                .when()
                .post("/auth/token/")
                .then()
                .spec(emptyUsernameOrPasswordReturns400Spec)
                .extract().as(EmptyPasswordResponseModel.class);
        String expectedError = "This field may not be blank.";
        String actualPassword = responseModel.getPassword().get(0);
        assertThat(actualPassword).isEqualTo(expectedError);

    }

    @Test
    public void emptyUsernameReturns400() {

        LoginBodyModel loginData = new LoginBodyModel();
        loginData.setUsername("");
        loginData.setPassword("misha");

        EmptyLoginResponseModel responseModel = given(loginRequestSpec)
                .body(loginData)
                .when()
                .post("/auth/token/")
                .then()
                .spec(emptyUsernameOrPasswordReturns400Spec)
                .extract().as(EmptyLoginResponseModel.class);
        String expectedError = "This field may not be blank.";
        String actualLogin = responseModel.getUsername().get(0);
        assertThat(actualLogin).isEqualTo(expectedError);
    }

    @Test
    public void invalidUsernameReturns401() {

        LoginBodyModel loginData = new LoginBodyModel();
        loginData.setUsername("#$#$^^&");
        loginData.setPassword("misha");

        InvalidUsernameOrPasswordResposeModel resposeModel = given(loginRequestSpec)
                .body(loginData)
                .when()
                .post("/auth/token/")
                .then()
                .spec(invalidUsernameReturns401Spec)
                .extract().as(InvalidUsernameOrPasswordResposeModel.class);
        String expectedError = "Invalid username or password.";
        String actualResult = resposeModel.getDetail();
        assertThat(actualResult).isEqualTo(expectedError);

    }
}
