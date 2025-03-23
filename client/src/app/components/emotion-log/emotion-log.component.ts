import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { EmotionLogService } from '../../services/emotion-log.service';
import { EmotionStore } from '../../store/emotion.store';
import { EmotionLog } from '../../models/emotion.model';

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

  emotions$ = this.emotionStore.emotions$;

  
  
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
    this.emotionLogForm.patchValue({emotion: emotionName})
  }

  processEmotionLogForm() {
    // send to service
    const emotionLog: EmotionLog = {
      deviceId: localStorage.getItem('deviceId') || "", // what if localstorage is null?
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
      },
      error: (error) => {
        console.error('Error saving emotion log', error);
      }
    })


  }

  

  



}
