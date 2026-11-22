package specs.review;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.filter.log.LogDetail.ALL;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static specs.BaseSpec.baseRequestSpec;

public class ReviewSpec {
    public static RequestSpecification reviewRequestSpec = baseRequestSpec;


    public static ResponseSpecification successfulPostReviewSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(201)
            .expectBody(matchesJsonSchemaInClasspath("schemas/review/successful_post_review_json_shema.json"))
            .build();


}
