import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { EmotionLogResponse, EmotionWeeklyPattern } from '../models/emotion.model';

@Injectable({
  providedIn: 'root'
})
export class EmotionInsightsService {

  constructor() { }
  private baseUrl = '/api/insights';
  http = inject(HttpClient);

  getWeeklyPattern(): Observable<EmotionWeeklyPattern[]> {
    return this.http.get<EmotionWeeklyPattern[]>(`${this.baseUrl}/weekly`);
  }
  
  // need to implement get emotion logs by id
  getEmotionLogsByIds(logIds: number[]): Observable<EmotionLogResponse[]> {
    return this.http.post<EmotionLogResponse[]>(`${this.baseUrl}/logs-by-ids`, {logIds});
  }

  getEmotionLogsByDate(date: string): Observable<EmotionLogResponse[]> {
    const httpParams = new HttpParams().append("date", date);
    return this.http.get<EmotionLogResponse[]>(`${this.baseUrl}/daily`, {params: httpParams});
  }


}
