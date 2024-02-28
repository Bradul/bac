package org.example.controllers;

import org.example.User.User;
import org.example.User.UserRepository;
import org.example.requestmodels.LoginData;
import org.example.requestmodels.UserData;
import org.example.validators.ValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

// TODO - For early testing purposes: Allow ALL requests from ALL origins. This should be removed in later stages of the project
@CrossOrigin(origins = "*")
@RestController
public class UserController {
    private UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Controller for creating a new user.
     *
     * @param userData Data for the new user - username, email, password
     * @return HTTP 200 - User created successfully
     *         HTTP 400 - Data is malformed, or in wrong format
     *         HTTP 409 - User already exists
     *         HTTP 500 - Database insertion failure
     */
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/user",
            consumes = {"application/json"}
    )
    public ResponseEntity<String> createUser(@RequestBody UserData userData) {
        if(userData == null ||
                userData.getUsername() == null || userData.getEmail() == null || userData.getPassword() == null)
            return new ResponseEntity<>("Corrupted input", HttpStatus.BAD_REQUEST);

        if(!ValidatorService.validateEmail(userData.getEmail()))
            return new ResponseEntity<>("Invalid email format", HttpStatus.BAD_REQUEST);

        if(!ValidatorService.validatePassword(userData.getPassword()))
            return new ResponseEntity<>("Invalid password format", HttpStatus.BAD_REQUEST);

        if(userRepository.findById(userData.getUsername()).isPresent())
            return new ResponseEntity<>("User with that username already exists", HttpStatus.CONFLICT);

        User user = new User(userData);
        try {
            userRepository.save(user);
        } catch (Exception e) {
            return new ResponseEntity<>("Couldn't create user", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("User created successfully!", HttpStatus.OK);
    }

    /**
     * Controller for logging in a user
     *
     * @param loginData Data for logging in - username and password
     * @return HTTP 200 - User logged in successfully
     *         HTTP 400 - Data is malformed, or in wrong format
     *         HTTP 401 - Unauthorised login operation => TODO
     *         HTTP 404 - User not found
     */
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/user/login",
            consumes = {"application/json"}
    )
    public ResponseEntity<String> loginUser(@RequestBody LoginData loginData) {
        if(loginData == null ||
                loginData.getUsername() == null || loginData.getPassword() == null)
            return new ResponseEntity<>("Corrupted input", HttpStatus.BAD_REQUEST);

        if(!ValidatorService.validatePassword(loginData.getPassword()))
            return new ResponseEntity<>("Invalid password format", HttpStatus.BAD_REQUEST);

        Optional<User> optionalUser = userRepository.findById(loginData.getUsername());
        if(optionalUser.isEmpty())
            return new ResponseEntity<>("Account does not exist or could not be found", HttpStatus.NOT_FOUND);

        User user = optionalUser.get();
        if(!user.getPassword().equals(loginData.getPassword()))
            return new ResponseEntity<>("Incorrect password", HttpStatus.UNAUTHORIZED);
        // TODO - Implement authentication security logic
        return new ResponseEntity<>("User " + loginData.getUsername() + " is now logged in", HttpStatus.OK);
    }

    /**
     * Controller method for getting a user
     *
     * @param username Username of user to be retrieved
     * @return HTTP 200 - User retrieved successfully
     *         HTTP 400 - Username is malformed
     *         HTTP 404 - User does not exist
     */
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/user/{username}"
    )
    public ResponseEntity<User> getUser(@PathVariable String username) {
        if(username == null || username.isEmpty())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Optional<User> optionalUser = userRepository.findById(username);
        return optionalUser.map(user -> new ResponseEntity<>(user, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Controller method for deleting a user
     *
     * @param username Username of user to be deleted
     * @return HTTP 200 - User deleted successfully
     *         HTTP 400 - Username is malformed
     *         HTTP 404 - User not found
     *         HTTP 500 - Database failure
     */
    @RequestMapping(
            method = RequestMethod.DELETE,
            value = "/user/deleteUser/{username}"
    )
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        if(username == null || username.isEmpty())
            return new ResponseEntity<>("Request is malformed", HttpStatus.BAD_REQUEST);
        Optional<User> optionalUser = userRepository.findById(username);
        if(optionalUser.isEmpty())
            return new ResponseEntity<>("User with that name does not exist", HttpStatus.NOT_FOUND);
        User toDelete = optionalUser.get();
        try {
            userRepository.delete(toDelete);
        } catch (Exception e) {
            return new ResponseEntity<>("Something went wrong\nCheck user deletion", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("User " + username + " deleted successfully", HttpStatus.OK);
    }
}
