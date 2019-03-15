import {Component} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {filter, flatMap} from 'rxjs/operators';
import {OauthService} from './oauth.service';
import {Token} from './token';

@Component({
  selector: 'auth-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent {
  title = 'auth';

  ngOnInit() {
    this._activatedRoute.queryParams
      .pipe(filter(it => (it['state'] == this._oauthService.state(false).id)), filter(it => (it['code'] != null)), flatMap(it => this._oauthService.token(it['code'])))
      .subscribe((it: Token) => {
        this._router.navigate([this._oauthService.state(false).path]);
      }, error => {
        console.log(error);
      });
  }


  public login() {
    this._oauthService.authorize();
  }

  public user() {
    this._oauthService.user().subscribe();
  }

  constructor(private _router: Router, private _activatedRoute: ActivatedRoute, private _oauthService: OauthService) {
  }
}


