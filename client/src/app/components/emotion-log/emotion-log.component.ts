import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { EmotionLogService } from '../../services/emotion-log.service';
import { EmotionStore } from '../../store/emotion.store';
import { EmotionLog } from '../../models/emotion.model';
import { Router } from '@angular/router';
import { timestamp } from 'rxjs';

@Component({
  selector: 'app-emotion-log',
  standalone: false,
  templateUrl: './emotion-log.component.html',
  styleUrl: './emotion-log.component.css'
})
export class EmotionLogComponent implements OnInit {
  
  fb = inject(FormBuilder);
  emotionLogService = inject(EmotionLogService);
  emotionStore = inject(EmotionStore);
  emotionLogForm!: FormGroup;
  router = inject(Router);

  emotions$ = this.emotionStore.emotions$;
  selectedEmotion: string | null = null;
  submitting = false;

  
  
  ngOnInit(): void {
    this.emotionLogForm = this.createEmotionLogForm();
  }


  createEmotionLogForm(): FormGroup {
    return this.fb.group({
      // emotion comes from backend?? 
      emotion: this.fb.control<string>('', [Validators.required]), 
      intensity: this.fb.control<number>(3, [Validators.min(1), Validators.max(5)]),
      sendToDevice: this.fb.control<boolean>(false),
      notes: this.fb.control<string>('')
    })
  }

  selectEmotion(emotionName: string) {
    this.selectedEmotion = emotionName;
    this.emotionLogForm.patchValue({emotion: emotionName})
  }

  processEmotionLogForm() {
    // send to service
    const emotionLog: EmotionLog = {
      deviceId: localStorage.getItem('deviceId') ?? "", // what if localstorage is null?
      emotion: this.emotionLogForm.get('emotion')?.value,
      intensity: this.emotionLogForm.get('intensity')?.value,
      sendToDevice: this.emotionLogForm.get("sendToDevice")?.value,
      timestamp: new Date().toISOString(),
      notes: this.emotionLogForm.get('notes')?.value
    };

    this.emotionLogService.saveEmotionLog(emotionLog).subscribe({
      next: (response) => {
        console.log('Emotion log saved successfully', response);
        this.emotionLogForm = this.createEmotionLogForm();
        this.selectedEmotion = null;
        // redirect to daily logs view // need to create this
        this.router.navigate([`/daily-logs`], {queryParams: {date : emotionLog.timestamp}});

      },
      error: (error) => {
        console.error('Error saving emotion log', error);
      }
    })


  }

  // Helper methods for template
  isEmotionSelected(emotionName: string): boolean {
    return this.selectedEmotion === emotionName;
  }

  getIntensityLabel(): string {
    const intensity = this.emotionLogForm.get('intensity')?.value;
    switch (intensity) {
      case 1: return 'Very Low';
      case 2: return 'Low';
      case 3: return 'Moderate';
      case 4: return 'High';
      case 5: return 'Very High';
      default: return 'Moderate';
    }
  }

  

  



}
