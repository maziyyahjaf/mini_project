import { HttpClient, HttpHeaders } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Auth, createUserWithEmailAndPassword, signInWithEmailAndPassword, updateProfile, User } from "@angular/fire/auth";
import { BehaviorSubject, from, lastValueFrom, map, Observable } from "rxjs";
import { UserLoginPayload, UserRegistrationPayload } from "../models/user.model";
import { ApiResponse, LoginResponse, SuccessfulRegistrationResponse } from "../models/api-response.model";


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

            // create user in firebase
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
            
            // register user in backend
           const response = await lastValueFrom(this.http.post<SuccessfulRegistrationResponse>('/api/register', registrationPayload));
           
           // Store device and pairing info only after successful registration
            localStorage.setItem("deviceId", deviceId);
            localStorage.setItem("pairedDeviceId", response.pairedDeviceId);
            localStorage.setItem("pairingId", response.pairingId);  
            return response;
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

    async loginUser(email: string, password: string) {
        try {
            const userCredential = await signInWithEmailAndPassword(this.firebaseAuth, email, password);
            const response = await lastValueFrom(this.http.get<UserLoginPayload>('/api/login'));

            console.log(response);
            localStorage.setItem("name", response.name);
            localStorage.setItem("email", response.email);
            localStorage.setItem("deviceId", response.deviceId);
            localStorage.setItem("pairedDeviceId", response.pairedDeviceId);
            localStorage.setItem("isPaired", JSON.stringify(response.isPaired));
            localStorage.setItem("telegramChatID", response.telegramChatId);
            localStorage.setItem("telegramLinkCode", response.telegramLinkCode);
            localStorage.setItem("timezone", response.timezone);
            localStorage.setItem("pairing_id", response.pairing_id);
            return response;
        } catch (error: any) {
            console.error("Error logging in: ", error);
            if (error.error?.message) {
                throw new Error(error.error.message);
            } else if (error.message) {
                throw new Error(error.message);
            } else {
                throw new Error("Login failed.");
            }
        }
    }
}