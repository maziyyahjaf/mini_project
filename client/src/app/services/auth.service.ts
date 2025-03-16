import { HttpClient, HttpHeaders } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Auth, createUserWithEmailAndPassword, updateProfile } from "@angular/fire/auth";
import { from, lastValueFrom, Observable } from "rxjs";
import { UserRegistrationPayload } from "../models/user.model";
import { ApiResponse, SuccessfulRegistrationResponse } from "../models/api-response.model";


@Injectable({
    providedIn: 'root'
})
export class AuthService {

    firebaseAuth = inject(Auth);
    http = inject(HttpClient);

    async validateDevice(deviceId: string) {
        return lastValueFrom(this.http.get<ApiResponse>(`/api/validate-device?deviceId=${deviceId}`));
    }

    async registerUser(name: string, email: string, password: string, deviceId: string, timezone: string) {
        try {
            // validate device id
            const deviceValidationResponse = await this.validateDevice(deviceId);

            if (deviceValidationResponse.status !== "valid") {
                console.log(deviceValidationResponse.message)
                throw new Error("Invalid Device ID: " + deviceValidationResponse.message);
            }

            const userCredential = createUserWithEmailAndPassword(this.firebaseAuth, email,password);
            const firebaseUid = (await userCredential).user.uid;
            const idToken = await (await userCredential).user.getIdToken();

            const registrationPayload: UserRegistrationPayload  = {
                firebaseUid,
                name,
                email,
                deviceId,
                timezone
            };

            const httpHeaders = new HttpHeaders().set("Authorization", `Bearer ${idToken}`);
            return lastValueFrom(this.http.post<SuccessfulRegistrationResponse>('/api/register', registrationPayload))
        } catch (error: any) {
            console.error("Error registering User: ", error);
            // handle specific error types
            if (error.error?.message) {
                // backend error
                throw new Error(error.error.message);
            } else if (error.message) {
                // firebase or other errors
                throw new Error(error.message);
            } else {
                // generic error
                throw new Error("Registration failed.")
            }
            
        }
        
    }
}