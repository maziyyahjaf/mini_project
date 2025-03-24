import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { DashboardSnapshot, EmotionLogResponse } from '../models/emotion.model';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  constructor() { }
  
  private baseUrl = '/api/insights';
  private dashboardUrl = '/api/dashboard';
  private mqttURL = '/api/mqtt'
  http = inject(HttpClient);

  getLatestEmotionLogForToday(date: string): Observable<EmotionLogResponse> {
    const httpParams = new HttpParams().append("date", date);
    return this.http.get<EmotionLogResponse>(`${this.baseUrl}/today/latest`, {params: httpParams});
  }

  getDashboardSnapshot(date:string, pairingId: string): Observable<DashboardSnapshot> {
    const httpParams = new HttpParams()
                        .set("date", date)
                        .set("pairingId", pairingId);
    return this.http.get<DashboardSnapshot>(`${this.dashboardUrl}`, {params: httpParams});
  }

  sendHug(pairedDeviceId: string) {
    const partnerDeviceId = {
      pairedDeviceId: pairedDeviceId
    }
    return this.http.post(`${this.mqttURL}/send-hug`, partnerDeviceId)
  }
}
