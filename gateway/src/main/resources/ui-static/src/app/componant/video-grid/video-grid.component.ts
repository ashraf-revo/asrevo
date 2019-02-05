import {Component, Input, OnInit} from '@angular/core';
import {Master} from "../../domain/master";

@Component({
  selector: 'as-video-grid',
  templateUrl: './video-grid.component.html',
  styleUrls: ['./video-grid.component.css']
})
export class VideoGridComponent implements OnInit {
  @Input()
  master: Master;

  constructor() {
  }

  ngOnInit() {
  }

}
