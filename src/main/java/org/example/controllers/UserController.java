package org.example.controllers;

import org.example.User.User;
import org.example.User.UserRepository;
import org.example.requestmodels.LoginData;
import org.example.requestmodels.UserData;
import org.example.validators.ValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

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

        // TODO - Implement authentication security logic

        return new ResponseEntity<>("User " + loginData.getUsername() + " is now logged in", HttpStatus.OK);
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/user/{username}"
    )
    public ResponseEntity<User> getUser(@RequestBody String username) {
        if(username == null || username.isEmpty())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Optional<User> optionalUser = userRepository.findById(username);
        return optionalUser.map(user -> new ResponseEntity<>(user, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
