import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { EmotionWeeklyPattern } from '../models/emotion.model';

@Injectable({
  providedIn: 'root'
})
export class EmotionInsightsService {

  constructor() { }
  private baseUrl = 'api/insights';
  http = inject(HttpClient);

  getWeeklyPattern(): Observable<EmotionWeeklyPattern[]> {
    return this.http.get<EmotionWeeklyPattern[]>(`${this.baseUrl}/weekly`)
  }
  
  // need to implemeny get emotion logs by id

}
