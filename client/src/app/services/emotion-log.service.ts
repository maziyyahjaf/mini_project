import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { EmotionLog } from '../models/emotion.model';

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


}
