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
import {GraphService} from '../../graph/graph.service';
import {AvailableSearchFiltersService} from '../../../services/configuration/available-search-filters.service';


@Component({
  selector: 'app-result',
  templateUrl: './result.component.html',
  styleUrls: ['./result.component.scss'],
  providers: [GraphModalComponent]
})
export class ResultComponent implements OnInit, OnDestroy {
  content: SenseContent|YiddishContent;
  yiddishContent: YiddishContent[];
  currentYiddishTabIndex = 0;
  routeSubscription: Subscription = null;
  routeParamsSubscription: Subscription = null;
  selectedYiddishVariant: Number;
  synsetIdStateSubscription: Subscription = null;
  settingsDict: {};
  synsetData: {} = null;
  modalLabelEmitter = new EventEmitter<string>();

  synsetId: string;
  yiddishContentPresent = false;
  senseLoaded = false;
  relations: Object = null;
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
              private dictionaryItems: AvailableSearchFiltersService,
              public graphModal: MatDialog,

    ) { }

  ngOnInit() {
    this.content = null;
    this.yiddishContent = [];

    this.settingsDict = this.dictionaryItems.getSearchFields();

    console.log(this.settingsDict);

    const searchQueryParams = this.route.snapshot.queryParamMap;
    if (searchQueryParams.keys.length > 0) {
      // @ts-ignore - suppressing non existing error
      this.sidebar.loadOptionsFromParameters(searchQueryParams.params);
    }

    this.synsetId = this.route.snapshot.paramMap.get('lemma_id');
    // console.log(this.synsetId);
    this.state.setResultComponentRouteObserver(this.route);

    this.synsetId = this.state.getSynsetId();
    this.yiddishContentPresent = false;
    if (this.synsetId) {
      this.showNothingFoundMsg = false;
      this.updateCurrentSynset(true);
    } else {
      this.synsetData = null;
      this.relations = null;
      this.senseLoaded = false;
      this.showNothingFoundMsg = true;
    }

    this.synsetIdStateSubscription = this.state.getSynsetIdEmitter().subscribe(data => {
      const id = data[0];
      const graphInitiated = data[1];
      this.synsetId = id;

      if (!id) { // nothing found
        this.showNothingFoundMsg = true;
      } else {
        this.senseLoaded = false;
        this.showNothingFoundMsg = false;
        this.updateCurrentSynset(true, !graphInitiated);
      }
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

    this.routeParamsSubscription = this.route.queryParams.subscribe(params => {
      this.selectedYiddishVariant = +params['variant'];
      this.checkSelectedTab();
    });

    this.mobile = this.state.getMobileState();
    this.mobileListener = this.state.getMobileStateEmitter().subscribe(state => {
      this.mobile = state;
    });
  }

  private checkSelectedTab() {
    const tabToBeSelected = this.yiddishContent.findIndex(it => it.id === this.selectedYiddishVariant);
    if (tabToBeSelected > -1) {
      this.currentYiddishTabIndex = tabToBeSelected;
    }
  }

  private updateCurrentSynset(isSearchFieldEmpty, updateGraph= true) {
    this.content = null;
    this.yiddishContent = [];
    this.yiddishContentPresent = false;

    this.http.getSenseDetails(this.synsetId).subscribe((response) => {
      this.content = new SenseContent(response, null, this.settingsDict);
      this.senseLoaded = true;
      const originalSenseContent = this.content;

      this.modalLabelEmitter.emit(this.content.lemma);
      this.sidebar.assignSingleOptionIfEmpty(this.content);


      // load yiddish content
      if (response['_links']['yiddish']) {
        this.http.getYiddishDetails(this.synsetId).subscribe(response => {
          for (const yContent of response.rows) {
            this.yiddishContent.push(new YiddishContent(yContent, originalSenseContent, this.settingsDict));
          }
          if (response.rows.length > 0 ) {
            this.content = this.yiddishContent[0];
            this.yiddishContentPresent = true;
          }
          this.checkSelectedTab();
        });
      }

      if (response['_links']['synset']) {
        this.http.getAbsolute(response['_links']['synset']).subscribe(synsetResponse => {
          this.synsetData = synsetResponse;
          if (this.synsetData['senses']) {
            this.synsetData['senses'].splice(
              this.synsetData['senses'].findIndex(sense => {sense['id'] = this.content.senseId; }), 1
            );
          }
        });
      }

      if (updateGraph) {
        console.log(this.content.senseId);
        this.graph.initializeFromSynsetId(this.content.senseId);
      }
    });
    this.http.getSenseRelations(this.synsetId).subscribe(results => {
      this.relations = results;
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
        topLabelEmitter: this.modalLabelEmitter,
        senseId: this.content.senseId
      }
    });
  }
  selectedTabChange(event) {
    this.currentYiddishTabIndex = event.index - 1;
  }
}
