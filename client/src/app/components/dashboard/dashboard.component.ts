import { Component, inject, OnInit } from '@angular/core';
import { UserLoginPayload } from '../../models/user.model';
import { DashboardSnapshot, EmotionLogResponse } from '../../models/emotion.model';
import { DashboardService } from '../../services/dashboard.service';
import { DateTime } from 'luxon';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-dashboard',
  standalone: false,
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit{

  // can get name from local storage
  userName: string | null = localStorage.getItem('name');
  pairingId: string = localStorage.getItem('pairing_id') || '';
  todayMood!: EmotionLogResponse;
  partnerMood!: EmotionLogResponse;
  streakCount!: number;
  date!: string;
  noLogMessage: string = '';
  snapshot!: DashboardSnapshot;
  moodEmoji: string = '';
  moodRelativeTime: string = '';
  auraClass:string = 'aura-neutral'
  hugStatusMessage: string = '';
  lastHugTime: string | null = null;

  dashboardService = inject(DashboardService);
  router = inject(Router);
  authService = inject(AuthService);
  
  ngOnInit(): void {
    this.date = new Date().toISOString();

    this.dashboardService.getDashboardSnapshot(this.date,this.pairingId).subscribe({
      next: (snapshot) => {
        console.log(snapshot);
        this.snapshot = snapshot;

        if (snapshot.latestEmotionLog) {
          this.todayMood = snapshot.latestEmotionLog
          const emotion = snapshot.latestEmotionLog.emotion;
          this.auraClass = this.mapMoodToAura(emotion);
          this.moodEmoji = this.getEmojiForEmotion(emotion);
          this.moodRelativeTime = DateTime.fromISO(snapshot.latestEmotionLog.timestamp).toRelative() ?? '';
          console.log(`You've been feeling ${emotion} ${this.moodEmoji} (${this.moodRelativeTime})`)
        }

        if (snapshot.hugInteraction?.lastSimultaneousHug) {
          // this.lastHugTime = DateTime.fromISO(snapshot.hugInteraction.lastSimultaneousHug, {zone: 'utc'})
          //                         .setZone(DateTime.local().zoneName)
          //                         .toRelative();
          this.lastHugTime = snapshot.hugInteraction?.lastSimultaneousHug;
          console.log(`You hugged together ${this.getRelativeTime(this.lastHugTime)}`)
          console.log("lastHugTime value:", this.lastHugTime); 


        }

        if (!snapshot.partnerEmotion) {
          console.log(`Your partner hasn't checked in yet 🐻`);
        }

      },
      error: (error) => {
        console.error(error.message);
      }
    })
  }


  getRelativeTime(isoTime: string): string {
    return DateTime.fromISO(isoTime, { zone: 'utc' })
      .setZone(DateTime.local().zoneName)
      .toRelative() || '';
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

  mapMoodToAura(mood: string): string {
    switch (mood.toLowerCase()) {
      case 'happy': return 'aura-happy';
      case 'excited': return 'aura-excited';
      case 'sad': return 'aura-sad';
      case 'calm': return 'aura-calm';
      case 'stressed': return 'aura-stressed';
      case 'love': return 'aura-love';
      case 'longing': return 'aura-tired';
      case 'anxious': return 'aura-anxious';
      default: return 'aura-neutral';
    }
  }

  sendHug() {
    const pairedDeviceId = localStorage.getItem('pairedDeviceId') || '';
    if (!pairedDeviceId) {
      this.hugStatusMessage = '⚠️ No paired device found!';
      return;
    }
    this.dashboardService.sendHug(pairedDeviceId).subscribe({
      next: () => {
        this.hugStatusMessage = '🤗 Hug sent!';
        setTimeout(() => this.hugStatusMessage = '', 3000);
      },
      error: (err) => {
        this.hugStatusMessage = '⚠️ Failed to send hug';
        console.error(err);
      }
    });
  }

  refreshLastHugTime() {
    this.dashboardService.refreshLastHugTime(this.pairingId).subscribe({
      next: (res) => {
       this.lastHugTime = res.timestamp;
      },
      error: (err) => {
        console.error('Failed to fetch updated hug time', err);
      }
    })
  }

  async onLogout() {
    try {
      await this.authService.logout();
      this.router.navigate(['/login']);

    } catch (error) {
      console.error('logout error:', error);
    }
  }

  
    
}
