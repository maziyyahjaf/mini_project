import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class HugCountService {

  constructor() { }

  private baseUrl = "/api/hugs/count";
  http = inject(HttpClient)

  getDailyHugCount(pairingId: string): Observable<{count: number}> {
    return this.http.get<{count: number}>(`${this.baseUrl}/${pairingId}`);
  }

 
}
