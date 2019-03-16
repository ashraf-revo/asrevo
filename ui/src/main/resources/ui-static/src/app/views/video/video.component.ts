import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {TubeService} from '../../services/tube.service';
import {FeedbackService} from '../../services/feedback.service';
import {AuthService} from '../../services/auth.service';
import {AuthUser} from '../../domain/auth-user';
import {ReplaySubject, zip} from 'rxjs';
import {map, mergeMap} from 'rxjs/internal/operators';
import {Master} from "../../domain/master";


@Component({
  selector: 'as-video',
  templateUrl: './video.component.html',
  styleUrls: ['./video.component.css']
})
export class VideoComponent implements OnInit {
  public id = '-1';
  public m: Master;
  public isLiked = false;
  public commentText = '';
  public authUser: AuthUser;
  public replaySubject: ReplaySubject<string> = new ReplaySubject<string>();

  constructor(private _activatedRoute: ActivatedRoute,
              private _tubeService: TubeService,
              private _feedBackService: FeedbackService,
              private _authService: AuthService) {
    this.authUser = this._authService.getAuthUser();
  }

  ngOnInit(): void {
    this._authService.onChange().subscribe(it => this.authUser = it);
    this._activatedRoute.params
      .pipe(map(it => it['id']))
      .subscribe(it => {
        this.id = it;
        this.replaySubject.next(it);
      });

    this._activatedRoute.params
      .pipe(map(it => it['id'])
        , mergeMap(it => this._tubeService.findOne(it)))
      .subscribe(it => {
        this.m = it;
      });
    zip(this._authService.onChange().asObservable(), this._activatedRoute.params.pipe(map(it => it['id'])), (r1, r2) => {
      if (this.authUser.isAuth === 'true' && r1.isAuth === 'true') {
        this.liked();
      }
      return r2;
    }).pipe(mergeMap(it => this._feedBackService.view(it))).subscribe();
  }

  like() {
    if (this.authUser.isAuth === 'true') {
      this._feedBackService.like(this.id).subscribe(it => {
        this.isLiked = true;
        this.m.mediaInfo.likesCount += 1;
      });
    }
  }

  unlike() {
    if (this.authUser.isAuth === 'true') {
      this._feedBackService.unlike(this.id).subscribe(it => {
        this.isLiked = false;
        this.m.mediaInfo.likesCount -= 1;
      });
    }
  }

  likeOrUnLike() {
    if (this.authUser.isAuth === 'true') {
      if (this.isLiked) {
        this.unlike();
      } else {
        this.like();
      }
    }
  }

  liked() {
    this._feedBackService.liked(this.id).subscribe(it => {
      this.isLiked = it;
    });
  }
}
