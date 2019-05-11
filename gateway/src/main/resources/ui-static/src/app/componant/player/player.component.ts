import {AfterViewInit, Component, Input, OnDestroy} from '@angular/core';

declare var Clappr: any;
declare var LevelSelector: any;
declare var ClapprThumbnailsPlugin: any;

@Component({
  selector: 'as-player',
  templateUrl: './player.component.html',
  styleUrls: ['./player.component.css']
})
export class PlayerComponent implements AfterViewInit, OnDestroy {
  @Input()
  id: string;
  private player: any = null;

  constructor() {
  }

  ngAfterViewInit(): void {
    this.player = new Clappr.Player({
      source: "/tube/api/master/" + this.id + ".m3u8", parentId: "#player",
      plugins: [Clappr.FlasHLS, LevelSelector, ClapprThumbnailsPlugin],
      height: 340,
      autoPlay: true,
      width: 528,
      levelSelectorConfig: {
        title: 'Quality',
        labelCallback: function (playbackLevel, customLabel) {
          return playbackLevel.level.height + 'p'; // High 720p
        }
      },
      scrubThumbnails: {
        backdropHeight: 64,
        spotlightHeight: 84,
        thumbs: []
      }
    });
  }

  ngOnDestroy() {
    this.player.destroy();
  }
}
