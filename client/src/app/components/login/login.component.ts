import { HttpClient } from '@angular/common/http';
import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit{
  

  form!: FormGroup;
  fb = inject(FormBuilder);
  http = inject(HttpClient);
  router = inject(Router);


  ngOnInit(): void {
    this.form = this.createForm();
  }

  createForm(): FormGroup {
    return this.fb.group({
      email: this.fb.control<string>('', [Validators.required]),
      password: this.fb.control<string>('', [Validators.required])
    })
  }

  onSubmit(): void {
    console.log('login');
  }



}
