package org.example.User;

import lombok.*;
import org.example.requestmodels.UserData;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.regex.Pattern;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Document(collection = "users")
public class User {
    @Id
    private String username;

    @Field("email")
    private String email;

    @Field("password")
    private String password;

    public User(UserData userData) {
        this.username = userData.getUsername();
        this.email = userData.getEmail();
        this.password = userData.getPassword();
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
