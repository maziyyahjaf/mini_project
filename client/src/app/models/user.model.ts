export interface UserRegistrationPayload {
    firebaseUid: string | undefined;
    name: string;
    email: string;
    deviceId: string;
    timezone: string;
}

export interface UserLoginPayload {
    name: string;
    email: string;
    deviceId: string;
    pairedDeviceId: string;
    isPaired: boolean;
    telegramChatId: string;
    telegramLinkCode: string;
    timezone: string;
    pairing_id: string;
}

export interface UserSettings {
    pairingStatus: boolean;
    telegramChatId: string | null;
    telegramLinkCode: string | null;
}