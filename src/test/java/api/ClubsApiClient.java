package api;

import io.qameta.allure.Step;
import tests.lombok.clubs.ClubCreateBodyModel;
import tests.lombok.clubs.ClubModel;
import tests.lombok.clubs.ClubsListResponseModel;

import static io.restassured.RestAssured.given;
import static specs.clubs.ClubsSpec.*;

public class ClubsApiClient {
    @Step("Получение списка клубов GET /clubs/")
    public ClubsListResponseModel getClubs() {
        return given(clubsRequestSpec)
                .when()
                .get("/clubs/")
                .then()
                .spec(successfulClubsListResponseSpec)
                .extract()
                .as(ClubsListResponseModel.class);
    }

    @Step("[API] Создание клуба POST /clubs/")
    public ClubModel createClub(String accessToken, ClubCreateBodyModel body) {
        return given(clubsRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(body)
                .when()
                .post("/clubs/")
                .then()
                .spec(successfulCreateClubResponseSpec)
                .extract()
                .as(ClubModel.class);
    }
}
