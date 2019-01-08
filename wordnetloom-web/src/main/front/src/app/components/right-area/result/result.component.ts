import {Component, EventEmitter, OnDestroy, OnInit} from '@angular/core';
import {HttpService} from '../../../services/http.service';
import {CurrentStateService} from '../../../services/current-state.service';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs/Subscription';
import {SenseContent} from './sensecontent';
import {YiddishContent} from './yiddishcontent';
import {SidebarService} from '../../../services/sidebar.service';
import {GraphModalComponent} from '../../graph/graph-modal/graph-modal.component';
import {MatDialog} from '@angular/material';
import {GraphService} from "../../graph/graph.service";

@Component({
  selector: 'app-result',
  templateUrl: './result.component.html',
  styleUrls: ['./result.component.scss'],
  providers: [GraphModalComponent]
})
export class ResultComponent implements OnInit, OnDestroy {
  content: SenseContent|YiddishContent;
  yiddishContent: YiddishContent[];
  routeSubscription: Subscription = null;
  synsetIdStateSubscription: Subscription = null;

  modalLabelEmitter = new EventEmitter<string>();

  synsetId: string;
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
              private graph: GraphService,
              public graphModal: MatDialog,
    ) { }

  ngOnInit() {
    // this.content = [];
    this.content = null;
    this.yiddishContent = [];
    const searchQueryParams = this.route.snapshot.queryParamMap;
    if (searchQueryParams.keys.length > 0) {
      // @ts-ignore - suppressing non existing error
      this.sidebar.loadOptionsFromParameters(searchQueryParams.params);
    }

    this.synsetId = this.route.snapshot.paramMap.get('lemma_id');
    console.log(this.synsetId);
    this.state.setResultComponentRouteObserver(this.route);

    this.synsetId = this.state.getSynsetId();
    console.log(this.synsetId);
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
    this.content = null;
    this.yiddishContent = [];
    this.yiddishContentPresent = false;

    this.http.getSenseDetails(this.synsetId).subscribe((response) => {
      console.log(response);
      this.content = new SenseContent(response);

      this.modalLabelEmitter.emit(this.content.lemma);
      this.sidebar.assignSingleOptionIfEmpty(this.content);

      // if (response['Yiddish'].length > 0) {

      if (response['_links']['yiddish']) {
        console.log('yiddish content present');
        // getting yiddish contnent
        this.http.getYiddishDetails(this.synsetId).subscribe(response => {
          console.log(response);
          for (const yContent of response.rows) {
            this.yiddishContent.push(new YiddishContent(yContent))
          }
          if (response.rows.length > 0 ){
            this.content = this.yiddishContent[0];
            this.yiddishContentPresent = true;
          }
          // this.yiddishContent = yiddishContent;
        });
      } else {
        console.log('yiddish content not present');
      }
      this.graph.initializeFromSynsetId(this.content.senseId);
    });
    this.http.getSenseRelations(this.synsetId).subscribe(results => {
      this.relations = results;
      // return;
      // this.relations = {};
      //
      // const mapRelsFcn = (rel) => {
      //   const key = Object.keys(rel)[0]; // assuming only one key possible!
      //   return {'key': key, items: rel[key]};
      // };
      //
      // this.relations['incoming'] = results[0].map(mapRelsFcn);
      // this.relations['outgoing'] = results[1].map(mapRelsFcn);
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
    if (this.content) {
      topLabel = this.content.lemma;
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
