import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {UserService} from '../../services/user.service';
import {TubeService} from '../../services/tube.service';
import {ActivatedRoute, Router} from '@angular/router';
import {DefaultService} from '../../services/default.service';
import {Master} from "../../domain/master";
import {OauthService} from "../../services/oauth.service";
import {Token} from "../../domain/token";
import {filter, flatMap} from "rxjs/operators";

@Component({
  selector: 'as-base',
  templateUrl: './base.component.html',
  styleUrls: ['./base.component.css']
})
export class BaseComponent implements OnInit {
  public master: Master[] = [];

  constructor(public _activatedRoute: ActivatedRoute,
              public _router: Router,
              private _userService: UserService,
              private _authService: AuthService,
              private _tubeService: TubeService,
              private _defaultService: DefaultService,
              private _oauthService: OauthService) {
    this._defaultService.handle(this._router, this._authService);
  }


  ngOnInit() {
    this._oauthService.currentUser().subscribe(it => this._authService.setAuth(it, 'true'), it => this._authService.setAuth(null, 'false'));
    this._tubeService.findAllPagining(5, '0').subscribe((it: Master[]) => this.master = it);
    this._activatedRoute.queryParams
      .pipe(filter(it => (it['state'] == this._oauthService.state(false).id)), filter(it => (it['code'] != null)), flatMap(it => this._oauthService.token(it['code'])))
      .subscribe((it: Token) => {
        this._authService.setAuth(this._oauthService.decode(it), 'true');
        this._router.navigate([this._oauthService.state(false).path]);
      }, error => {
        console.log(error);
      });

  }


}
