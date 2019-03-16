import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Token} from '../domain/token';
import {State} from '../domain/state';
import {map} from 'rxjs/operators';
import {User} from "../domain/user";
import {JwtHelperService} from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root'
})
export class OauthService {
  private client_id: string = 'revox';
  private client_secret: string = 'revox';
  private provider: string = '/auth';
  private helper = new JwtHelperService();

  constructor(private _httpClient: HttpClient) {
  }

  private getParams(): URLSearchParams {
    let params = new URLSearchParams();
    params.append('client_id', this.client_id);
    params.append('redirect_uri', window.location.origin + "/");
    return params;
  }

  token(code): Observable<Token> {
    let params = this.getParams();
    params.append('grant_type', 'authorization_code');
    params.append('code', code);
    localStorage.removeItem('access_token');
    return this._httpClient.post<Token>(this.provider + '/oauth/token', params.toString(), {
      headers: new HttpHeaders({
        'Content-type': 'application/x-www-form-urlencoded; charset=utf-8',
        'Authorization': 'Basic ' + btoa(this.client_id + ':' + this.client_secret)
      })
    }).pipe(map(it => {
      localStorage.setItem('access_token', it.access_token);
      return it;
    }));
  }

  public decode(token: Token): User {
    return this.helper.decodeToken(token.access_token)['user'];
  }

  public static token() {
    return localStorage.getItem('access_token');
  }

  authorize() {
    let params = this.getParams();
    params.append('response_type', 'code');
    params.append('scope', 'read');
    params.append('state', this.state(true).id);
    window.location.href = this.provider + '/oauth/authorize?' + params.toString();
  }

  currentUser(): Observable<User> {
    return this._httpClient.get<User>(this.provider + '/user').pipe(map(it => it['user']));
  }

  state(generate: boolean): State {
    if (generate) localStorage.removeItem('state');
    let item = localStorage.getItem('state');
    if (item == null) {
      let state = new State();
      state.id = btoa(Math.random().toString(36));
      state.path = window.location.pathname;
      localStorage.setItem('state', JSON.stringify(state));
      return state;
    }
    else return JSON.parse(item);
  }


}
