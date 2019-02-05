import {Injectable} from '@angular/core';
import {DefaultService} from './default.service';
import {User} from '../domain/user';
import {HttpClient} from '@angular/common/http';
import {mergeMap, tap} from 'rxjs/internal/operators';
import {Observable, of} from 'rxjs';
import {Ids} from '../domain/ids';
import {map} from 'rxjs/operators';


@Injectable()
export class UserService {
  private url = '/auth/';
  private _cacheUser: Map<string, User> = new Map<string, User>();

  constructor(private _http: HttpClient, private _defaultService: DefaultService) {
    this.url = this._defaultService.url + this.url;
  }

  currentUser(): Observable<User> {
    return this._http.get(this.url + 'user').pipe(map(it => it["user"]));
  }

  findOne(id: string): Observable<User> {
    if (this._cacheUser.has(id)) {
      return of(this._cacheUser.get(id));
    }
    return this._http.get<User>(this.url + 'api/user/' + id)
      .pipe(tap(it => this._cacheUser.set(id, it)));
  }

  findAll(ids: string[]): Observable<User[]> {
    return this._http.post<User[]>(this.url + 'api/user', new Ids(ids));
  }

  logout(): Observable<Object> {
    return this._http.get(this.url + 'signout', {responseType: 'text'})
      .pipe(mergeMap(it => this._http.get(this._defaultService.url + '/signout')));
  }

}
