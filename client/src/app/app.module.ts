import { APP_INITIALIZER, importProvidersFrom, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { HTTP_INTERCEPTORS, provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';
import { initializeApp, provideFirebaseApp } from '@angular/fire/app';
import { getAuth, provideAuth } from '@angular/fire/auth';
import { environment } from '../environments/environment.development';
import { AngularFireAuthModule } from "@angular/fire/compat/auth";
import { AuthInterceptor } from './interceptors/auth.interceptor';
import { SuccessRegistrationComponent } from './components/success-registration/success-registration.component';
import { HugTrackerComponent } from './components/hug-tracker/hug-tracker.component';


// firebase initialization function
// export function initializeFirebase() {
//   return () => {
//     const app = initializeApp(environment.firebase);
//     const auth = getAuth(app);
//   }
// }


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    SuccessRegistrationComponent,
    HugTrackerComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    
  ],
  providers: [ provideHttpClient(withInterceptorsFromDi()),
    // {
    //   provide: APP_INITIALIZER,
    //   useFactory: initializeFirebase,
    //   multi: true
    // },
    provideFirebaseApp(() => initializeApp(environment.firebase)),
    provideAuth(() => getAuth()),
    {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true}
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
