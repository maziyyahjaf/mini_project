import { Component, inject, OnInit } from '@angular/core';
import { EmotionLogResponse } from '../../models/emotion.model';
import { ActivatedRoute, Router } from '@angular/router';
import { EmotionInsightsService } from '../../services/emotion-insights.service';
import { EmotionLogService } from '../../services/emotion-log.service';

@Component({
  selector: 'app-daily-logs',
  standalone: false,
  templateUrl: './daily-logs.component.html',
  styleUrl: './daily-logs.component.css'
})
export class DailyLogsComponent implements OnInit {

  
  todaysLogs: EmotionLogResponse[] = [];
  router = inject(Router);
  route = inject(ActivatedRoute);
  isLoading = true;
  dateString!: string;
  error: string | null = null;
  moodEmoji: string = '';

  insightsService = inject(EmotionInsightsService);
  emotionLogService = inject(EmotionLogService);
  
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


  getEmojiForEmotion(emotion: string): string {
    const map: Record<string, string> = {
      happy: '😊',
      excited: '🤩',
      calm: '😌',
      love: '🥰',
      longing: '🥺',
      sad: '😢',
      anxious: '😰',
      stressed: '😖',
    };
    return map[emotion.toLowerCase()] || '🌈';
  }

  deleteLog(logId: number) {
    this.emotionLogService.deleteEmotionLog(logId).subscribe({
      next: (data) => {
        console.log(data);
        this.todaysLogs = this.todaysLogs.filter(log => log.emotionLogId !== logId);

      },
      error: (err) => {
        console.log(err);
      }
    })
  }

  wasSentToDevice(log: EmotionLogResponse): boolean {
    return !!log.sendToDevice;
  }

  

}
