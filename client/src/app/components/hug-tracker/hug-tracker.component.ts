import { Component, inject, OnInit } from '@angular/core';
import { WebSocketService } from '../../services/web-socket.service';

@Component({
  selector: 'app-hug-tracker',
  standalone: false,
  templateUrl: './hug-tracker.component.html',
  styleUrl: './hug-tracker.component.css'
})
export class HugTrackerComponent implements OnInit{
  hugCount: number = 0;
  pairId = "abcd1234_efgh5678"; // should get this from backend?? how will this persist??
  // how should i get the pairId
  // i need to know the user's device id..
  // need to save user's device id??

  wsService = inject(WebSocketService);
  
  ngOnInit(): void {

    console.log('Component initializing...');
  
  // Check connection status every second
  const interval = setInterval(() => {
    console.log('Connection status:', this.wsService.isConnected());
    if (this.wsService.isConnected()) {
      clearInterval(interval);
    }
  }, 1000);
    this.wsService.subscribeToHugCount(this.pairId, (message) => {
      console.log("Received message:", message);
      this.hugCount = message;

    });
  }

}
