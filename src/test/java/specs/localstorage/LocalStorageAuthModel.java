package specs.localstorage;

import lombok.Data;

public class LocalStorageAuthModel {
    private BookClubAuth book_club_auth;

    @Data
    public static class BookClubAuth {
        private User user;
        private String accessToken;
        private String refreshToken;
        private boolean isAuthenticated;
    }

    @Data
    public static class User {
        private Integer id;
        private String username;
        private String firstName;
        private String lastName;
        private String email;
        private String remoteAddr;
    }
}
