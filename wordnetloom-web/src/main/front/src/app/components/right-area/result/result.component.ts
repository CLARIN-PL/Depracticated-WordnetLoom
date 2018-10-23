import {ChangeDetectorRef, Component, Injectable, OnChanges, OnDestroy, OnInit} from '@angular/core';
import {HttpService} from '../../../services/http.service';
import {CurrentStateService} from '../../../services/current-state.service';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs/Subscription';
import {SenseContent} from './sensecontent';
import {SidebarService} from '../../../services/sidebar.service';

@Component({
  selector: 'app-result',
  templateUrl: './result.component.html',
  styleUrls: ['./result.component.css']
})
export class ResultComponent implements OnInit, OnDestroy {
  content: SenseContent[];
  subscription: Subscription = null;

  lemmaId: number;
  yiddishContentPresent = false;
  relations= [];
  footerFirstTabSelected = true;
  showNothingFoundMsg = false;
  rightPanelHidden = false;
  footerPanelHidden = false;


  constructor(private http: HttpService,
              private sidebar: SidebarService,
              private route: ActivatedRoute
    ) { }

  ngOnInit() {
    this.content = [];
    const searchQueryParams = this.route.snapshot.queryParamMap;
    if (searchQueryParams.keys.length > 0) {
      // @ts-ignore - suppressing non existing error
      this.sidebar.loadOptionsFromParameters(searchQueryParams.params);
    }
    this.lemmaId = +this.route.snapshot.paramMap.get('lemma_id');
    this.subscription = this.route.params.subscribe(params => {
      const searchLemma = params['search_lemma'];
      if (searchLemma) {
        if (searchLemma === '*') {
          this.sidebar.getAllOptions({lemma: ''});
        }
        this.sidebar.getAllOptions({lemma: searchLemma});
      }

      this.lemmaId = +params['lemma_id']; // (+) converts string 'id' to a number
      if (!isNaN(this.lemmaId)) {
        this.showNothingFoundMsg = false;
        this.updateCurrentLexicalUnit(!searchLemma);
      } else {
        this.showNothingFoundMsg = true;
      }
    });
  }

  private updateCurrentLexicalUnit(isSearchFieldEmpty) {
    this.content = [];
    this.http.getLexicalUnitDetails(this.lemmaId).subscribe((response) => {
      if (response['Yiddish'].length > 0) {
        for(let i = 0; i < response['Yiddish'].length; i++) {
          this.yiddishContentPresent = true;
          this.content.push(new SenseContent(response, i));
        }
      } else {
        this.yiddishContentPresent = false;
        this.content.push(new SenseContent(response));
      }

      this.sidebar.assignSingleOptionIfEmpty(this.content[0]);
    });
    this.http.getSenseRelations(this.lemmaId).subscribe(results => {
      this.relations = results[0].concat(results[1]);

      this.relations = this.relations.map(rel => {
        const key = Object.keys(rel)[0]; // assuming only one key possible!
        return {'key': key, items: rel[key]};
      });
    });
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  footerTabChange(idx) {
    this.footerFirstTabSelected = idx === 0;
  }
}