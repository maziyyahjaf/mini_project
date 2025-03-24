package com.example.maziyyah.light_touch.light_touch.models;
import java.time.LocalDateTime;

public class HugInteractionDTO {
    
    private LocalDateTime lastSimultaneousHug;

    public LocalDateTime getLastSimultaneousHug() {
        return lastSimultaneousHug;
    }

    public void setLastSimultaneousHug(LocalDateTime lastSimultaneousHug) {
        this.lastSimultaneousHug = lastSimultaneousHug;
    }

    
}
