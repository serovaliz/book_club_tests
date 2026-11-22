package tests;

import api.AuthApiClient;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.BasePage;
import pages.ClubPage;

import static tests.testData.TestData.*;

@Feature("Club tests")
public class ClubsTests extends TestBase {
    AuthApiClient authApi = new AuthApiClient();


    String bookTitle = getRandomBookTitle();
    String bookAuthor = getRandomBookAuthor();
    String bookPublisher = getRandomBookPublisher();

    @Test
    @Owner("serovaliz")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("[API + UI] Владелец не может выйти из клуба")
    public void cantLeaveClubAsOwner() {
        String authData = authApi.getAuthLocalStorage(USERNAME, PASSWORD);

        BasePage basePage = new BasePage();
        basePage.openWithAuth("/clubs/create", authData);

        ClubPage clubPage = new ClubPage();
        clubPage.fillBookTitle(bookTitle)
                .fillBookAuthors(bookAuthor)
                .fillPublicationYear(PUBLICATION_YEAR)
                .fillDescription(bookTitle + bookPublisher)
                .fillTelegramChatLink(TELEGRAM_LINK)
                .submitButtonClick();

        basePage.openClub()
                .searchClub(bookTitle)
                .openClubFromList(bookTitle);

        clubPage.pressLeaveButton()
                .assertOwnerCannotLeaveClub();
    }

    @Test
    @Owner("serovaliz")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("[API + UI] Ошибка при создании клуба с некорректной ссылкой на telegram")
    public void wrongTelegramChatLinkPutClubsTest() {
        String authData = authApi.getAuthLocalStorage(USERNAME, PASSWORD);

        BasePage basePage = new BasePage();
        basePage.openWithAuth("/clubs/create", authData);

        ClubPage clubPage = new ClubPage();
        clubPage.fillBookTitle(bookTitle)
                .fillBookAuthors(bookAuthor)
                .fillPublicationYear(PUBLICATION_YEAR)
                .fillDescription(bookTitle + bookPublisher)
                .fillTelegramChatLink(INVALID_TELEGRAM_LINK)
                .submitButtonClick()
                .assertTelegramChatError(TELEGRAM_LINK_ERROR);
    }

    @Test
    @Owner("serovaliz")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("[API + UI] Отображение формы создания клуба")
    public void createClubFormIsVisibleTest() {
        String authData = authApi.getAuthLocalStorage(USERNAME, PASSWORD);

        BasePage basePage = new BasePage();
        basePage.openWithAuth("/clubs/create", authData);

        ClubPage clubPage = new ClubPage();
        clubPage.assertCreateClubFormIsVisible();
    }
}