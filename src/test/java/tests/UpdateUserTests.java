package tests;

import api.AuthApiClient;
import api.UserApiClient;
import io.qameta.allure.*;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tests.lombok.login.LoginBodyModel;
import tests.lombok.update.InvalidCredsResponseModel;
import tests.lombok.update.UpdateRequestModel;
import tests.lombok.update.UpdateResponseModel;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.update.UpdateSpec.updateResponseSpec;
import static specs.update.UpdateSpec.updateUserWithoutAuthReturns401Spec;
import static tests.testData.TestData.*;

@Feature("Update User tests")
public class UpdateUserTests extends TestBase {

    AuthApiClient authApi = new AuthApiClient();
    UserApiClient userApi = new UserApiClient();


    String firstName;
    String lastName;
    String email;

    @BeforeEach
    public void beforeEach() {

        firstName = getRandomFirstName();
        lastName = getRandomLastName();
        email = getRandomEmail();
    }

    @Test
    @Owner("serovaliz")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("[API] Успешное обновление данных пользователя")
    public void updateUserTest() {

        LoginBodyModel loginData = new LoginBodyModel();
        loginData.setUsername(USERNAME);
        loginData.setPassword(PASSWORD);
        String accessToken = authApi.getTokenSuccessfully(loginData).getAccess();


        UpdateRequestModel userData = new UpdateRequestModel();
        userData.setUsername(USERNAME);
        userData.setFirstName(firstName);
        userData.setLastName(lastName);
        userData.setEmail(email);


        UpdateResponseModel updateResponse = userApi.updateMe(userData, accessToken)
                .spec(updateResponseSpec)
                .extract()
                .as(UpdateResponseModel.class);


        step("Проверка обновлённых полей пользователя в ответе", () -> {
            assertThat(updateResponse.getUsername()).isEqualTo(USERNAME);
            assertThat(updateResponse.getEmail()).isEqualTo(email);
            assertThat(updateResponse.getLastName()).isEqualTo(lastName);
            assertThat(updateResponse.getFirstName()).isEqualTo(firstName);
            assertThat(updateResponse.getId()).isNotNull();
        });
    }

    @Test
    @Owner("serovaliz")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("[API] Обновление пользователя без авторизации возвращает 401")
    public void updateUserWithoutAuthReturns401() {
        UpdateRequestModel userData = new UpdateRequestModel();
        userData.setUsername(USERNAME);
        userData.setFirstName(firstName);
        userData.setLastName(lastName);
        userData.setEmail(email);

        InvalidCredsResponseModel invalidCredsResponseModel = userApi.updateMe(userData)
                .spec(updateUserWithoutAuthReturns401Spec)
                .extract()
                .as(InvalidCredsResponseModel.class);

        step("Проверка текста ошибки об отсутствии credentials", () -> {

            assertThat(invalidCredsResponseModel.getDetail()).isEqualTo(AUTH_CREDENTIALS_ERROR);
        });
    }
}