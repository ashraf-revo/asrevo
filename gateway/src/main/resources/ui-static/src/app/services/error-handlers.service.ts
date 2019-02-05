import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable, of, throwError} from 'rxjs';
import {delay, mergeMap, retryWhen, take} from 'rxjs/internal/operators';


@Injectable()
export class ErrorHandlersService implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (req.url === '/tube/api/save') {
      return next.handle(req);
    }
    return next.handle(req)
      .pipe(
        retryWhen(error => {
          return error
            .pipe(mergeMap((e: any) => {
              if (e.status === 500) {
                return of(e.status).pipe(delay(1000));
              }
              return throwError({error: 'No retry'});
            }), take(5));
        })
      )
      ;
  }

  constructor() {
  }

}
