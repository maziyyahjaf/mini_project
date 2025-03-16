import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { ApiResponse } from '../../models/api-response.model';

@Component({
  selector: 'app-register',
  standalone: false,
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent implements OnInit {
 
  form!: FormGroup;
  router = inject(Router);
  fb = inject(FormBuilder);
  authService = inject(AuthService);
  detectedTimezone!: string;

  ngOnInit(): void {
    this.form = this.createForm();
    this.detectedTimezone = Intl.DateTimeFormat().resolvedOptions().timeZone;
  }

  createForm(): FormGroup {
    return this.fb.group({
      deviceId: this.fb.control<string>('', [Validators.required]),
      name: this.fb.control<string>('', [Validators.required]),
      email: this.fb.control<string>('', [Validators.required]),
      password: this.fb.control<string>('', [Validators.required])
    })
  }

  async onSubmit(): Promise<void> {
    console.log('register');
    const formValue = this.form.value;
    //check how this is possible formValue.email etc
    try {
      const result = await this.authService.registerUser(
        formValue.name, formValue.email, formValue.password, formValue.deviceId, this.detectedTimezone
      );
      console.log("Registration Succesful: ", result);
       // handle success (redirect, show success message)
      // NEED TO RETURN LINKING CODE for telegram!!!
      this.router.navigate(["/register-success"], {queryParams: {code: result.teleLinkingCode}})

    } catch (error: any) {
      alert("Registration failed: " + error.message);

    }
   
   
   
   
  }

}
