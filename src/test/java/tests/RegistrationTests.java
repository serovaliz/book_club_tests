package tests;

import api.RegistrationApiClient;
import io.qameta.allure.*;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tests.lombok.registration.EmptyData400;
import tests.lombok.registration.InvalidJson400;
import tests.lombok.registration.RegistrationBodyLombockModel;
import tests.lombok.registration.RegistrationTestResponseLombokModel;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static specs.registration.RegistrationSpec.emptyOrInvalidDataResponseSpec;
import static specs.registration.RegistrationSpec.successfulRegistrationResponseSpec;
import static tests.testData.TestData.*;

@Feature("Registration tests")
public class RegistrationTests extends TestBase {

    RegistrationApiClient registrationApi = new RegistrationApiClient();
    String username;
    String password;

    @BeforeEach
    public void beforeEach() {
        username = getRandomRegistrationUsername();
        password = getRandomRegistrationPassword();
    }

    @Test
    @Owner("serovaliz")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("[API] Успешная регистрация пользователя в Book Club")
    public void successfulRegistrationTest() {
        RegistrationBodyLombockModel registrationData = new RegistrationBodyLombockModel();
        registrationData.setUsername(username);
        registrationData.setPassword(password);

        RegistrationTestResponseLombokModel registrationResponse = registrationApi.register(registrationData)
                .spec(successfulRegistrationResponseSpec)
                .extract()
                .as(RegistrationTestResponseLombokModel.class);

        step("Проверка полей созданного пользователя в ответе", () -> {
            assertThat(registrationResponse.getUsername()).isEqualTo(username);
            assertNotNull(registrationResponse.getId());
            assertNotNull(registrationResponse.getRemoteAddr());
            assertThat(registrationResponse.getFirstname()).isNullOrEmpty();
            assertThat(registrationResponse.getLastName()).isNullOrEmpty();
            assertThat(registrationResponse.getEmail()).isNullOrEmpty();
        });
    }

    @Test
    @Owner("serovaliz")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("[API] Проверка запроса без передачи данных пользователя")
    public void emptyDataTest() {
        EmptyData400 response = registrationApi.register(EMPTY_JSON_BODY)
                .spec(emptyOrInvalidDataResponseSpec)
                .extract()
                .as(EmptyData400.class);

        step("Проверка ошибок обязательности полей username и password", () -> {
            String actualUsername = response.getUsername().get(0);
            String actualPassword = response.getPassword().get(0);
            assertThat(actualUsername).isEqualTo(FIELD_REQUIRED_ERROR);
            assertThat(actualPassword).isEqualTo(FIELD_REQUIRED_ERROR);
        });
    }

    @Test
    @Owner("serovaliz")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("[API] Проверка регистрации с невалидным JSON")
    public void invalidJsonTest() {
        InvalidJson400 response = registrationApi.register(INVALID_JSON_BODY)
                .spec(emptyOrInvalidDataResponseSpec)
                .extract()
                .as(InvalidJson400.class);

        step("Проверка текста ошибки парсинга JSON", () -> {
            assertThat(response.getDetail()).isEqualTo(INVALID_JSON_ERROR);
        });
    }

    @Test
    @Owner("serovaliz")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("[API] Проверка регистрации с null-значениями полей")
    public void nullValuesTest() {
        EmptyData400 response = registrationApi.register(NULL_VALUES_JSON_BODY)
                .spec(emptyOrInvalidDataResponseSpec)
                .extract()
                .as(EmptyData400.class);

        step("Проверка ошибок о недопустимости null для username и password", () -> {
            String actualUsername = response.getUsername().get(0);
            String actualPassword = response.getPassword().get(0);
            assertThat(actualUsername).isEqualTo(FIELD_NULL_ERROR);
            assertThat(actualPassword).isEqualTo(FIELD_NULL_ERROR);
        });
    }
}