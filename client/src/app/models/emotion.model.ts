export interface EmotionLog {
    deviceId: string;
    emotion: string;
    intensity: number;
    timestamp: string;
    sendToDevice: boolean;

}


export interface Emotion {
    emotionName: string;
    emotionIconReference: string;
    displayOrder: number;
}