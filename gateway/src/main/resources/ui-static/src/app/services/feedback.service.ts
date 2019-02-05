import {Injectable} from '@angular/core';
import {DefaultService} from './default.service';
import {UserInfo} from '../domain/user-info';
import {UserMediaComment} from '../domain/user-media-comment';
import {UserMediaLike} from '../domain/user-media-like';
import {UserMediaView} from '../domain/user-media-view';
import {UserUserFollow} from '../domain/user-user-follow';
import {User} from '../domain/user';
import {HttpClient} from '@angular/common/http';
import {UserService} from './user.service';
import {Ids} from '../domain/ids';
import {MediaInformation} from '../domain/media-information';
import {Observable} from 'rxjs';
import {filter, map, mergeMap} from 'rxjs/internal/operators';
import {Search} from "../domain/search";
import {SearchResult} from "../domain/searchResult";
import {Master} from "../domain/master";

@Injectable()
export class FeedbackService {
  private url = '/feedback/api/';
  // private _cacheUserInfo: Map<string, UserInfo> = new Map<string, UserInfo>();
  // private _cacheMediaInfo: Map<string, MediaInfo> = new Map<string, MediaInfo>();

  constructor(private _http: HttpClient, private _defaultService: DefaultService, private _userService: UserService) {
    this.url = this._defaultService.url + this.url;
  }

  search(search: Search): Observable<SearchResult> {
    return this._http.post<SearchResult>(this.url + 'search', search)
      .pipe(filter((it: SearchResult) => it.master.length > 0), mergeMap((it: SearchResult) => {
        return this._userService.findAll(it.master.map((itm: Master) => itm.userId))
          .pipe(map((itus: User[]) => {
            it.master.map((itm: Master) => {
              itm.user = itus.find((i: User) => i.id === itm.userId);
              return itm;
            });
            return it;
          }))
          ;
      }))
      ;
  }

  followersTo(id: string): Observable<UserUserFollow[]> {
    return this._http.get<UserUserFollow[]>(this.url + 'user/followers/' + id);
  }

  followers(id: string): Observable<User[]> {
    return this.followersTo(id)
      .pipe(mergeMap((it: UserUserFollow[]) => {
        return this._userService.findAll(it.map(value => value.from));
      }));
  }

  followingTo(id: string): Observable<UserUserFollow[]> {
    return this._http.get<UserUserFollow[]>(this.url + 'user/following/' + id);
  }

  following(id: string): Observable<User[]> {
    return this.followingTo(id)
      .pipe(mergeMap((it: UserUserFollow[]) => {
        return this._userService.findAll(it.map(value => value.to));
      }));
  }

  trending(): Observable<MediaInformation[]> {
    return this._http.get<MediaInformation[]>(this.url + 'trending');
  }

  userInfo(id: string): Observable<UserInfo> {
    // if (this._cacheUserInfo.has(id)) return Observable.of(this._cacheUserInfo.get(id));
    return this._http.get<UserInfo>(this.url + 'user/info/' + id);
    // .pipe(tap(it => this._cacheUserInfo.set(id, it)));
  }

  mediaInfo(id: string): Observable<MediaInformation> {
    // if (this._cacheMediaInfo.has(id)) return Observable.of(this._cacheMediaInfo.get(id));
    return this._http.get<MediaInformation>(this.url + 'media/info/' + id);
    // .pipe(tap(it => this._cacheMediaInfo.set(id, it)));
  }

  mediaInfoAll(ids: string[]): Observable<MediaInformation[]> {
    return this._http.post<MediaInformation[]>(this.url + 'media/info', new Ids(ids));
  }

  comments(id: string): Observable<UserMediaComment[]> {
    return this._http.get<UserMediaComment[]>(this.url + 'media/comments/' + id)
      .pipe(mergeMap((itumcs: UserMediaComment[]) => {
        return this._userService.findAll(itumcs.map((it: UserMediaComment) => it.userId))
          .pipe(map(itus => {
            itumcs.forEach(itumc => {
              itumc.user = itus.find(ituc => itumc.userId === ituc.id);
            });
            return itumcs;
          }));
      }));
  }

  like(id: string): Observable<UserMediaLike> {
    return this._http.post<UserMediaLike>(this.url + 'media/like/' + id, null);
  }

  liked(id: string): Observable<boolean> {
    return this._http.post(this.url + 'media/liked/' + id, null).pipe(map(it => it['liked'] === true));
  }

  unlike(id: string): Observable<Object> {
    return this._http.post(this.url + 'media/unlike/' + id, null);
  }

  view(id: string): Observable<UserMediaView> {
    return this._http.post<UserMediaView>(this.url + 'media/view/' + id, null);
  }

  views(id: string, size: number, lastId: string): Observable<UserMediaView[]> {
    return this._http.get<UserMediaView[]>(this.url + 'media/views/' + id + '/' + size + '/' + lastId);
  }

  follow(id: string): Observable<UserUserFollow> {
    return this._http.post<UserUserFollow>(this.url + 'user/follow/' + id, null);
  }

  followed(id: string): Observable<boolean> {
    return this._http.post(this.url + 'user/followed/' + id, null).pipe(map(it => it['followed'] === true));
  }

  unfollow(id: string): Observable<Object> {
    return this._http.post(this.url + 'user/unfollow/' + id, null);
  }

  comment(id: string, userMediaComment: UserMediaComment): Observable<UserMediaComment> {
    return this._http.post<UserMediaComment>(this.url + 'media/comment/' + id, userMediaComment)
      .pipe(mergeMap(it => this._userService.findOne(it.userId).pipe(map((itu => {
        it.user = itu;
        return it;
      })))));
  }

  uncomment(id: string): Observable<Object> {
    return this._http.post(this.url + 'media/uncomment/' + id, null);
  }
}
