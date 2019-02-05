import {Injectable} from '@angular/core';
import {AuthUser} from '../domain/auth-user';
import {User} from '../domain/user';
import {ReplaySubject, Subject} from 'rxjs';

@Injectable()
export class AuthService {
  private authUser: AuthUser;
  private _change: Subject<AuthUser> = new ReplaySubject<AuthUser>(1);

  constructor() {
    this.authUser = new AuthUser(null, null);
    this.setAuth(null, null);
  }

  setAuth(user: User, isAuth: string) {
    this.authUser.setData(user, isAuth);
    this._change.next(this.authUser);
  }

  getIsAuth(): boolean {
    return this.authUser.isAuth === 'true';
  }

  getAuthUser(): AuthUser {
    return this.authUser;
  }

  public onChange(): Subject<AuthUser> {
    return this._change;
  }
}
