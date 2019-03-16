import {Component, OnInit} from '@angular/core';
import {Location} from '@angular/common';
import {Search} from '../../domain/search';
import {ActivatedRoute, Params} from '@angular/router';
import {map} from 'rxjs/internal/operators';
import {Master} from "../../domain/master";
import {FeedbackService} from "../../services/feedback.service";

@Component({
  selector: 'as-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {
  public master: Master[] = [];
  public search: Search = new Search();

  constructor(private _activatedRoute: ActivatedRoute, private _feedbackService: FeedbackService, private _location: Location) {
  }

  more() {
    this.search.page += 1;
    this.doSearch();
  }

  ngOnInit() {
    this._activatedRoute.params.pipe(map((it: Params) => {
      const search = new Search();
      search.search_key = it['search_key'].split('-').join(' ');
      search.page = Number.parseInt(it['page']);
      return search;
    }))
      .subscribe(it => {
        this.search = it;
        this.master = [];
        this.doSearch();
      });
  }

  doSearch() {
    this._feedbackService.search(this.search).subscribe(it => {
      it.master.forEach(itm => {
        this.master.push(itm);
      });
      this._location.replaceState('/search/' + this.search.page + '/' + (this.search.search_key.split(' ').join('-')));
    });
  }
}
