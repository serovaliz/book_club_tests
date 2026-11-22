package tests;

import api.AuthApiClient;
import api.ClubsApiClient;
import api.ReviewApiClient;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.BasePage;
import pages.ReviewPage;
import tests.lombok.clubs.ClubCreateBodyModel;
import tests.lombok.clubs.ClubModel;
import tests.lombok.clubs.ClubReviewModel;
import tests.lombok.login.LoginBodyModel;
import tests.lombok.login.ResponseBodyModel;

import static tests.testData.TestData.*;


@Feature("Review tests")
public class ReviewsTests extends TestBase {
    AuthApiClient authApi = new AuthApiClient();
    ClubsApiClient clubsApi = new ClubsApiClient();
    ReviewApiClient reviewApi = new ReviewApiClient();

    @Test
    @Owner("serovaliz")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("[API + UI] Добавление ревью в клуб")
    void addReviewTest() {

        LoginBodyModel loginData = new LoginBodyModel();
        loginData.setUsername(USERNAME);
        loginData.setPassword(PASSWORD);
        ResponseBodyModel responseBodyModel = authApi.getTokenSuccessfully(loginData);
        String accessToken = responseBodyModel.getAccess();
        String authData = authApi.getAuthLocalStorage(USERNAME, PASSWORD);


        ClubCreateBodyModel clubBody = new ClubCreateBodyModel();
        clubBody.setBookTitle(getRandomBookTitle());
        clubBody.setBookAuthors(getRandomBookAuthor());
        clubBody.setPublicationYear(2020);
        clubBody.setDescription("Publisher:" + faker.book().publisher());
        clubBody.setTelegramChatLink(TELEGRAM_LINK);
        ClubModel createdClub = clubsApi.createClub(accessToken, clubBody);


        ClubReviewModel reviewBody = new ClubReviewModel();
        reviewBody.setClub(createdClub.getId());
        reviewBody.setAssessment(5);
        reviewBody.setReadPages(100);
        reviewBody.setReview(REVIEW_TEXT);
        reviewApi.successfulSendReview(reviewBody, accessToken);


        String clubUrl = "/clubs/" + createdClub.getId();
        BasePage basePage = new BasePage();
        basePage.openWithAuth(clubUrl, authData);

        ReviewPage reviewPage = new ReviewPage();
        reviewPage.assertReviewIsVisible(REVIEW_TEXT);
    }
}