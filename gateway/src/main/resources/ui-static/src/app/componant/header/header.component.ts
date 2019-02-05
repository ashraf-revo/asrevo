import {Component, Input, OnInit} from '@angular/core';
import {AuthUser} from '../../domain/auth-user';
import {UserService} from '../../services/user.service';
import {AuthService} from '../../services/auth.service';
import {ActivatedRoute, NavigationEnd, Router} from '@angular/router';
import {map} from 'rxjs/internal/operators';

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
  public search_key = '';
  public authUser: AuthUser;

  constructor(private _userService: UserService, private _authService: AuthService) {
  }

  ngOnInit() {
    this._authService.onChange().subscribe(it => this.authUser = it);
    if (this.router.url.indexOf('/search') !== -1) {
      this.search_key = this.mapUrl(this.router.url);
    }
    this.router.events.pipe(map(it => {
        if (it instanceof NavigationEnd && it.url.indexOf('/search') !== -1) {
          console.log(it);
          return this.mapUrl(it.url);
        }
        else {
          return '';
        }
      }
    )).subscribe(it => {
      this.search_key = it;
    });
  }

  mapUrl(input: string): string {
    const message = input.split('/');
    return message[3].split('-').join(' ');
  }

  search() {
    if (this.search_key.length > 0) {
      this.router.navigate(['/search', '0', this.search_key.split(' ').join('-')]);
    }
  }

  logout() {
    this._userService.logout().subscribe(it => {
      this._authService.setAuth(null, 'false');
    });

  }
}
