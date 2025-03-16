import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-success-registration',
  standalone: false,
  templateUrl: './success-registration.component.html',
  styleUrl: './success-registration.component.css'
})
export class SuccessRegistrationComponent implements OnInit {
  
  linkingCode!: string;
  telegramDeepLink!: string;

  activatedRoute = inject(ActivatedRoute);
  
  ngOnInit(): void {
    // can use observable??
    this.linkingCode = this.activatedRoute.snapshot.queryParams['code'];
    this.telegramDeepLink = `https://t.me/toy_hugs_bot?start=${this.linkingCode}`;
  }

}
