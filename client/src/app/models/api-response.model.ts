export interface ApiResponse {
    status: string;
    message: string;
}

export interface SuccessfulRegistrationResponse {
    status: string;
    message: string;
    teleLinkingCode: string;
}