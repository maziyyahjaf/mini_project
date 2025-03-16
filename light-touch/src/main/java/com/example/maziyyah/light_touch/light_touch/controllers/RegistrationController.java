package com.example.maziyyah.light_touch.light_touch.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.maziyyah.light_touch.light_touch.models.ApiResponse;
import com.example.maziyyah.light_touch.light_touch.models.RegistrationPayload;
import com.example.maziyyah.light_touch.light_touch.models.SuccesfulRegistrationResponse;
import com.example.maziyyah.light_touch.light_touch.services.UserService;
import com.example.maziyyah.light_touch.light_touch.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

@RestController
@RequestMapping("/api")
public class RegistrationController {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestHeader("Authorization") String authHeader, 
                                                @RequestBody String registrationPayload) {

        try {
            // validate device id
            logger.info("authHeader {}", authHeader);
            String token = authHeader.replace("Bearer ", "");
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
            String firebaseUid = decodedToken.getUid();

            //register user
            // pass this firebaseUid or 
            // need to return the telegram linking code
            logger.info("firebase uid received: {}", firebaseUid);
            RegistrationPayload payload = Utils.toRegistrationPayload(registrationPayload);
            logger.info("payload from front end: {}", payload);
            String telegramLinkingCode = userService.saveUser(payload);
            // throw error if something goes wrong in saving registration details??
            return ResponseEntity.status(HttpStatus.OK).body(new SuccesfulRegistrationResponse("valid", "Success registration", telegramLinkingCode));


        } catch (FirebaseAuthException ex) {
            logger.info("error {}, {}", ex.getCause(), ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse("error", "Invalid Token"));

        }
       
    }
}
