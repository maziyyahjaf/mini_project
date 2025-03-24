import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { EmotionLog, EmotionLogResponse, EmotionLogUpdate } from '../models/emotion.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EmotionLogService {

  constructor() { }

  // send form data info to backend to save the 
  private baseUrl = "/api/emotions/log";
  http = inject(HttpClient);


  saveEmotionLog(emotionLog: EmotionLog) {
    return this.http.post('/api/emotions/log', emotionLog);
  }

  updateEmotionLog(updatedLog: EmotionLogResponse): Observable<EmotionLogResponse>  {
    // need to transform emotion log from response to emotionlog??
    const payload: EmotionLogUpdate = {
      deviceId: localStorage.getItem('deviceId') || "",
      emotionLogId: updatedLog.emotionLogId,
      emotion: updatedLog.emotion,
      intensity: updatedLog.intensity,
      //timestamp: new Date().toISOString(),
      timestamp: updatedLog.timestamp, // Use the original timestamp
      sendToDevice: updatedLog.sendToDevice,
      notes: updatedLog.notes
    }
    return this.http.put<EmotionLogResponse>(`/api/emotions/log/${updatedLog.emotionLogId}`, payload);
  }

  


}
