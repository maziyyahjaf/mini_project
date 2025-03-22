package com.example.maziyyah.light_touch.light_touch.controllers;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.maziyyah.light_touch.light_touch.models.ApiResponse;
import com.example.maziyyah.light_touch.light_touch.models.User;
import com.example.maziyyah.light_touch.light_touch.services.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

@RestController
@RequestMapping("/api")
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/login")
    public ResponseEntity<?> loginUser(@RequestHeader("Authorization") String authHeader) {
        try {
            // need to get details based on firebase uid

            logger.info("authHeader {}", authHeader);
            String token = authHeader.replace("Bearer ", "");
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
            String firebaseUid = decodedToken.getUid();

            logger.info("firebase uid from token: {}", firebaseUid);
            Optional<User> userOpt = userService.getUserDetailsByFirebaseUid(firebaseUid);

            if(userOpt.isEmpty()) {
                logger.error("No user details for firebase uid: {}", firebaseUid);
                return ResponseEntity.badRequest().body("No user details found?");
            }

            User user = userOpt.get();

            return ResponseEntity.ok().body(user);

        } catch (FirebaseAuthException ex) {
            logger.error("Firebase authentication error", ex);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse("error", "Invalid Token"));

        } catch (Exception ex) {
            logger.info("error {}, {}", ex.getCause(), ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("error", "Login failed: " + ex.getMessage()));
        }
    }
}
