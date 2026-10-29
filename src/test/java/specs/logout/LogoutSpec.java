package specs.logout;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static allure.CustomAllureListener.withCustomTemplate;
import static io.restassured.filter.log.LogDetail.ALL;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LogoutSpec {
    public static RequestSpecification logoutRequestSpec = new RequestSpecBuilder()
            .addFilter(withCustomTemplate())
            .setContentType(JSON)
            .setBasePath("/api/v1")
            .log(ALL)
            .build();

    public static ResponseSpecification successfullLogoutSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(200)
            .expectBody(equalTo("{}"))
            .build();
    public static ResponseSpecification logoutWithoutTokenTestSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(400)
            .expectBody("refresh", notNullValue())
            .build();
    public static ResponseSpecification logoutWithInvalidTokenTestSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(401)
            .expectBody("detail", notNullValue())
            .expectBody("code", notNullValue())
            .build();


}

