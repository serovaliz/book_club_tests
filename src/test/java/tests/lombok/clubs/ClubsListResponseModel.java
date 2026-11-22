package tests.lombok.clubs;

import lombok.Data;

import java.util.List;

@Data
public class ClubsListResponseModel {
    Integer count;
    String next;
    String previous;
    List<ClubModel> results;
}
