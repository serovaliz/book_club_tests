package tests.lombok.clubs;

import lombok.Data;

import java.util.List;

@Data
public class ClubModel {
    Integer id;
    String bookTitle;
    String bookAuthors;
    Integer publicationYear;
    String description;
    String telegramChatLink;
    Integer owner;
    List<Integer> members;
    List<ClubReviewModel> reviews;
    String created;
    String modified;
}
