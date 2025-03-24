import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';

@Component({
  selector: 'app-ambient-background',
  standalone: false,
  templateUrl: './ambient-background.component.html',
  styleUrl: './ambient-background.component.css'
})
export class AmbientBackgroundComponent implements OnChanges {
  @Input() mood: string  = '' ;
  @Input() intensity: number = 1;

  backgroundClass = 'neutral';
  
  
  ngOnChanges(changes: SimpleChanges): void {
    this.backgroundClass = this.mapMoodToClass(this.mood);
  }

  mapMoodToClass(mood: string): string {
    switch(mood?.toLowerCase()) {
      case 'happy': return 'bg-happy';
      case 'excited': return 'bg-excited';
      case 'calm': return 'bg-calm';
      case 'love': return 'bg-calm';
      case 'longing': return 'bg-longing';
      case 'sad': return 'bg-sad';
      case 'anxious': return 'bg-stressed';
      default: return 'neutral';
    }
  }

}
