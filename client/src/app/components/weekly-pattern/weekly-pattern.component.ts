import { Component, inject, OnInit } from '@angular/core';
import { EmotionLog, EmotionWeeklyPattern } from '../../models/emotion.model';
import { EmotionInsightsService } from '../../services/emotion-insights.service';

@Component({
  selector: 'app-weekly-pattern',
  standalone: false,
  templateUrl: './weekly-pattern.component.html',
  styleUrl: './weekly-pattern.component.css'
})
export class WeeklyPatternComponent implements OnInit{
  
  weeklyPatterns: EmotionWeeklyPattern[] = [];
  selectedLogs: EmotionLog[] = [];
  isDetailsModalOpen = false;
  isLoading = true;
  error: string | null = null;

  insightsService = inject(EmotionInsightsService);


  // group patterns by day for easier rendering
  get patternsByDay(): {[day: string]: EmotionWeeklyPattern[]} {
    const groupedPatterns: { [day: string]: EmotionWeeklyPattern[]} = {};

    const daysOfWeek = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'];
    daysOfWeek.forEach( day => {
      groupedPatterns[day] = [];
    });

    this.weeklyPatterns.forEach(pattern => {
      if (groupedPatterns[pattern.dayOfWeek]) {
        groupedPatterns[pattern.dayOfWeek].push(pattern);
      }
    });

    return groupedPatterns;

  }

  ngOnInit(): void {
    this.loadWeeklyPatterns();
  }

  loadWeeklyPatterns(): void {
    this.isLoading = true;

    this.insightsService.getWeeklyPattern().subscribe({
      next: (data) => {
        this.weeklyPatterns = data;
        this.isLoading = false;
      },
      error : (error) => {
        this.error = 'Failed to load weekly patterns';
        this.isLoading = false;
        console.error(error);
      }
    })
  }

  showDetails(pattern: EmotionWeeklyPattern): void {
    if (!pattern.logIds || pattern.logIds.length === 0) {
      this.selectedLogs = [];
      return;
    }

    // this.insightsService.getEmotionLogsByIds(pattern.logIds)
    //   .subscribe({
    //     next: (logs) => {
    //       this.selectedLogs = logs;
    //       this.isDetailsModalOpen = true;
    //     },
    //     error: (err) => {
    //       console.error('Failed to load emotion log details', err);
    //     }
    //   });
  }

  closeDetailsModal(): void {
    this.isDetailsModalOpen = false;
  }

  // Helper for getting emotion color classes
  getEmotionColorClass(emotion: string): string {
    // Map emotions to color classes
    const emotionColors: { [key: string]: string } = {
      'happy': 'bg-yellow-200',
      'sad': 'bg-blue-200',
      'Angry': 'bg-red-200',
      'anxious': 'bg-purple-200',
      'calm': 'bg-green-200'
      // Add more emotions as needed
    };
    
    return emotionColors[emotion] || 'bg-gray-200';
  }


}
