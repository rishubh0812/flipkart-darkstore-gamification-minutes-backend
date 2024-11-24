package com.Hackathon.minutesGamification.controller;

import com.Hackathon.minutesGamification.database.entities.User;
import com.Hackathon.minutesGamification.services.AuthenticateService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/auth")
public class Authenticate {

    private AuthenticateService authenticateService;

    public Authenticate(AuthenticateService authenticateService) {
        this.authenticateService = authenticateService;
    }

    @PostMapping("/{userName}/{password}")
    public ResponseEntity<?> authenticate(@PathVariable String userName, @PathVariable String password) {
        if (authenticateService.userExists(userName)){
            User user = authenticateService.getUser(userName, password);
            if (user != null) {
                return ResponseEntity.status(HttpStatus.OK).body(user);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect Password.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }
}
