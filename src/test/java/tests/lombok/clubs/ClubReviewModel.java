package tests.lombok.clubs;

import lombok.Data;

@Data
public class ClubReviewModel {
    Integer id;
    Integer club;
    ClubReviewUserModel user;
    String review;
    Integer assessment;
    Integer readPages;
    String created;
    String modified;
}
