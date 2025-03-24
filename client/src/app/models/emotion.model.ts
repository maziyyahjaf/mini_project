export interface EmotionLog {
    deviceId: string;
    emotion: string;
    intensity: number;
    timestamp: string;
    sendToDevice: boolean;
    notes?: string;

}


export interface Emotion {
    emotionName: string;
    emotionIconReference: string;
    displayOrder: number;
}

export interface EmotionWeeklyPattern {
    dayOfWeek: string;
    emotion: string;
    frequency: number;
    logIds: number[];
}

export interface EmotionLogResponse {
    emotionLogId: number;
    emotion: string;
    intensity: number;
    timestamp: string;
    sendToDevice: boolean;
    notes?: string;
}

export interface EmotionLogUpdate {
    deviceId: string;
    emotionLogId: number;
    emotion: string;
    intensity: number;
    timestamp: string;
    sendToDevice: boolean;
    notes?: string;
}

interface JournalEntry {
    entryId?: number;
    logId: number;
    content: string;
    timestamp: string;
    prompt?: string;
  }

export interface DashboardSnapshot {
    latestEmotionLog: EmotionLogResponse | null;
    hugInteraction: HugInteraction | null;
    partnerEmotion: PartnerEmotion | null;
}

export interface HugInteraction {
    lastSimultaneousHug?: string;
}

export interface PartnerEmotion {
    emotion: string;
    intensity: number;
    timestamp: string;
}