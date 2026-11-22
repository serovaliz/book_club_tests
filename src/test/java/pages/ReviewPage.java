package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ReviewPage extends BasePage {
    private final SelenideElement assessmentInput = $("#assessment");
    private final SelenideElement readPagesInput = $("#readPages");
    private final SelenideElement reviewInput = $("#review");
    private final SelenideElement submitReviewButton = $(".save-btn");
    private final ElementsCollection reviewContents = $$(".review-content");

    @Step("[UI] Заполнить и отправить отзыв")
    public ReviewPage addReview(String assessment, String readPages, String reviewText) {
        assessmentInput.setValue(assessment);
        readPagesInput.setValue(readPages);
        reviewInput.setValue(reviewText);
        submitReviewButton.click();
        return this;
    }

    @Step("[UI] Проверить, что отзыв '{expectedText}' отображается")
    public ReviewPage assertReviewIsVisible(String expectedText) {
        reviewContents.findBy(text(expectedText)).shouldBe(visible);
        return this;
    }
}
