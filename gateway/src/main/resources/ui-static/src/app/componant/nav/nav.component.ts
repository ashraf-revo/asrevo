import {Component, OnInit} from '@angular/core';
import {AuthUser} from '../../domain/auth-user';
import {AuthService} from '../../services/auth.service';

@Component({
  selector: 'as-nav',
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.css']
})
export class NavComponent implements OnInit {
  public authUser: AuthUser;


  constructor(private _authService: AuthService) {
  }

  ngOnInit() {
    this._authService.onChange().subscribe(it => this.authUser = it);
  }

}
