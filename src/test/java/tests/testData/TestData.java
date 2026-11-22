package tests.testData;

import net.datafaker.Faker;

public class TestData {

    public static final String USERNAME = "misha";
    public static final String PASSWORD = "misha";


    public static final String LOGOUT_USERNAME = "qaguru";
    public static final String LOGOUT_PASSWORD = "qaguru123";


    public static final String TELEGRAM_LINK = "https://t.me/qa_guru";
    public static final String INVALID_TELEGRAM_LINK = "me/qa_guru";
    public static final String REVIEW_TEXT = "Отличная супер книга!";
    public static final String PUBLICATION_YEAR = "2020";


    public static final String AUTH_CREDENTIALS_ERROR = "Authentication credentials were not provided.";
    public static final String FIELD_REQUIRED_ERROR = "This field is required.";
    public static final String FIELD_NULL_ERROR = "This field may not be null.";
    public static final String FIELD_BLANK_ERROR = "This field may not be blank.";
    public static final String INVALID_JSON_ERROR = "JSON parse error - unexpected character: line 1 column 3 (char 2)";
    public static final String INVALID_TOKEN_DETAIL = "Token is invalid";
    public static final String INVALID_TOKEN_CODE = "token_not_valid";
    public static final String INVALID_LOGIN_ERROR = "Invalid username or password.";
    public static final String TELEGRAM_LINK_ERROR = "Ссылка должна начинаться с https://t.me/";


    public static final String JWT_TOKEN_PREFIX = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";


    public static final String EMPTY_JSON_BODY = "{}";
    public static final String INVALID_JSON_BODY = "{ username: testuser, email: test@example.com ";
    public static final String NULL_VALUES_JSON_BODY = """
            {
              "username": null,
              "password": null
            }
            """;
    public static final String INVALID_REFRESH_TOKEN = "totally.invalid.token.123";
    public static final String INVALID_USERNAME = "#$#$^^&";


    public static final Faker faker = new Faker();


    public static String getRandomBookTitle() {
        return "Title is:" + faker.book().title();
    }

    public static String getRandomBookAuthor() {
        return "Author is:" + faker.book().author();
    }

    public static String getRandomBookPublisher() {
        return "Publisher is:" + faker.book().publisher();
    }


    public static String getRandomFirstName() {
        return faker.name().firstName();
    }

    public static String getRandomLastName() {
        return faker.name().lastName();
    }

    public static String getRandomEmail() {
        return faker.internet().emailAddress();
    }


    public static String getRandomRegistrationUsername() {
        return faker.name().firstName() + faker.number().digits(4);
    }

    public static String getRandomRegistrationPassword() {
        return faker.name().firstName();
    }
}