package tests;


import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tests.lombok.registration.EmptyData400;
import tests.lombok.registration.InvalidJson400;
import tests.lombok.registration.RegistrationBodyLombockModel;
import tests.lombok.registration.RegistrationTestResponseLombokModel;

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
        registrationData.setUsername(username);
        registrationData.setPassword(password);


        RegistrationTestResponseLombokModel registrationResponse = given(registrationSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(successfulRegistrationResponseSpec)
                .extract()
                .as(RegistrationTestResponseLombokModel.class);
        String expectedUsername = registrationResponse.getUsername();
        assertThat(expectedUsername).isEqualTo(username);
        assertNotNull(registrationResponse.getId());
        assertNotNull(registrationResponse.getRemoteAddr());
        assertThat(registrationResponse.getFirstname()).isNullOrEmpty();
        assertThat(registrationResponse.getLastName()).isNullOrEmpty();
        assertThat(registrationResponse.getEmail()).isNullOrEmpty();


    }

    @Test
    @DisplayName("Проверка запроса без передачи данных пользователя")
    public void emptyDataTest() {
        String registrationData = "{}";

        EmptyData400 response = given(registrationSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(emptyOrInvalidDataResponseSpec)
                .extract()
                .as(EmptyData400.class);
        String expectedError = "This field is required.";
        String actualUsername = response.getUsername().get(0);
        String actualPassword = response.getPassword().get(0);
        assertThat(actualUsername).isEqualTo(expectedError);
        assertThat(actualPassword).isEqualTo(expectedError);

    }

    @Test
    @DisplayName("Проверка регистрации с невалидным JSON")
    public void invalidJsonTest() {
        String registrationData = "{ username: testuser, email: test@example.com ";

        InvalidJson400 response = given(registrationSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(emptyOrInvalidDataResponseSpec)
                .extract()
                .as(InvalidJson400.class);
        String expectedError = "JSON parse error - unexpected character: line 1 column 3 (char 2)";
        String actualDetail = response.getDetail();
        assertThat(actualDetail).isEqualTo(expectedError);


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

        EmptyData400 response = given(registrationSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(emptyOrInvalidDataResponseSpec)
                .extract()
                .as(EmptyData400.class);
        String expectedError = "This field may not be null.";
        String actualUsername = response.getUsername().get(0);
        String actualPassword = response.getPassword().get(0);
        assertThat(actualUsername).isEqualTo(expectedError);
        assertThat(actualPassword).isEqualTo(expectedError);
    }


}
