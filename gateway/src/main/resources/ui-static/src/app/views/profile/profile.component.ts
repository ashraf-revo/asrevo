import {Component, OnInit} from '@angular/core';
import {UserService} from '../../services/user.service';
import {TubeService} from '../../services/tube.service';
import {ActivatedRoute, Params} from '@angular/router';
import {User} from '../../domain/user';
import {AuthService} from '../../services/auth.service';
import {AuthUser} from '../../domain/auth-user';
import {filter, tap} from 'rxjs/operators';
import {map, mergeMap} from 'rxjs/internal/operators';
import {Master} from "../../domain/master";

@Component({
  selector: 'as-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  public id: string;
  public user: User;
  public authUser: AuthUser;
  public master: Master[] = [];
  constructor(private _userService: UserService,
              private _tubeService: TubeService,
              private _activatedRoute: ActivatedRoute,
              private _authService: AuthService) {

  }

  ngOnInit() {
    this._authService.onChange()
      .pipe(tap(itvm => this.authUser = itvm), filter(it => it.isAuth === 'true'), mergeMap(itvm => this._activatedRoute.params.pipe(map((it: Params) => it['id']))))
      .subscribe(it => {
      });
    this._activatedRoute.params.pipe(map((it: Params) => it['id'])).subscribe(it => {
      this.id = it;
    });

    this._activatedRoute.params
      .pipe(map((it: Params) => it['id'])
        , mergeMap(it => this._userService.findOne(it)))
      .subscribe(it => {
        this.user = it;
      });
    this._activatedRoute.params.pipe(map((it: Params) => it['id']), mergeMap(it => this._tubeService.findByUserId(it))).subscribe(it => {
      this.master = it;
    });
  }
}
