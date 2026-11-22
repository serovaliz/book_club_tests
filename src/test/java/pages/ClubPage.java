package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.confirm;

public class ClubPage extends BasePage {

    private final SelenideElement leaveButton = $(".leave-btn");
    private final SelenideElement error = $(".error");


    private final SelenideElement bookTitleInput = $("#bookTitle");
    private final SelenideElement bookAuthorsInput = $("#bookAuthors");
    private final SelenideElement publicationYearInput = $("#publicationYear");
    private final SelenideElement descriptionInput = $("#description");
    private final SelenideElement telegramChatLinkInput = $("#telegramChatLink");
    private final SelenideElement telegramChatError = $(".form-group:has(#telegramChatLink) .error");
    private final SelenideElement submitButton = $(".submit-btn");



    @Step("[UI] Владелец не может покинуть клуб: подтверждение и сообщение об ошибке")
    public ClubPage assertOwnerCannotLeaveClub() {
        error.shouldHave(text("Не удалось покинуть клуб"));
        return this;
    }


    @Step("[UI] Покинуть клуб и подтвердить диалог")
    public ClubPage pressLeaveButton() {
        leaveButton.click();
        confirm();
        return this;
    }

    @Step("[UI] Заполнить Название книги")
    public ClubPage fillBookTitle(String value) {
        bookTitleInput.setValue(value);
        return this;
    }

    @Step("[UI] Заполнить Автор(ы) книги")
    public ClubPage fillBookAuthors(String value) {
        bookAuthorsInput.setValue(value);
        return this;
    }

    @Step("[UI] Заполнить Год выпуска")
    public ClubPage fillPublicationYear(String value) {
        publicationYearInput.setValue(value);
        return this;
    }

    @Step("[UI] Заполнить Описание книги")
    public ClubPage fillDescription(String value) {
        descriptionInput.setValue(value);
        return this;
    }

    @Step("[UI] Заполнить Ссылку на Telegram чат")
    public ClubPage fillTelegramChatLink(String value) {
        telegramChatLinkInput.setValue(value);
        return this;
    }


    @Step("[UI] Нажать на кнопку Создать Клуб")
    public ClubPage submitButtonClick() {
        submitButton.click();
        return this;
    }


    @Step("[UI] Проверить ошибку валидации ссылки на Telegram")
    public ClubPage assertTelegramChatError(String expectedText) {
        telegramChatError.shouldHave(text(expectedText));
        return this;
    }

    @Step("[UI] Проверить, что форма создания клуба отображается")
    public ClubPage assertCreateClubFormIsVisible() {
        bookTitleInput.shouldBe(visible);
        bookAuthorsInput.shouldBe(visible);
        telegramChatLinkInput.shouldBe(visible);

        return this;
    }
}