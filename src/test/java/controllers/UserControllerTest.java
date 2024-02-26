package controllers;

import org.example.User.User;
import org.example.User.UserRepository;
import org.example.controllers.UserController;
import org.example.requestmodels.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {
    private static UserRepository userRepository;
    private static UserController userController;

    @BeforeAll
    public static void setup() {
        //Mock repositories and initialize controller
        userRepository = Mockito.mock(UserRepository.class);
        userController = new UserController(userRepository);
        //Create mock data for testing
        User testUser = new User("testUser", "testuser@mail.com", "testPass");

        User boom = new User("boom", "valid@mail.com", "testValid");

        User valid = new User("valid", "valid@mail.com", "validPass");
        //Define specific situations for mocked repositories
        when(userRepository.findById("testUser")).thenReturn(Optional.of(testUser));
        when(userRepository.findById("boom")).thenReturn(Optional.empty());
        when(userRepository.findById("valid")).thenReturn(Optional.empty());
        doThrow(Exception.class).when(userRepository).save(boom);
    }

    @Test
    public void testCreateUserNullData() {
        ResponseEntity<String> r1 = userController.createUser(new UserData(null, null, null));
        assertEquals(HttpStatus.BAD_REQUEST, r1.getStatusCode());
        assertEquals("Corrupted input", r1.getBody());
    }

    @Test
    public void testCreateUserInvalidEmail() {
        ResponseEntity<String> r1 = userController.createUser(new UserData("test", "invalid", "testPass"));
        assertEquals(HttpStatus.BAD_REQUEST, r1.getStatusCode());
        assertEquals("Invalid email format", r1.getBody());
    }

    @Test
    public void testCreateUserInvalidPassword() {
        ResponseEntity<String> r1 = userController.createUser(new UserData("test", "valid@mail.com", "invalid"));
        assertEquals(HttpStatus.BAD_REQUEST, r1.getStatusCode());
        assertEquals("Invalid password format", r1.getBody());
    }

    @Test
    public void testCreateUserAlreadyExists() {
        ResponseEntity<String> r1 = userController.createUser(new UserData("testUser", "valid@mail.com", "testPass"));
        assertEquals(HttpStatus.CONFLICT, r1.getStatusCode());
        assertEquals("User with that username already exists", r1.getBody());
    }

    @Test
    public void testBoom() {
        UserData boom = new UserData("boom", "valid@mail.com", "testValid");
        ResponseEntity<String> r1 = userController.createUser(boom);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, r1.getStatusCode());
        assertEquals("Couldn't create user", r1.getBody());
    }

    @Test
    public void testOK() {
        UserData valid = new UserData("valid", "valid@mail.com", "validPass");
        ResponseEntity<String> r1 = userController.createUser(valid);
        assertEquals(HttpStatus.OK, r1.getStatusCode());
        assertEquals("User created successfully!", r1.getBody());
    }
}
