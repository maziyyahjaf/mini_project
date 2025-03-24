package com.example.maziyyah.light_touch.light_touch.models;

public class DashboardSnapshotDTO {

    private EmotionLogDTO latestEmotionLog;

    private HugInteractionDTO hugInteraction;

    private PartnerEmotionDTO partnerEmotion;
    

    public EmotionLogDTO getLatestEmotionLog() {
        return latestEmotionLog;
    }

    public void setLatestEmotionLog(EmotionLogDTO latestEmotionLog) {
        this.latestEmotionLog = latestEmotionLog;
    }

    public HugInteractionDTO getHugInteraction() {
        return hugInteraction;
    }

    public void setHugInteraction(HugInteractionDTO hugInteraction) {
        this.hugInteraction = hugInteraction;
    }

    public PartnerEmotionDTO getPartnerEmotion() {
        return partnerEmotion;
    }

    public void setPartnerEmotion(PartnerEmotionDTO partnerEmotion) {
        this.partnerEmotion = partnerEmotion;
    }

    
    
}
