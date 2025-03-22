export interface ApiResponse {
    status: string;
    message: string;
}

export interface SuccessfulRegistrationResponse {
    status: string;
    message: string;
    teleLinkingCode: string;
    pairedDeviceId: string;
    pairingId: string;

}

export interface LoginResponse {
    status: string;
    message: string;
    deviceId: string;
    pairedDeviceId: string;
    pairingId: string;
}