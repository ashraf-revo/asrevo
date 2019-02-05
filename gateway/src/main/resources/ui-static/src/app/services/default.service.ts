import {Injectable} from '@angular/core';
import {NavigationEnd, Router} from '@angular/router';
import {AuthService} from './auth.service';

@Injectable()
export class DefaultService {
  public url = '';
  private _lastRoute: NavigationEnd = null;
  private protectedUrl: string[] = ['settings', 'upload', 'subscriptions', 'history'];

  constructor() {
  }

  set lastRoute(value: NavigationEnd) {
    this._lastRoute = value;
  }

  isAccessible(router: Router, authService: AuthService): boolean {
    if (authService.getAuthUser().isAuth == null || authService.getAuthUser().isAuth === 'true') {
      return true;
    } else if (authService.getAuthUser().isAuth === 'false' && this.protectedUrl.indexOf(this._lastRoute.url.split('/')[1]) !== -1) {
      router.navigate(['/']);
      return false;
    }
    return true;
  }

  get lastRoute(): NavigationEnd {
    return this._lastRoute;
  }


  handle(router: Router, authService: AuthService) {
    router.events.subscribe(it => {
      if (it instanceof NavigationEnd) {
        this.lastRoute = it;
        this.isAccessible(router, authService);
      }
    });
    authService.onChange().subscribe(it => {
      this.isAccessible(router, authService);
    });
  }
}
