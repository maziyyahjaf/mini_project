package com.example.maziyyah.light_touch.light_touch.services;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.maziyyah.light_touch.light_touch.exceptions.Devices.PairedDeviceIdNotFoundException;
import com.example.maziyyah.light_touch.light_touch.models.RegistrationPayload;
import com.example.maziyyah.light_touch.light_touch.models.SuccesfulRegistrationResponse;
import com.example.maziyyah.light_touch.light_touch.models.User;
import com.example.maziyyah.light_touch.light_touch.repositories.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    MailService mailService;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // linking code to link telegram bot
     public String generateLinkingCode() {
        String linkingCode = UUID.randomUUID().toString().substring(0, 6);
        return linkingCode; // send this to the front end
    }


    @Transactional
    public SuccesfulRegistrationResponse saveUser(RegistrationPayload payload) {
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
            String partnerName = payload.getName();
            // need to get pairedDevice user email
            Optional<String> pairedDeviceIdUserEmailOpt = userRepository.fetchEmailForEarlierRegisteredUser(pairedDeviceId);
            if (pairedDeviceIdUserEmailOpt.isPresent()) {
                String pairedDeviceIdUserEmail = pairedDeviceIdUserEmailOpt.get();
                try {
                    mailService.sendPartnerJoinedEmail(pairedDeviceIdUserEmail, partnerName);
                } catch (Exception e) {
                    logger.error("Failed to send partner joined email", e);

                }

            } else {
                logger.warn("No email found for paired user with device ID: {}", pairedDeviceId);

            }

        }

        String telegramLinkingCode = generateLinkingCode();

        // generate the pairing id
        String pairingId = generatePairingId(deviceId, pairedDeviceId);

        
        userRepository.saveNewlyRegisteredUser(payload, pairedDeviceId, isPairedStatus, telegramLinkingCode, pairingId);

        SuccesfulRegistrationResponse successfulRegistrationResponse = new SuccesfulRegistrationResponse("valid",
                                                                                                        "successful registration", 
                                                                                                        telegramLinkingCode, 
                                                                                                        pairedDeviceId, 
                                                                                                        pairingId);

        return successfulRegistrationResponse;

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

    private String generatePairingId(String deviceId, String pairedDeviceId) {
        return deviceId.compareTo(pairedDeviceId) < 0 ? deviceId + "_" + pairedDeviceId : pairedDeviceId + "_" + deviceId;
    }

    public Optional<User> getUserDetailsByFirebaseUid(String firebaseUid) {
        return userRepository.getUserDetailsByFirebaseUid(firebaseUid);
    }

   



   




   
    
}
