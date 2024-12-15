package org.example.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// TODO - For early testing purposes: Allow ALL requests from ALL origins. This should be removed in later stages of the project
@CrossOrigin(origins = "*")
@RestController
public class CodeController {

    @Autowired
    public CodeController() {}

    // TODO - logic for filepath, language, code; Also make non static
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/code",
            consumes = {"application/json"}
    )
    public ResponseEntity<String> compileCode(@RequestBody String code) {
        if(code == null)
            return new ResponseEntity<>("Request is malformed", HttpStatus.BAD_REQUEST);
        try {
            String filePath = "";
            String language = "";
            //String output = CompilerService.compileCode(filePath, language, code);
            return new ResponseEntity<>("Accepted", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
