import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AppComponent } from './app.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { SuccessRegistrationComponent } from './components/success-registration/success-registration.component';
import { HugTrackerComponent } from './components/hug-tracker/hug-tracker.component';
import { EmotionLogComponent } from './components/emotion-log/emotion-log.component';
import { WeeklyPatternComponent } from './components/weekly-pattern/weekly-pattern.component';

const routes: Routes = [
  {path: "", component: AppComponent},
  {path: "login", component: LoginComponent},
  {path: "register", component: RegisterComponent},
  {path: "register-success", component: SuccessRegistrationComponent},
  {path: "hugs", component: HugTrackerComponent},
  {path: "log", component: EmotionLogComponent},
  {path: "insights", component: WeeklyPatternComponent},
  {path: "**", component: AppComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
