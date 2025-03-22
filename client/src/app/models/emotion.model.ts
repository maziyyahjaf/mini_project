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

export interface EmotionWeeklyPatter {
    dayOfWeek: string;
    emotion: string;
    frequency: number;
    logIds: number[];
}