package tests;


import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tests.lombok.registration.EmptyData400;
import tests.lombok.registration.InvalidJson400;
import tests.lombok.registration.RegistrationBodyLombockModel;
import tests.lombok.registration.RegistrationTestResponseLombokModel;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static specs.registration.RegistrationSpec.*;


public class RegistrationTests extends TestBase {

    String username;
    String password;


    @BeforeEach
    public void beforeEach() {
        Faker faker = new Faker();
        username = faker.name().firstName();
        password = faker.name().firstName();

    }


    @Test
    @DisplayName("Успешная регистрация пользователя в Book Club")
    public void successfulRegistrationTest() {

        RegistrationBodyLombockModel registrationData = new RegistrationBodyLombockModel();
        registrationData.setUsername(username + "1996");
        registrationData.setPassword(password);

        RegistrationTestResponseLombokModel registrationResponse = step("Отправка запроса на регистрацию с валидными данными", () ->
                given(registrationSpec)
                        .body(registrationData)
                        .when()
                        .post("/users/register/")
                        .then()
                        .spec(successfulRegistrationResponseSpec)
                        .extract()
                        .as(RegistrationTestResponseLombokModel.class)
        );

        step("Проверка полей созданного пользователя в ответе", () -> {
            String expectedUsername = registrationResponse.getUsername();
            assertThat(expectedUsername).isEqualTo(username + "1996");
            assertNotNull(registrationResponse.getId());
            assertNotNull(registrationResponse.getRemoteAddr());
            assertThat(registrationResponse.getFirstname()).isNullOrEmpty();
            assertThat(registrationResponse.getLastName()).isNullOrEmpty();
            assertThat(registrationResponse.getEmail()).isNullOrEmpty();
        });
    }

    @Test
    @DisplayName("Проверка запроса без передачи данных пользователя")
    public void emptyDataTest() {
        String registrationData = "{}";

        EmptyData400 response = step("Отправка запроса на регистрацию без данных пользователя", () ->
                given(registrationSpec)
                        .body(registrationData)
                        .when()
                        .post("/users/register/")
                        .then()
                        .spec(emptyOrInvalidDataResponseSpec)
                        .extract()
                        .as(EmptyData400.class)
        );

        step("Проверка ошибок обязательности полей username и password", () -> {
            String expectedError = "This field is required.";
            String actualUsername = response.getUsername().get(0);
            String actualPassword = response.getPassword().get(0);
            assertThat(actualUsername).isEqualTo(expectedError);
            assertThat(actualPassword).isEqualTo(expectedError);
        });
    }

    @Test
    @DisplayName("Проверка регистрации с невалидным JSON")
    public void invalidJsonTest() {
        String registrationData = "{ username: testuser, email: test@example.com ";

        InvalidJson400 response = step("Отправка запроса на регистрацию с невалидным JSON", () ->
                given(registrationSpec)
                        .body(registrationData)
                        .when()
                        .post("/users/register/")
                        .then()
                        .spec(emptyOrInvalidDataResponseSpec)
                        .extract()
                        .as(InvalidJson400.class)
        );

        step("Проверка текста ошибки парсинга JSON", () -> {
            String expectedError = "JSON parse error - unexpected character: line 1 column 3 (char 2)";
            String actualDetail = response.getDetail();
            assertThat(actualDetail).isEqualTo(expectedError);
        });
    }

    @Test
    @DisplayName("Проверка регистрации с null-значениями полей")
    public void nullValuesTest() {
        String registrationData = """
                {
                  "username": null,
                  "password": null
                }
                """;

        EmptyData400 response = step("Отправка запроса на регистрацию с null-значениями полей", () ->
                given(registrationSpec)
                        .body(registrationData)
                        .when()
                        .post("/users/register/")
                        .then()
                        .spec(emptyOrInvalidDataResponseSpec)
                        .extract()
                        .as(EmptyData400.class)
        );

        step("Проверка ошибок о недопустимости null для username и password", () -> {
            String expectedError = "This field may not be null.";
            String actualUsername = response.getUsername().get(0);
            String actualPassword = response.getPassword().get(0);
            assertThat(actualUsername).isEqualTo(expectedError);
            assertThat(actualPassword).isEqualTo(expectedError);
        });
    }


}
