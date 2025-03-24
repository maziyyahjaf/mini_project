import { Component, inject, OnInit } from '@angular/core';
import { EmotionLogResponse } from '../../models/emotion.model';
import { ActivatedRoute, Router } from '@angular/router';
import { EmotionInsightsService } from '../../services/emotion-insights.service';

@Component({
  selector: 'app-daily-logs',
  standalone: false,
  templateUrl: './daily-logs.component.html',
  styleUrl: './daily-logs.component.css'
})
export class DailyLogsComponent implements OnInit {
deleteLog(arg0: number) {
throw new Error('Method not implemented.');
}
  
  todaysLogs: EmotionLogResponse[] = [];
  router = inject(Router);
  route = inject(ActivatedRoute);
  isLoading = true;
  dateString!: string;
  error: string | null = null;

  insightsService = inject(EmotionInsightsService);
  
  ngOnInit(): void {
    this.route.queryParams.subscribe(queryParams => {
      this.dateString = queryParams['date']
    });
    this.insightsService.getEmotionLogsByDate(this.dateString).subscribe({
      next: (data) => {
          this.todaysLogs = data;
          this.isLoading = false;
      },
      error: (error) => {
        this.error = 'Failed to load daily logs';
        this.isLoading = false;
        console.error(error);
      }
    });
  } 

  

}
