package com.example.maziyyah.light_touch.light_touch.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.maziyyah.light_touch.light_touch.exceptions.Devices.PairedDeviceIdNotFoundException;
import com.example.maziyyah.light_touch.light_touch.models.RegistrationPayload;
import com.example.maziyyah.light_touch.light_touch.repositories.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // linking code to link telegram bot
     public String generateLinkingCode() {
        String linkingCode = UUID.randomUUID().toString().substring(0, 6);
        return linkingCode; // send this to the front end
    }


    @Transactional
    public String saveUser(RegistrationPayload payload) {
        // extract device id from payload
        String deviceId = payload.getDeviceId();
    
        // update device availability to FALSE
        updateDeviceIdAvailability(deviceId);

        // find the paired device ID
        String pairedDeviceId = findPairedDeviceId(deviceId);

        // check if the paired device is registered (is_available = FALSE) ie: if pairedDeviceAvailabilityStatus is true -> means not registered yet
        Boolean pairedDeviceAvailabilityStatus = checkPairedDeviceAvailabilityStatus(pairedDeviceId);
       
        // determine if the paired device is registered
        Boolean isPairedStatus = false;
        if (pairedDeviceAvailabilityStatus != null && !pairedDeviceAvailabilityStatus) {
            isPairedStatus = true;
            // trigger email to pairedDeviceUSer
            updatePairingStatus(deviceId, pairedDeviceId);
            // send email
        }

        String telegramLinkingCode = generateLinkingCode();

        // generate the pairing id
        // String pairingId = generatePairingId(deviceId, pairedDeviceId);
        userRepository.saveNewlyRegisteredUser(payload, pairedDeviceId, isPairedStatus, telegramLinkingCode);

        return telegramLinkingCode;

    }

    public void updateDeviceIdAvailability(String deviceId) {
        userRepository.updateDeviceIdAvailability(deviceId);
    }

    public String findPairedDeviceId(String deviceId) {
        Optional<String> pairedDeviceIdOpt = userRepository.findPairedDeviceIdForNewlyRegisteredDevice(deviceId);
        if (pairedDeviceIdOpt.isEmpty()) {
            throw new PairedDeviceIdNotFoundException("No paired device id found.");
        }
        String pairedDeviceId = pairedDeviceIdOpt.get();
        return pairedDeviceId;
    }

    public Boolean checkPairedDeviceAvailabilityStatus(String pairedDeviceId) {
        Optional<Boolean> availableStatusOpt = userRepository.checkPairedDeviceIdAvailabilityStatus(pairedDeviceId);
        if (availableStatusOpt.isEmpty()) {
            throw new PairedDeviceIdNotFoundException("No paired device id found.");
        }

        Boolean availableStatus = availableStatusOpt.get();
        return availableStatus;
    }

    public void updatePairingStatus(String deviceId, String pairedDeviceId) {
        userRepository.updatePairingStatus(deviceId, pairedDeviceId);
    }

   



   




   
    
}
