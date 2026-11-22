package api;

import io.qameta.allure.Step;
import tests.lombok.clubs.ClubReviewModel;


import static io.restassured.RestAssured.given;
import static specs.review.ReviewSpec.reviewRequestSpec;
import static specs.review.ReviewSpec.successfulPostReviewSpec;


public class ReviewApiClient {
    @Step("Успешное создание ревью и получение статуса")
    public ClubReviewModel successfulSendReview(ClubReviewModel postBody, String accessToken) {
        return given()
                .spec(reviewRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(postBody)
                .when()
                .post("clubs/reviews/")
                .then()
                .spec(successfulPostReviewSpec)
                .extract()
                .as(ClubReviewModel.class);
    }
}
