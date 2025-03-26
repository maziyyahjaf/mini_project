import { Component, inject, OnInit } from '@angular/core';
import { UserSettingsService } from '../../services/user-settings.service';
import { UserSettings } from '../../models/user.model';

@Component({
  selector: 'app-user-settings',
  standalone: false,
  templateUrl: './user-settings.component.html',
  styleUrl: './user-settings.component.css'
})
export class UserSettingsComponent implements OnInit {
  

  userSettingsService = inject(UserSettingsService);
  userSettings!: UserSettings;

  ngOnInit(): void {
    this.userSettingsService.fetchUserSettings().subscribe({
      next: (data) => {
        this.userSettings = data;
        console.log(data);
      },
      error: (err) => {
        console.log(err);
      }
    })
  }


}
