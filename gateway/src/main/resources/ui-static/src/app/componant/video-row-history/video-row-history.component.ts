import {Component, Input, OnInit} from '@angular/core';
import {UserMediaView} from "../../domain/user-media-view";

@Component({
  selector: 'as-video-row-history',
  templateUrl: './video-row-history.component.html',
  styleUrls: ['./video-row-history.component.css']
})
export class VideoRowHistoryComponent implements OnInit {
  @Input()
  public userMediaView: UserMediaView;

  constructor() {
  }

  ngOnInit() {
  }

}
