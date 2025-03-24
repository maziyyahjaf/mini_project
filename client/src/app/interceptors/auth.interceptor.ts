import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { inject } from "@angular/core";
import { Auth } from "@angular/fire/auth";
import { Router } from "@angular/router";
import { catchError, EMPTY, from, Observable, switchMap } from "rxjs";
import { environment } from "../../environments/environment";

export class AuthInterceptor implements HttpInterceptor {
    
    firebaseAuth = inject(Auth);
    router = inject(Router);

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

        const user = this.firebaseAuth.currentUser;
        console.log("current user", user);
        console.log("current user in interceptor:", user ? "User exists" : "No user");
        console.log("request URL:", req.url);

        // First handle API URL modification for relative URLs starting with /api
        let updatedReq = req;
        if (req.url.startsWith('/api') && environment.apiUrl) {
            updatedReq = req.clone({
                url: `${environment.apiUrl}${req.url}`
            });
            console.log("modified URL:", updatedReq.url);
        }

        // If no user is authenticated, return the (potentially) modified request
        if (!user) {
            console.log("no signedin user");
            return next.handle(updatedReq);
        }

    // If user exists, add the token to the (potentially) modified request
        return from(user.getIdToken(true)).pipe(
            switchMap(token => {
                console.log('token in interceptor: ', token.substring(0,10) + '....');
                const modifiedReq = updatedReq.clone({
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