import { Component, inject, OnInit } from '@angular/core';
import { WebSocketService } from '../../services/web-socket.service';
import { HugCountService } from '../../services/hug-count.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-hug-tracker',
  standalone: false,
  templateUrl: './hug-tracker.component.html',
  styleUrl: './hug-tracker.component.css',
})
export class HugTrackerComponent implements OnInit {
  hugCount: number = 0;
  // pairId = "abcd1234_efgh5678"; // should get this from backend?? how will this persist??
  // when user first registers -> success registration -> save to localstorage
  // need to figure out when user logins
  // when user logins -> authenticate with firebase -> go to api/login to get device id, pairing id, paired device id info?
  pairingId: string | null = localStorage.getItem('pairing_id'); // retrieve pairing ID from local storage

  wsService = inject(WebSocketService);
  hugCountService = inject(HugCountService);

  ngOnInit(): void {
    console.log('Component initializing...');

    if (!this.pairingId) {
      console.error(
        'No pairing ID found in local storage. User might need to log in'
      );
      // redirect to login or show a message?
      return;
    }

    this.fetchDailyHugCount();

    // Check connection status every second
    const interval = setInterval(() => {
      console.log('Connection status:', this.wsService.isConnected());
      if (this.wsService.isConnected()) {
        clearInterval(interval);
      }
    }, 1000);

    // subscribe to live updates via WebSocket
    this.wsService.subscribeToHugCount(this.pairingId, (message) => {
      console.log('Received message:', message);
      this.hugCount = message.count;
    });

  }

  fetchDailyHugCount() {
    this.hugCountService.getDailyHugCount(this.pairingId!).subscribe({
      next: (response) => {
        console.log('Fetched daily hug count: ', response.count),
          (this.hugCount = response.count);
      },
      error: (error) => {
        console.error('Failed to fetch hug count', error);
      },
    });
  }
}
