import {Component, OnInit} from '@angular/core';
import {TubeService} from '../../services/tube.service';
import {AuthService} from '../../services/auth.service';
import {AuthUser} from '../../domain/auth-user';
import {UserMediaView} from '../../domain/user-media-view';
import {take} from 'rxjs/operators';
import {filter, mergeMap} from 'rxjs/internal/operators';

@Component({
  selector: 'as-history',
  templateUrl: './history.component.html',
  styleUrls: ['./history.component.css']
})
export class HistoryComponent implements OnInit {
  public userMediaViews: UserMediaView[] = [];
  private lastid = '0';
  public canLoad = true;

  constructor(private _authService: AuthService, private _tubeService: TubeService) {
  }

  ngOnInit() {
    this.load();
  }

  load() {
    if (this.canLoad) {
      this.canLoad = false;
      this._authService.onChange().asObservable().pipe(filter((it: AuthUser) => it.isAuth === 'true'))
        .pipe(mergeMap(it => this._tubeService.history(5, this.lastid)), take(1))
        .subscribe((it: UserMediaView[]) => {
            it.forEach(i => {
              this.userMediaViews.push(i);
            });
          }
          , error => {
          }, () => {
            if (this.userMediaViews.length > 0) {
              this.lastid = this.userMediaViews[this.userMediaViews.length - 1].id;
            }
            this.canLoad = true;
          });
    }
  }
}
