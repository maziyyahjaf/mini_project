import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { inject } from "@angular/core";
import { Auth } from "@angular/fire/auth";
import { from, Observable, switchMap } from "rxjs";

export class AuthInterceptor implements HttpInterceptor {
    
    firebaseAuth = inject(Auth);

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

        const user = this.firebaseAuth.currentUser;

        if (!user) {
            console.log("no signedin user");
            return next.handle(req);
        }

        return from(user.getIdToken()).pipe(
            switchMap(token => {
                console.log('token in interceptor: ', token.substring(0,10) + '....');
                const modifiedReq = req.clone({
                    setHeaders: { Authorization: `Bearer ${token}`}
                });
                return next.handle(modifiedReq);
            })
        )

    }
    
}