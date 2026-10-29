package tests;

import api.AuthApiClient;
import api.UserApiClient;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tests.lombok.login.LoginBodyModel;
import tests.lombok.update.InvalidCredsResponseModel;
import tests.lombok.update.UpdateRequestModel;
import tests.lombok.update.UpdateResponseModel;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.update.UpdateSpec.*;

public class UpdateUserTests extends TestBase {

    AuthApiClient authApi = new AuthApiClient();
    UserApiClient userApi = new UserApiClient();
    String username = "misha";
    String password = "misha";
    String firstName;
    String lastName;
    String email;

    @BeforeEach
    public void beforeEach() {
        Faker faker = new Faker();
        firstName = faker.name().firstName();
        lastName = faker.name().lastName();
        email = faker.internet().emailAddress();

    }

    @Test
    @DisplayName("Успешное обновление данных пользователя")
    public void updateUserTest() {

        LoginBodyModel loginData = new LoginBodyModel();
        loginData.setUsername(username);
        loginData.setPassword(password);

        String accessToken = step("Авторизация и получение access токена", () ->
                authApi.getTokenSuccessfully(loginData).getAccess()
        );

        UpdateRequestModel userData = new UpdateRequestModel();
        userData.setUsername(username);
        userData.setFirstName(firstName);
        userData.setLastName(lastName);
        userData.setEmail(email);

        UpdateResponseModel updateResponse = step("Отправка запроса на обновление данных пользователя", () ->
                userApi.updateMe(userData, accessToken)
                        .spec(updateResponseSpec)
                        .extract()
                        .as(UpdateResponseModel.class)
        );

        step("Проверка обновлённых полей пользователя в ответе", () -> {
            assertThat(updateResponse.getUsername()).isEqualTo(username);
            assertThat(updateResponse.getEmail()).isEqualTo(email);
            assertThat(updateResponse.getLastName()).isEqualTo(lastName);
            assertThat(updateResponse.getFirstName()).isEqualTo(firstName);
            assertThat(updateResponse.getId()).isNotNull();
        });
    }

    @Test
    @DisplayName("Обновление пользователя без авторизации возвращает 401")
    public void updateUserWithoutAuthReturns401() {

        UpdateRequestModel userData = new UpdateRequestModel();
        userData.setUsername(username);
        userData.setFirstName(firstName);
        userData.setLastName(lastName);
        userData.setEmail(email);

        InvalidCredsResponseModel invalidCredsResponceModel = step("Отправка запроса на обновление без авторизации", () ->
                userApi.updateMe(userData)
                        .spec(updateUserWithoutAuthReturns401Spec)
                        .extract()
                        .as(InvalidCredsResponseModel.class)
        );

        step("Проверка текста ошибки об отсутствии credentials", () -> {
            String ExpectedDetail = "Authentication credentials were not provided.";
            assertThat(invalidCredsResponceModel.getDetail()).isEqualTo(ExpectedDetail);
        });
    }
}
