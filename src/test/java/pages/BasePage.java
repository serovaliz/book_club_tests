package pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.localStorage;


public class BasePage {
    private final SelenideElement clubsLink = $("[data-testid=clubs-link]");
    private final SelenideElement searchInput = $(".search-input");
    private final SelenideElement clubsList = $(".clubs-list");

    @Step("[UI] Открыть страницу с авторизацией")
    public BasePage openWithAuth(String pagePath, String authLocalStorage) {
        Selenide.open("/favicon.ico");
        localStorage().setItem("book_club_auth", authLocalStorage);
        Selenide.open(pagePath);
        return this;
    }

    @Step("[UI] Открыть список клубов")
    public BasePage openClub() {
        clubsLink.click();
        return this;
    }

    @Step("Найти клуб с {clubName}")
    public BasePage searchClub(String clubName) {
        searchInput.setValue(clubName).pressEnter();
        return this;
    }

    @Step("Открыть клуб с {clubName}")
    public BasePage openClubFromList(String clubName) {
        clubsList.$(byText(clubName))
                .parent().parent().$(".open-btn").click();
        return this;

    }

}