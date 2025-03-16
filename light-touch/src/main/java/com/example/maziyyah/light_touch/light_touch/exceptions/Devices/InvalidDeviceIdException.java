package com.example.maziyyah.light_touch.light_touch.exceptions.Devices;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidDeviceIdException extends ResponseStatusException {

    public InvalidDeviceIdException(String reason) {
        super(HttpStatus.BAD_REQUEST, reason);
      
    }
    
}
