import {Component, Input, OnInit} from '@angular/core';
import {FeedbackService} from '../../services/feedback.service';
import {UserMediaComment} from '../../domain/user-media-comment';
import {AuthService} from '../../services/auth.service';
import {AuthUser} from '../../domain/auth-user';
import {ReplaySubject} from 'rxjs';
import {mergeMap, tap} from 'rxjs/internal/operators';

@Component({
  selector: 'as-comments-box',
  templateUrl: './comments-box.component.html',
  styleUrls: ['./comments-box.component.css']
})
export class CommentsBoxComponent implements OnInit {
  public id: string;
  @Input()
  public replaySubject: ReplaySubject<string> = new ReplaySubject<string>();
  public commentText = '';
  public userMediaComments: UserMediaComment[] = [];
  public authUser: AuthUser;

  constructor(private _authService: AuthService, private _feedBackService: FeedbackService) {
  }

  ngOnInit() {
    this._authService.onChange().subscribe(it => this.authUser = it);
    this.replaySubject.asObservable()
      .pipe(tap(itid => this.id = itid), mergeMap((it: string) => this._feedBackService.comments(it)))
      .subscribe(it => this.userMediaComments = it);
  }

  comment() {
    if (this.authUser.isAuth && this.commentText.trim().length !== 0) {
      const umc: UserMediaComment = new UserMediaComment();
      umc.message = this.commentText;
      this._feedBackService.comment(this.id, umc).subscribe(it => {
        this.userMediaComments.push(it);
        this.commentText = '';
      });
    }
  }
}
