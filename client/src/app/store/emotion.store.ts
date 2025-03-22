import { inject, Injectable } from "@angular/core";
import { Emotion } from "../models/emotion.model";
import { ComponentStore } from "@ngrx/component-store";
import { HttpClient } from "@angular/common/http";
import { catchError, EMPTY, tap } from "rxjs";
import { AuthService } from "../services/auth.service";

export interface EmotionState {
    emotions: Emotion[];
    selectedEmotion: string | null;
}

@Injectable({
    providedIn: 'root'
})
export class EmotionStore extends ComponentStore<EmotionState> {

    constructor(private authService: AuthService) {
        super({emotions: [], selectedEmotion: null});
         // Automatically load emotions when store is created
        this.loadEmotions();
        
    }

    private http = inject(HttpClient);
  



    readonly emotions$ = this.select(state => state.emotions);
    readonly selectedEmotion$ = this.select(state => state.selectedEmotion);

    readonly setEmotions = this.updater((state, emotions: Emotion[]) => ({
        ...state,
        emotions
    }));

    readonly selectEmotion = this.updater<string>((state, emotionName: string) => ({
        ...state,
        selectedEmotion: emotionName,
    }))

    readonly loadEmotions = this.effect(() => {
        return this.http.get<Emotion[]>('/api/emotions').pipe(
            tap(emotions => this.setEmotions(emotions)),
            catchError(error => {
                console.error("Error loading emotions: ", error);
                return EMPTY;
            })
        )
    })
}

