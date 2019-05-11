import {Component, Input, OnInit} from '@angular/core';
import {AuthUser} from '../../domain/auth-user';
import {UserService} from '../../services/user.service';
import {AuthService} from '../../services/auth.service';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'as-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  @Input()
  public router: Router;
  @Input()
  public activatedRoute: ActivatedRoute;
  public authUser: AuthUser;

  constructor(private _userService: UserService, private _authService: AuthService) {
  }

  ngOnInit() {
    this._authService.onChange().subscribe(it => this.authUser = it);
  }


  logout() {
    this._userService.logout().subscribe(it => {
      this._authService.setAuth(null, 'false');
    });

  }
}
