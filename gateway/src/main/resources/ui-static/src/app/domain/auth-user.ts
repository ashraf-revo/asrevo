import {User} from './user';

export class AuthUser {
  constructor(public user: User, public isAuth: string = null) {
  }

  setData(user: User, isAuth: string) {
    this.user = user;
    this.isAuth = isAuth;
  }
}
