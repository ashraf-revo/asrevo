import {Injectable} from '@angular/core';
import {DefaultService} from './default.service';
import {UserService} from './user.service';
import {HttpClient} from '@angular/common/http';
import {filter, map} from 'rxjs/operators';
import {Observable, zip} from 'rxjs';
import {FeedbackService} from './feedback.service';
import {AuthService} from './auth.service';
import {User} from '../domain/user';
import {MediaInformation} from '../domain/media-information';
import {UserUserFollow} from '../domain/user-user-follow';
import {UserMediaView} from '../domain/user-media-view';
import {Ids} from '../domain/ids';
import {mergeMap} from 'rxjs/internal/operators';
import {MediaGroup} from "../domain/media-group";
import {File} from "../domain/file";
import {Master} from "../domain/master";

@Injectable()
export class TubeService {
  private url = '/tube/api/';


  constructor(private _http: HttpClient,
              private _defaultService: DefaultService,
              private _userService: UserService,
              private _feedbackService: FeedbackService,
              private _authService: AuthService) {
    this.url = this._defaultService.url + this.url;
  }

  findAllPagining(size: number, id: string): Observable<Master[]> {
    return this._http.get<Master[]>(this.url + "master/" + size + '/' + id)
      .pipe(filter(it => it.length > 0), mergeMap((it: Master[]) => {
        return zip(this._userService.findAll(it.map(value => value.userId)),
          this._feedbackService.mediaInfoAll(it.map(value => value.id)),
          (users: User[], mediaInfo: MediaInformation[]) => {
            return it.map((master: Master) => {
              master.user = users.find((u: User) => u.id === master.userId);
              master.mediaInfo = mediaInfo.find((m: MediaInformation) => m.mediaId === master.id);
              return master;
            });
          });
      }));
  }

  subscriptions(size: number, id: string): Observable<Master[]> {
    return this._feedbackService.followingTo(this._authService.getAuthUser().user.id)
      .pipe(filter((it: UserUserFollow[]) => it.length > 0), mergeMap((it: UserUserFollow[]) => {
        return zip(this._http.post<Master[]>(this.url + "master/" + size + '/' + id,
          new Ids(it.map(value => value.to))),
          this._userService.findAll(it.map(value => value.to)),
          (masters: Master[], users: User[]) => {
            return masters.map(value => {
              value.user = users.find((u: User) => u.id === value.userId);
              return value;
            });
          });
      }));
  }

  history(size: number, lastId: string): Observable<UserMediaView[]> {
    return this._feedbackService.views(this._authService.getAuthUser().user.id, size, lastId)
      .pipe(filter((it: UserMediaView[]) => it.length > 0), mergeMap((itums: UserMediaView[]) => {
        return this.findAllByIds(itums.map(itum => itum.mediaId))
          .pipe(map((itms: Master[]) => {
            itums.forEach((itum: UserMediaView) => {
              itum.m = itms.find(itm => itm.id === itum.mediaId);
            });
            return itums;
          }));
      }))
      ;
  }

  trending(): Observable<Master[]> {
    return this._feedbackService.trending()
      .pipe(filter((it: MediaInformation[]) => it.length > 0), mergeMap((itums: MediaInformation[]) => {
        return zip(this._feedbackService.mediaInfoAll(itums.map(itum => itum.mediaId)),
          this.findAllByIds(itums.map(itum => itum.mediaId)),
          (mis: MediaInformation[], ms: Master[]) => {
            return ms.map((master: Master) => {
              master.mediaInfo = mis.find((m: MediaInformation) => m.mediaId === master.id);
              return master;
            });
          });
      }));
  }


  findAllByIds(ids: string[]): Observable<Master[]> {
    return this._http.post<Master[]>(this.url + "master/", new Ids(ids))
      .pipe(mergeMap((itms: Master[]) => {
        return this._userService.findAll(itms.map((itm: Master) => itm.userId))
          .pipe(map((itus: User[]) => {
            itms.forEach(itm => {
              itm.user = itus.find(itu => itu.id === itm.userId);
            });
            return itms;
          }))
          ;
      }));
  }

  findByUserId(itm: string): Observable<Master[]> {
    return this._http.get<Master[]>(this.url + "master/" + 'user/' + itm)
      .pipe(mergeMap((it: Master[]) => {
        return zip(this._userService.findAll(
          it.map(value => value.userId)),
          this._feedbackService.mediaInfoAll(it.map(value => value.id)),
          (users: User[], mediaInfo: MediaInformation[]) => {
            return it.map((master: Master) => {
              master.user = users.find((u: User) => u.id === master.userId);
              master.mediaInfo = mediaInfo.find((m: MediaInformation) => m.mediaId === master.id);
              return master;
            });
          });
      }));
  }

  findOne(it: string): Observable<Master> {
    return this._http.get<Master>(this.url + "master/" + 'one/' + it)
      .pipe(mergeMap((itm: Master) => this._userService.findOne(itm.userId).pipe(map(itu => {
        itm.user = itu;
        return itm;
      }))), mergeMap((itmf: Master) => this._feedbackService.mediaInfo(itmf.id).pipe(map(itm => {
        itmf.mediaInfo = itm;
        return itmf;
      }))));
  }

  findGroup(master: string): Observable<MediaGroup> {
    return this._http.get<MediaGroup>(this.url + "master/" + "group/" + master)
  }

  findAllInGroup(id: string): Observable<Master[]> {
    return this._http.get<Master[]>(this.url + "master/" + "group/" + id + "/media")
      .pipe(mergeMap((it: Master[]) => {
        return zip(this._userService.findAll(
          it.map(value => value.userId)),
          this._feedbackService.mediaInfoAll(it.map(value => value.id)),
          (users: User[], mediaInfo: MediaInformation[]) => {
            return it.map((master: Master) => {
              master.user = users.find((u: User) => u.id === master.userId);
              master.mediaInfo = mediaInfo.find((m: MediaInformation) => m.mediaId === master.id);
              return master;
            });
          });
      }));
  }

  save(file: File): Observable<any> {
    return this._http.post(this.url + "file/" + "save", file);
  }


}
