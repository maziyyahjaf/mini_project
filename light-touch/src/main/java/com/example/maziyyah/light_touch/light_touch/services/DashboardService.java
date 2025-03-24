package com.example.maziyyah.light_touch.light_touch.services;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.maziyyah.light_touch.light_touch.models.DashboardSnapshotDTO;
import com.example.maziyyah.light_touch.light_touch.models.EmotionLogDTO;
import com.example.maziyyah.light_touch.light_touch.models.HugInteractionDTO;
import com.example.maziyyah.light_touch.light_touch.models.PartnerEmotionDTO;
import com.example.maziyyah.light_touch.light_touch.repositories.HugEventRepository;
import com.example.maziyyah.light_touch.light_touch.repositories.UserRepository;

@Service
public class DashboardService {

    @Autowired
    private HugEventRepository hugEventRepository;

    @Autowired
    private EmotionInsightsService emotionInsightsService;

    @Autowired
    private UserRepository userRepository;


    public DashboardSnapshotDTO getDashboardSnapshot(String isoDateString, String pairing_id, String firebaseUid) {
        DashboardSnapshotDTO snapshot = new DashboardSnapshotDTO();

        
        HugInteractionDTO hugInteractionDTO = new HugInteractionDTO();
        Optional<LocalDateTime> lastSimultaneousHugOpt = hugEventRepository.getLastSimultaneousHug(pairing_id);
        lastSimultaneousHugOpt.ifPresent(hugInteractionDTO::setLastSimultaneousHug);
        snapshot.setHugInteraction(hugInteractionDTO);
        
        Optional<EmotionLogDTO> latestLogOpt = emotionInsightsService.getLatestEmotionLogForToday(isoDateString, firebaseUid);
        snapshot.setLatestEmotionLog(latestLogOpt.orElse(null));
        
        Optional<String> partnerUidOpt = userRepository.getPartnerFirebaseUid(pairing_id, firebaseUid);
        if (partnerUidOpt.isPresent()) {
            String partnerUid = partnerUidOpt.get();
            Optional<EmotionLogDTO> partnerLogOpt = emotionInsightsService.getLatestEmotionLogForToday(isoDateString, partnerUid);

           if (partnerLogOpt.isPresent()) {
            EmotionLogDTO partnerLog = partnerLogOpt.get();
            PartnerEmotionDTO dto = new PartnerEmotionDTO();
            dto.setEmotion(partnerLog.getEmotion());
            dto.setIntensity(partnerLog.getIntensity());
            dto.setTimestamp(partnerLog.getTimestamp());
            snapshot.setPartnerEmotion(dto);
           } else {
            snapshot.setPartnerEmotion(null);
           }
           
        } else {
            snapshot.setPartnerEmotion(null);
        }
        return snapshot;

    }
    
}
