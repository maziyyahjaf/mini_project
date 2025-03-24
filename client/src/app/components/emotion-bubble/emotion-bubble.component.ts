import { Component, Input } from '@angular/core';
import { EmotionLogResponse } from '../../models/emotion.model';

@Component({
  selector: 'app-emotion-bubble',
  standalone: false,
  templateUrl: './emotion-bubble.component.html',
  styleUrl: './emotion-bubble.component.css'
})
export class EmotionBubbleComponent {

  @Input() mood: string | undefined = '';
  @Input() emoji: string = '';
  @Input() relativeTime: string = '';




}
