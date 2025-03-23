import { Component, inject, OnInit } from '@angular/core';
import { EmotionLog, EmotionLogResponse, EmotionWeeklyPattern } from '../../models/emotion.model';
import { EmotionInsightsService } from '../../services/emotion-insights.service';

@Component({
  selector: 'app-weekly-pattern',
  standalone: false,
  templateUrl: './weekly-pattern.component.html',
  styleUrl: './weekly-pattern.component.css'
})
export class WeeklyPatternComponent implements OnInit{
  
  weeklyPatterns: EmotionWeeklyPattern[] = [];
  selectedLogs: EmotionLogResponse[] = [];
  selectedPattern: EmotionWeeklyPattern | null = null;
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

  // Calculate percentages for each day
  get patternsByDayWithPercentage(): { [day: string]: (EmotionWeeklyPattern & { percentage: number, heightStyle: string })[] } {
    const result: { [day: string]: (EmotionWeeklyPattern & { percentage: number, heightStyle: string })[] } = {};
    
    // Calculate percentages for each day
    for (const day in this.patternsByDay) {
      const patterns = this.patternsByDay[day];
      const totalFrequency = patterns.reduce((sum, pattern) => sum + pattern.frequency, 0);
      
      // Add percentage to each pattern
      result[day] = patterns.map(pattern => {
        const percentage = totalFrequency > 0 ? (pattern.frequency / totalFrequency) * 100 : 0;
        
        // Calculate height based on percentage (minimum height of 40px for visibility)
        // This sets the height as a percentage of the container height
        const heightStyle = totalFrequency > 0 ? 
          `height: ${Math.max(percentage, 5)}%;` : 
          'height: auto;';
        
        return {
          ...pattern,
          percentage,
          heightStyle
        };
      }).sort((a, b) => b.frequency - a.frequency); // Sort by frequency descending
    }
    
    return result;
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
    });
  }

  showDetails(pattern: EmotionWeeklyPattern): void {
    if (!pattern.logIds || pattern.logIds.length === 0) {
      this.selectedLogs = [];
      return;
    }

    this.insightsService.getEmotionLogsByIds(pattern.logIds)
      .subscribe({
        next: (logs) => {
          this.selectedLogs = logs;
          this.isDetailsModalOpen = true;
        },
        error: (err) => {
          console.error('Failed to load emotion log details', err);
        }
      });
  }

  closeDetailsModal(): void {
    this.isDetailsModalOpen = false;
  }

  // Helper for getting emotion color classes
  getEmotionColorClass(emotion: string): string {
    // Map emotions to Tailwind color classes
    const emotionColors: { [key: string]: string } = {
      'happy': 'bg-yellow-200',
      'sad': 'bg-indigo-200',
      'anxious': 'bg-orange-200',
      'stressed': 'bg-red-200',
      'calm': 'bg-blue-200',
      'excited': 'bg-green-200',
      'love': 'bg-magenta-200',
      'longing': 'bg-pink-200'
      // Add more emotions as needed
    };
    
    return emotionColors[emotion.toLowerCase()] || 'bg-gray-200';
  }

  // Get the total frequency for a day
  getDayTotal(day: string): number {
    return this.patternsByDay[day].reduce((sum, pattern) => sum + pattern.frequency, 0);
  }

  // Handle log updates from the details component
  onLogUpdated(updatedLog: EmotionLogResponse): void {
    // Refresh weekly patterns to reflect any changes
    this.loadWeeklyPatterns();
  }

  


}
