package tests.lombok.clubs;

import lombok.Data;

@Data
public class ClubCreateBodyModel {
    String bookTitle;
    String bookAuthors;
    Integer publicationYear;
    String description;
    String telegramChatLink;
}
