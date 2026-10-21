package specs.registration;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.ALL;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class RegistrationSpec {
    public static RequestSpecification registrationSpec = with()
            .log().all()
            .contentType(ContentType.JSON)
            .basePath("/api/v1");
    public static ResponseSpecification successfulRegistrationResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(201)
            .expectBody(matchesJsonSchemaInClasspath("schemas/registration/registration_response_shema.json"))
            .build();
    public static ResponseSpecification emptyOrInvalidDataResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(400)
            .build();


}
