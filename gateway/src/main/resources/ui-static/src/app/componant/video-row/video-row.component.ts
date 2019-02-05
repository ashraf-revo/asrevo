import {Component, Input, OnInit} from '@angular/core';
import {Master} from "../../domain/master";

@Component({
  selector: 'as-video-row',
  templateUrl: './video-row.component.html',
  styleUrls: ['./video-row.component.css']
})
export class VideoRowComponent implements OnInit {
  @Input()
  public master: Master;

  constructor() {
  }

  ngOnInit() {
  }

}
