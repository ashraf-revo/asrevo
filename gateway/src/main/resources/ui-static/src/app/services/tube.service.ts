import {Injectable} from '@angular/core';
import {DefaultService} from './default.service';
import {UserService} from './user.service';
import {HttpClient} from '@angular/common/http';
import {filter, map} from 'rxjs/operators';
import {Observable} from 'rxjs';
import {User} from '../domain/user';
import {mergeMap} from 'rxjs/internal/operators';
import {File} from "../domain/file";
import {Master} from "../domain/master";

@Injectable()
export class TubeService {
  private url = '/tube/api/';


  constructor(private _http: HttpClient,
              private _defaultService: DefaultService,
              private _userService: UserService) {
    this.url = this._defaultService.url + this.url;
  }

  findAllPagining(size: number, id: string): Observable<Master[]> {
    return this._http.get<Master[]>(this.url + "master/" + size + '/' + id)
      .pipe(filter(it => it.length > 0), mergeMap((it: Master[]) => {
        return this._userService.findAll(it.map(value => value.userId)).pipe(map(users => {
          return it.map((master: Master) => {
            master.user = users.find((u: User) => u.id === master.userId);
            return master;
          });
        }));
      }));
  }


  findByUserId(itm: string): Observable<Master[]> {
    return this._http.get<Master[]>(this.url + "master/" + 'user/' + itm)
      .pipe(mergeMap((it: Master[]) => {
        return this._userService.findAll(it.map(value => value.userId)).pipe(map(users => {
          return it.map((master: Master) => {
            master.user = users.find((u: User) => u.id === master.userId);
            return master;
          });
        }));
      }));
  }

  findOne(it: string): Observable<Master> {
    return this._http.get<Master>(this.url + "master/" + 'one/' + it)
      .pipe(mergeMap((itm: Master) => this._userService.findOne(itm.userId).pipe(map(itu => {
        itm.user = itu;
        return itm;
      }))));
  }

  save(file: File): Observable<any> {
    return this._http.post(this.url + "file/" + "save", file);
  }
}
