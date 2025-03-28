import { APP_INITIALIZER, importProvidersFrom, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { HTTP_INTERCEPTORS, provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';
import { initializeApp, provideFirebaseApp } from '@angular/fire/app';
import { browserSessionPersistence, getAuth, provideAuth, setPersistence } from '@angular/fire/auth';
import { environment } from '../environments/environment';
import { AngularFireAuthModule } from "@angular/fire/compat/auth";
import { AuthInterceptor } from './interceptors/auth.interceptor';
import { SuccessRegistrationComponent } from './components/success-registration/success-registration.component';
import { HugTrackerComponent } from './components/hug-tracker/hug-tracker.component';
import { EmotionLogComponent } from './components/emotion-log/emotion-log.component';
import { WeeklyPatternComponent } from './components/weekly-pattern/weekly-pattern.component';
import { EmotionLogDetailsComponent } from './components/emotion-log-details/emotion-log-details.component';
import { DailyLogsComponent } from './components/daily-logs/daily-logs.component';
import { JournalComponent } from './components/journal/journal.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { AmbientBackgroundComponent } from './components/ambient-background/ambient-background.component';
import { EmotionBubbleComponent } from './components/emotion-bubble/emotion-bubble.component';
import { PartnerStatusComponent } from './components/partner-status/partner-status.component';
import { FloatingPromptComponent } from './components/floating-prompt/floating-prompt.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { UserSettingsComponent } from './components/user-settings/user-settings.component';


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
    HugTrackerComponent,
    EmotionLogComponent,
    WeeklyPatternComponent,
    EmotionLogDetailsComponent,
    DailyLogsComponent,
    JournalComponent,
    DashboardComponent,
    AmbientBackgroundComponent,
    EmotionBubbleComponent,
    PartnerStatusComponent,
    FloatingPromptComponent,
    UserSettingsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    BrowserAnimationsModule
    
    
    
  ],
  providers: [ provideHttpClient(withInterceptorsFromDi()),
    // {
    //   provide: APP_INITIALIZER,
    //   useFactory: initializeFirebase,
    //   multi: true
    // },
    provideFirebaseApp(() => initializeApp(environment.firebase)),
    provideAuth(() =>getAuth()),
    {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true}
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
