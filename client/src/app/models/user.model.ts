export interface UserRegistrationPayload {
    firebaseUid: string | undefined;
    name: string;
    email: string;
    deviceId: string;
    timezone: string;
}