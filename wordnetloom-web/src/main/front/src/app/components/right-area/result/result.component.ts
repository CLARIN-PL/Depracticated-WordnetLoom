import {Component, EventEmitter, OnDestroy, OnInit} from '@angular/core';
import {HttpService} from '../../../services/http.service';
import {CurrentStateService} from '../../../services/current-state.service';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs/Subscription';
import {SenseContent} from './sensecontent';
import {SidebarService} from '../../../services/sidebar.service';
import {GraphModalComponent} from '../../graph/graph-modal/graph-modal.component';
import {MatDialog} from '@angular/material';

@Component({
  selector: 'app-result',
  templateUrl: './result.component.html',
  styleUrls: ['./result.component.scss'],
  providers: [GraphModalComponent]
})
export class ResultComponent implements OnInit, OnDestroy {
  content: SenseContent[];
  routeSubscription: Subscription = null;
  synsetIdStateSubscription: Subscription = null;

  modalLabelEmitter = new EventEmitter<string>();

  synsetId: number;
  yiddishContentPresent = false;
  relations = {};
  footerFirstTabSelected = true;
  showNothingFoundMsg = false;
  rightPanelHidden = false;
  footerPanelHidden = false;

  mobile = true;
  mobileListener;

  constructor(private http: HttpService,
              private state: CurrentStateService,
              private sidebar: SidebarService,
              private route: ActivatedRoute,
              public graphModal: MatDialog,
    ) { }

  ngOnInit() {
    this.content = [];
    const searchQueryParams = this.route.snapshot.queryParamMap;
    if (searchQueryParams.keys.length > 0) {
      // @ts-ignore - suppressing non existing error
      this.sidebar.loadOptionsFromParameters(searchQueryParams.params);
    }

    this.synsetId = +this.route.snapshot.paramMap.get('lemma_id');
    this.state.setResultComponentRouteObserver(this.route);

    this.synsetId = this.state.getSynsetId();
    this.updateCurrentSynset(true);

    this.synsetIdStateSubscription = this.state.getSynsetIdEmitter().subscribe(id => {
      this.synsetId = id;
      this.updateCurrentSynset(true);
    });


    // todo -- delete this?
    this.routeSubscription = this.route.params.subscribe(params => {
      const searchLemma = params['search_lemma'];
      if (searchLemma) {
        if (searchLemma === '*') {
          this.sidebar.getAllOptions({lemma: ''});
        }
        this.sidebar.getAllOptions({lemma: searchLemma});
      }
    });

    this.mobile = this.state.getMobileState();
    this.mobileListener = this.state.getMobileStateEmitter().subscribe(state => {
      this.mobile = state;
    });
  }


  private updateCurrentSynset(isSearchFieldEmpty) {
    this.content = [];
    this.http.getSenseDetails(this.synsetId).subscribe((response) => {
      if (response['Yiddish'].length > 0) {
        for(let i = 0; i < response['Yiddish'].length; i++) {
          this.yiddishContentPresent = true;
          this.content.push(new SenseContent(response, i));
        }
      } else {
        this.yiddishContentPresent = false;
        this.content.push(new SenseContent(response));
      }

      this.modalLabelEmitter.emit(this.content[0].lemma);

      this.sidebar.assignSingleOptionIfEmpty(this.content[0]);
    });
    this.http.getSenseRelations(this.synsetId).subscribe(results => {
      this.relations = {};

      const mapRelsFcn = (rel) => {
        const key = Object.keys(rel)[0]; // assuming only one key possible!
        return {'key': key, items: rel[key]};
      };

      this.relations['incoming'] = results[0].map(mapRelsFcn);
      this.relations['outgoing'] = results[1].map(mapRelsFcn);
    });
  }

  ngOnDestroy() {
    this.routeSubscription.unsubscribe();
    this.synsetIdStateSubscription.unsubscribe();
  }

  footerTabChange(idx) {
    this.footerFirstTabSelected = idx === 0;
  }

  showGraphModal() {
    let topLabel = '';
    if (this.content.length > 0) {
      topLabel = this.content[0].lemma;
    }
    this.graphModal.open(GraphModalComponent, {
      maxWidth: '100vw',
      height: '99%',
      width: '99%',
      data: {
        topLabel: topLabel,
        topLabelEmitter: this.modalLabelEmitter
      }
    });
  }
}
