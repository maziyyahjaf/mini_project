import {
  Component,
  EventEmitter,
  inject,
  Input,
  OnInit,
  Output,
} from '@angular/core';
import { EmotionLogResponse } from '../../models/emotion.model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { EmotionInsightsService } from '../../services/emotion-insights.service';
import { EmotionStore } from '../../store/emotion.store';
import { EmotionLogService } from '../../services/emotion-log.service';

@Component({
  selector: 'app-emotion-log-details',
  standalone: false,
  templateUrl: './emotion-log-details.component.html',
  styleUrl: './emotion-log-details.component.css',
})
export class EmotionLogDetailsComponent implements OnInit {
  @Input() logs: EmotionLogResponse[] = [];
  @Input() emotionTitle: string = '';
  @Input() dayOfWeek: string = '';

  @Output() closeModal = new EventEmitter<void>();
  @Output() logUpdated = new EventEmitter<EmotionLogResponse>();

  isEditModalOpen = false;
  currentEditLog: EmotionLogResponse | null = null;
  editForm!: FormGroup;
  emotionStore = inject(EmotionStore);
  emotions$ = this.emotionStore.emotions$;

  selectedEmotion:string | null = null;

  // need to add notes in my backend??

  fb = inject(FormBuilder);
  emotionLogService = inject(EmotionLogService);
 

  ngOnInit(): void {
    this.editForm = this.createEditForm();
  }

  createEditForm(): FormGroup {
    return this.fb.group({
      emotion: this.fb.control<string>('', [Validators.required]),
      intensity: this.fb.control<number>(3, [
        Validators.min(1),
        Validators.max(5),
      ]),
      sendToDevice: this.fb.control<boolean>(false),
      notes: this.fb.control<string>('', Validators.maxLength(255)),
    });
  }

  getEmotionIntensityStyle(
    emotion: string,
    intensity: number
  ): { [key: string]: string } {
    // default intensity if not provided
    intensity = intensity || 3;

    // map emotions to base colors
    // need to adjust to iot color mapping eventually
    // Map emotions to base colors with HSL values for easier manipulation
    const emotionBaseColors: { [key: string]: string } = {
      happy: 'hsl(50, 100%, 50%)', // Yellow
      excited: 'hsl(120, 100%, 35%)', // Green
      calm: 'hsl(210, 100%, 50%)', // Blue
      love: 'hsl(330, 100%, 70%)', // Pink/Magenta
      longing: 'hsl(300, 100%, 70%)', // Purple/Pink
      sad: 'hsl(240, 70%, 60%)', // Indigo
      anxious: 'hsl(30, 100%, 50%)', // Orange
      stressed: 'hsl(0, 100%, 50%)', // Red
      default: 'hsl(0, 0%, 75%)', // Gray
    };

    // Adjust lightness based on intensity (1-5)
    // Lower intensity = lighter color, Higher intensity = darker color
    const baseLightness = 90; // Start with a very light base
    const lightnessAdjustment = intensity * 12; // 12% darker per intensity level

    // get the base color for this emotion
    const baseColor =
      emotionBaseColors[emotion.toLowerCase()] || emotionBaseColors['default'];
    let hsl = baseColor;

    if (baseColor.startsWith('hsl')) {
      // Parse the HSL values
      const hslMatch = baseColor.match(/hsl\((\d+),\s*(\d+)%,\s*(\d+)%\)/);
      if (hslMatch) {
        const h = hslMatch[1];
        const s = hslMatch[2];
        const l = Math.max(20, baseLightness - lightnessAdjustment); // Ensure not too dark
        hsl = `hsl(${h}, ${s}%, ${l}%)`;
      }
    }

    // Return as a style object to be bound with [ngStyle]
    return {
      'background-color': hsl,
      color: intensity > 3 ? 'white' : 'black', // Text color adjustment for readability
      transition: 'background-color 0.3s ease', // Smooth transition for hover effects
    };
  }

  // start editing a log
  editLog(log: EmotionLogResponse): void {
    this.selectedEmotion = log.emotion;
    this.currentEditLog = log;
    this.editForm.setValue({
      emotion: log.emotion,
      intensity: log.intensity || 3,
      sendToDevice: log.sendToDevice,
      notes: log.notes || '',
    });
    this.isEditModalOpen = true;
  }

  // cancel editing
  cancelEdit(): void {
    this.isEditModalOpen = false;
    this.currentEditLog = null;
    this.selectedEmotion = null;
    this.editForm = this.createEditForm();
  }

  selectEmotion(emotion: string) {
    // this.editForm.patchValue({ emotion: emotion });
    this.selectedEmotion = emotion;
    this.editForm.get('emotion')?.setValue(emotion);
  }

  isEmotionSelected(emotion: string): boolean {
    return this.selectedEmotion === emotion;
  }

  toggleSendToDevice(): void {
    const currentValue = this.editForm.get('sendToDevice')?.value;
    this.editForm.get('sendToDevice')?.setValue(!currentValue);
  }

  // Close the details modal
  close(): void {
    this.closeModal.emit();
  }

  saveEditedLog(): void {
    if (this.editForm.valid && this.currentEditLog) {
      const updatedLog = {
        ...this.currentEditLog,
        emotion: this.editForm.get('emotion')?.value,
        intensity: this.editForm.get('intensity')?.value,
        sendToDevice: this.editForm.get('sendToDevice')?.value,
        notes: this.editForm.get('notes')?.value,
      };

      console.log(updatedLog);
      // call service to update log
      this.emotionLogService.updateEmotionLog(updatedLog).subscribe({
        next: (response) => {
          // find and update the log in the logs array
          const index = this.logs.findIndex(l => l.emotionLogId === updatedLog.emotionLogId);
          if (index !== -1) {
            this.logs[index] = response;
          }

          this.isEditModalOpen = false;
          this.currentEditLog = null;
          this.logUpdated.emit(response);

        },
        error: (err) => {
          console.error('Failed to update emotion log', err);
        }
      })

    }
  }
}
