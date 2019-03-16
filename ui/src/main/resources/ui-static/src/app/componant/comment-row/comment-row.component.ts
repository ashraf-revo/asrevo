import {Component, Input, OnInit} from '@angular/core';
import {UserMediaComment} from '../../domain/user-media-comment';

@Component({
  selector: 'as-comment-row',
  templateUrl: './comment-row.component.html',
  styleUrls: ['./comment-row.component.css']
})
export class CommentRowComponent implements OnInit {
  @Input()
  umc: UserMediaComment;

  constructor() {
  }

  ngOnInit() {
  }

}
