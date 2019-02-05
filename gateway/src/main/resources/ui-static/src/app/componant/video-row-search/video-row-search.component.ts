import {Component, Input, OnInit} from '@angular/core';
import {Master} from "../../domain/master";

@Component({
  selector: 'as-video-row-search',
  templateUrl: './video-row-search.component.html',
  styleUrls: ['./video-row-search.component.css']
})
export class VideoRowSearchComponent implements OnInit {
  @Input()
  public master: Master;

  constructor() {
  }

  ngOnInit() {
  }

}
