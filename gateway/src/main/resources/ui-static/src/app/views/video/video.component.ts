import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {TubeService} from '../../services/tube.service';
import {AuthService} from '../../services/auth.service';
import {AuthUser} from '../../domain/auth-user';
import {zip} from 'rxjs';
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
  public authUser: AuthUser;

  constructor(private _activatedRoute: ActivatedRoute,
              private _tubeService: TubeService,
              private _authService: AuthService) {
    this.authUser = this._authService.getAuthUser();
  }

  ngOnInit(): void {
    this._authService.onChange().subscribe(it => this.authUser = it);
    this._activatedRoute.params
      .pipe(map(it => it['id']))
      .subscribe(it => {
        this.id = it;
      });

    this._activatedRoute.params
      .pipe(map(it => it['id'])
        , mergeMap(it => this._tubeService.findOne(it)))
      .subscribe(it => {
        this.m = it;
      });
    zip(this._authService.onChange().asObservable(), this._activatedRoute.params.pipe(map(it => it['id'])), (r1, r2) => {
      return r2;
    }).subscribe();
  }

}
