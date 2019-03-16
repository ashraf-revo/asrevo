import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {TubeService} from '../../services/tube.service';
import {map, mergeMap} from 'rxjs/internal/operators';
import {MediaGroup} from "../../domain/media-group";
import {AuthService} from "../../services/auth.service";
import {AuthUser} from "../../domain/auth-user";
import {Master} from "../../domain/master";

@Component({
  selector: 'as-group',
  templateUrl: './group.component.html',
  styleUrls: ['./group.component.css']
})
export class GroupComponent implements OnInit {
  master: Master[] = [];
  group: MediaGroup = new MediaGroup();
  public authUser: AuthUser;

  constructor(private _activatedRoute: ActivatedRoute,
              private _tubeService: TubeService,
              private _authService: AuthService) {
    this.authUser = this._authService.getAuthUser();
  }

  ngOnInit(): void {
    this._authService.onChange().subscribe(it => this.authUser = it);

    this._activatedRoute.params
      .pipe(map(it => it['id'])
        , mergeMap(it => this._tubeService.findGroup(it)))
      .subscribe(it => {
        this.group = it;
      });

    this._activatedRoute.params
      .pipe(map(it => it['id'])
        , mergeMap(it => this._tubeService.findAllInGroup(it)))
      .subscribe(it => {
        this.master = it;
      });
  }

  delete(m: Master): void {
    this.master = this.master.filter(it => it.id != m.id);
  }
}
