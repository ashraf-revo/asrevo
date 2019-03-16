import {Component, Input, OnInit} from '@angular/core';
import {User} from '../../domain/user';

@Component({
  selector: 'as-user-row',
  templateUrl: './user-row.component.html',
  styleUrls: ['./user-row.component.css']
})
export class UserRowComponent implements OnInit {
  @Input()
  public user: User;

  constructor() {
  }

  ngOnInit() {
  }

}
