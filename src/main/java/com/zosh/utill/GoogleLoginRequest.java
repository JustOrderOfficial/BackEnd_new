package com.zosh.utill;


public class GoogleLoginRequest {
    private String token;
    private GoogleUser user;

    // ✅ Getter and Setter for token
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public GoogleUser getUser() {
        return user;
    }

    public void setUser(GoogleUser user) {
        this.user = user;
    }

    // ✅ Static Inner Class with proper getters and setters
    public static class GoogleUser {
        private String email;
        private String name;
        private String picture;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }
    }
}
