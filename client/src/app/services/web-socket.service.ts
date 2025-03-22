import { Injectable, OnDestroy } from '@angular/core';
import { Client, StompSubscription } from '@stomp/stompjs';
import { BehaviorSubject, filter, first, Observable } from 'rxjs';
import SockJS from 'sockjs-client';

@Injectable({
  providedIn: 'root',
})
export class WebSocketService {
  private stompClient: Client;
  private connectionState = new BehaviorSubject<boolean>(false);

  constructor() {

    console.log('connecting to Websocket');
    this.stompClient = new Client({
      brokerURL: "/ws",
      reconnectDelay: 5000,
      debug: (msg) => {
        console.log("STOMP Debug: ", msg);
      },
      onConnect: (frame) => {
        console.log('Websocket connected');
        this.connectionState.next(true);
      },
      onDisconnect: () => {
        console.log('Websocket disconnected');
        this.connectionState.next(false);
      },
      onStompError: (frame) => {
        console.error("STOMP error: ", frame);
      }
    });

    console.log("Activating STOMP client with SockJs...");
    this.stompClient.activate();
  }

  // Wait for connection before subscribing
  private whenConnected(): Observable<boolean> {
    return this.connectionState.pipe(
      filter((connected) => connected),
      first()
    );
  }

  subscribeToHugCount(pairingId: string, callback: (message: any) => void) {
    this.whenConnected().subscribe(() => {
      console.log(`Now subscribing to /topic/hugs/${pairingId}`);
      this.stompClient.subscribe(`/topic/hugs/${pairingId}`, (message) => {
        callback(JSON.parse(message.body));
      });
    });
  }

  // Check if already connected
  isConnected(): boolean {
    return this.stompClient && this.stompClient.active;
  }
}
