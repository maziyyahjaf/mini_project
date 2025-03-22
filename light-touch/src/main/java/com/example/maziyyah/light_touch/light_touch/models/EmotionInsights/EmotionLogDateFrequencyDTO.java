package com.example.maziyyah.light_touch.light_touch.models.EmotionInsights;

import java.util.Date;

public class EmotionLogDateFrequencyDTO {
    private Date logDate;
    private String emotionName;
    private int frequenxy;

    
    public EmotionLogDateFrequencyDTO() {
    }
    
    public EmotionLogDateFrequencyDTO(Date logDate, String emotionName, int frequenxy) {
        this.logDate = logDate;
        this.emotionName = emotionName;
        this.frequenxy = frequenxy;
    }
    public Date getLogDate() {
        return logDate;
    }
    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }
    public String getEmotionName() {
        return emotionName;
    }
    public void setEmotionName(String emotionName) {
        this.emotionName = emotionName;
    }
    public int getFrequenxy() {
        return frequenxy;
    }
    public void setFrequenxy(int frequenxy) {
        this.frequenxy = frequenxy;
    }


    


}
