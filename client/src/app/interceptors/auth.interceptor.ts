import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { inject } from "@angular/core";
import { Auth } from "@angular/fire/auth";
import { Router } from "@angular/router";
import { catchError, EMPTY, from, Observable, switchMap } from "rxjs";

export class AuthInterceptor implements HttpInterceptor {
    
    firebaseAuth = inject(Auth);
    router = inject(Router);

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

        const user = this.firebaseAuth.currentUser;
        console.log("current user", user);
        console.log("current user in interceptor:", user ? "User exists" : "No user");
        console.log("request URL:", req.url);

        if (!user) {
            console.log("no signedin user");
            return next.handle(req);
        }

        return from(user.getIdToken(true)).pipe(
            switchMap(token => {
                console.log('token in interceptor: ', token.substring(0,10) + '....');
                const modifiedReq = req.clone({
                    setHeaders: { Authorization: `Bearer ${token}`}
                });
                return next.handle(modifiedReq);
            }),
            catchError((error) => {
                console.error('Token refresh failed:', error);

                //redirect to login page
                this.router.navigate(['/login']);
                // return an empty observable to stop the request
                return EMPTY;
            })
        )

    }
    
}