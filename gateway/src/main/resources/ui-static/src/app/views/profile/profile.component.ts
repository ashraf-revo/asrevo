import {Component, OnInit} from '@angular/core';
import {UserService} from '../../services/user.service';
import {TubeService} from '../../services/tube.service';
import {ActivatedRoute, Params} from '@angular/router';
import {User} from '../../domain/user';
import {AuthService} from '../../services/auth.service';
import {FeedbackService} from '../../services/feedback.service';
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
  public isFollow = false;
  public master: Master[] = [];
  public followers: User[] = [];
  public following: User[] = [];

  constructor(private _userService: UserService,
              private _tubeService: TubeService,
              private _activatedRoute: ActivatedRoute,
              private _authService: AuthService,
              private _feedbackService: FeedbackService) {

  }

  ngOnInit() {
    this._authService.onChange()
      .pipe(tap(itvm => this.authUser = itvm), filter(it => it.isAuth === 'true'),
        mergeMap(itvm => this._activatedRoute.params.pipe(map((it: Params) => it['id']), mergeMap(it => this._feedbackService.followed(it)))
        )
      )

      .subscribe(it => this.isFollow = it);
    this._activatedRoute.params.pipe(map((it: Params) => it['id'])).subscribe(it => {
      this.id = it;
    });

    this._activatedRoute.params
      .pipe(map((it: Params) => it['id'])
        , mergeMap(it => this._userService.findOne(it))
        , mergeMap(it => this._feedbackService.userInfo(it.id).pipe(map(iti => {
          it.userInfo = iti;
          return it;
        }))))
      .subscribe(it => {
        this.user = it;
      });
    this._activatedRoute.params.pipe(map((it: Params) => it['id']), mergeMap(it => this._tubeService.findByUserId(it))).subscribe(it => {
      this.master = it;
    });

    this._activatedRoute.params.pipe(map((it: Params) => it['id']), mergeMap(it => this._feedbackService.followers(it))).subscribe(it => {
      this.followers = it;
    });

    this._activatedRoute.params.pipe(map((it: Params) => it['id']), mergeMap(it => this._feedbackService.following(it))).subscribe(it => {
      this.following = it;
    });
  }

  follow() {
    this._feedbackService.follow(this.id).subscribe(it => {
      this.user.userInfo.followers += 1;
      this.isFollow = true;
    });
  }

  unFollow() {
    this._feedbackService.unfollow(this.id).subscribe(it => {
      this.user.userInfo.followers -= 1;
      this.isFollow = false;
    });
  }

  followOrUnFollow() {
    if (this.isFollow) {
      this.unFollow();
    } else {
      this.follow();
    }
  }
}
