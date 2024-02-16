package org.example.requestmodels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class UserData {
    private String username;
    private String email;
    private String password;

    public UserData(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public UserData() {
        this.username = null;
        this.email = null;
        this.password = null;
    }

    @JsonCreator
    public static UserData create(@JsonProperty("username") String username,
                                  @JsonProperty("email") String email,
                                  @JsonProperty("password") String password) {
        return new UserData(username, email, password);
    }

    @JsonProperty("username")
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonProperty("email")
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("password")
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserData userData = (UserData) o;
        return Objects.equals(username, userData.username) && Objects.equals(email, userData.email) && Objects.equals(password, userData.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, email, password);
    }

    @Override
    public String toString() {
        return "UserData{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
