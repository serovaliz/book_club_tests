package tests;

import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tests.lombok.login.LoginBodyModel;
import tests.lombok.update.InvalidCredsResponseModel;
import tests.lombok.update.UpdateRequestModel;
import tests.lombok.update.UpdateResponseModel;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.login.LoginSpec.loginRequestSpec;
import static specs.login.LoginSpec.successfulLoginResponseSpec;
import static specs.update.UpdateSpec.*;

public class UpdateUserTests extends TestBase {
    String username = "misha";
    String password = "misha";
    String firstName;
    String lastName;
    String email;

    @BeforeEach
    public void beforeEach() {
        Faker faker = new Faker();
        firstName = faker.name().firstName();
        lastName = faker.name().lastName();
        email = faker.internet().emailAddress();

    }

    @Test
    public void updateUserTest() {

        LoginBodyModel loginData = new LoginBodyModel();
        loginData.setUsername(username);
        loginData.setPassword(password);

        String accessToken = given(loginRequestSpec)
                .body(loginData)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successfulLoginResponseSpec)
                .extract().path("access");

        UpdateRequestModel userData = new UpdateRequestModel();
        userData.setUsername(username);
        userData.setFirstName(firstName);
        userData.setLastName(lastName);
        userData.setEmail(email);

        UpdateResponseModel updateResponse = given(updateUserTestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(userData)
                .when()
                .put("/users/me/")
                .then()
                .spec(updateResponseSpec)
                .extract()
                .as(UpdateResponseModel.class);
        assertThat(updateResponse.getUsername()).isEqualTo(username);
        assertThat(updateResponse.getEmail()).isEqualTo(email);
        assertThat(updateResponse.getLastName()).isEqualTo(lastName);
        assertThat(updateResponse.getFirstName()).isEqualTo(firstName);
        assertThat(updateResponse.getId()).isNotNull();

    }

    @Test
    public void updateUserWithoutAuthReturns401() {

        UpdateRequestModel userData = new UpdateRequestModel();
        userData.setUsername(username);
        userData.setFirstName(firstName);
        userData.setLastName(lastName);
        userData.setEmail(email);

        InvalidCredsResponseModel invalidCredsResponceModel = given(updateUserTestSpec)
                .body(userData)
                .when()
                .put("/users/me/")
                .then()
                .spec(updateUserWithoutAuthReturns401Spec)
                .extract()
                .as(InvalidCredsResponseModel.class);

        String ExpectedDetail = "Authentication credentials were not provided.";
        assertThat(invalidCredsResponceModel.getDetail()).isEqualTo(ExpectedDetail);

    }
}
