import {Component, OnInit} from '@angular/core';
import {TubeService} from '../../services/tube.service';
import {Master} from "../../domain/master";

@Component({
  selector: 'as-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  public master: Master[] = [];
  private lastid = '0';
  public canLoad = true;

  constructor(private _tubeService: TubeService) {
  }

  ngOnInit() {
    this.load();
  }

  load() {
    if (this.canLoad) {
      this.canLoad = false;
      this._tubeService.findAllPagining(10, this.lastid).subscribe((it: Master[]) => {
        it.forEach(i => {
          this.master.push(i);
        });
      }, error => {
      }, () => {
        if (this.master.length > 0) this.lastid = this.master[this.master.length - 1].id;
        this.canLoad = true;
      });
    }
  }
}
