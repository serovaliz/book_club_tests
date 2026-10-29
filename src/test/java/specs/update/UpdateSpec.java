package specs.update;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static allure.CustomAllureListener.withCustomTemplate;
import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.ALL;

public class UpdateSpec {
    public static RequestSpecification updateUserTestSpec = with()
            .filter(withCustomTemplate())
            .log().all()
            .contentType(ContentType.JSON)
            .basePath("/api/v1");

    public static ResponseSpecification updateResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(200)
            .build();
    public static ResponseSpecification updateUserWithoutAuthReturns401Spec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(401)
            .build();
}
