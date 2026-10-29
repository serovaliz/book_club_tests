package api;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static specs.update.UpdateSpec.updateUserTestSpec;

public class UserApiClient {

    public ValidatableResponse updateMe(Object body) {
        return updateMe(body, null);
    }


    public ValidatableResponse updateMe(Object body, String accessToken) {
        RequestSpecification request = given(updateUserTestSpec)
                .body(body);

        if (accessToken != null) {
            request.header("Authorization", "Bearer " + accessToken);
        }

        return request
                .when()
                .put("/users/me/")
                .then();
    }
}
