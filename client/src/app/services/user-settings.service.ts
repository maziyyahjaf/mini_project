import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { UserSettings } from '../models/user.model';
import { lastValueFrom } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class UserSettingsService {

  constructor() { }

  private userSettingsUrl = "/api/user/settings";
  http = inject(HttpClient);

  fetchUserSettings(){
    return this.http.get<UserSettings>(`${this.userSettingsUrl}`);
  }
}
