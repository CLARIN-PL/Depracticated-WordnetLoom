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
  routeParamsSubscription: Subscription = null;
  selectedYiddishVariant: Number;
  senseIdStateSubscription: Subscription = null;
  listAlphabetStyleSubscription: Subscription = null;
  settingsDict: {};
  synsetData: {} = null;
  synonyms: Object[] = null;
  modalLabelEmitter = new EventEmitter<string>();

  senseId: string;
  listAlphabetStyle = 'latin';
  yiddishContentPresent = false;
  senseLoaded = false;
  relations: Object = null;
  footerFirstTabSelected = true;
  showNothingFoundMsg = false;
  rightPanelOpen;
  rightPanelOpenListener: Subscription = null;
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

    const searchQueryParams = this.route.snapshot.queryParamMap;
    if (searchQueryParams.keys.length > 0) {
      // @ts-ignore - suppressing non existing error
      this.sidebar.loadOptionsFromParameters(searchQueryParams.params);
    }

    this.senseId = this.route.snapshot.paramMap.get('lemma_id');
    this.state.setResultComponentRouteObserver(this.route);

    this.senseId = this.state.getSenseId();
    this.yiddishContentPresent = false;
    if (this.senseId) {
      this.showNothingFoundMsg = false;
      this.updateCurrentSense(true);
    } else {
      this.synsetData = null;
      this.relations = null;
      this.senseLoaded = false;
      this.showNothingFoundMsg = true;
    }

    this.senseIdStateSubscription = this.state.getSenseIdEmitter().subscribe(data => {
      const id = data[0];
      const graphInitiated = data[1];
      this.senseId = id;
      if (!id) { // nothing found
        this.showNothingFoundMsg = true;
      } else {
        this.senseLoaded = false;
        this.showNothingFoundMsg = false;
        this.updateCurrentSense(true, !graphInitiated);
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

    this.listAlphabetStyle = this.state.getListAlphabetStyle();
    this.listAlphabetStyleSubscription = this.state.getListAlphabetStyleEmitter().subscribe(state => {
      this.listAlphabetStyle = state;
    });

    this.rightPanelOpen = this.state.getRightDetailPanelOpen();
    this.rightPanelOpenListener = this.state.getRightDetailPanelOpenEmitter().subscribe(panelOpen => {
      this.rightPanelOpen = panelOpen;
    });
  }

  private checkSelectedTab() {
    const tabToBeSelected = this.yiddishContent.findIndex(it => it.id === this.selectedYiddishVariant);
    if (tabToBeSelected > -1) {
      this.currentYiddishTabIndex = tabToBeSelected;
    }
  }

  private setYiddishPrimaryFirst(variants: any[]) {
    if (variants.length === 0 || variants[0].variant_type === 'Yiddish_Primary_Lemma') {
      return variants;
    }
    const yiddishIdx = variants.findIndex(variant => variant.variant_type === 'Yiddish_Primary_Lemma');
    variants.unshift(variants.splice(yiddishIdx, 1)[0]); // return Yiddish primary lemma as first
    return variants;
  }

  private updateCurrentSense(isSearchFieldEmpty, updateGraph= true) {
    this.content = null;
    this.yiddishContent = [];
    this.yiddishContentPresent = false;

    this.http.getSenseDetails(this.senseId).subscribe((response) => {
      this.content = new SenseContent(response, null, this.settingsDict);
      this.senseLoaded = true;
      this.synonyms = null;
      const originalSenseContent = this.content;

      this.modalLabelEmitter.emit(this.content.lemma);
      this.sidebar.assignSingleOptionIfEmpty(this.content);


      // load yiddish content
      if (response['_links']['yiddish']) {
        this.http.getYiddishDetails(this.senseId).subscribe(response => {
          response.rows = this.setYiddishPrimaryFirst(response.rows);

          for (const yContent of response.rows) {
            this.yiddishContent.push(new YiddishContent(yContent, originalSenseContent, this.settingsDict, this.http));
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
            this.synonyms = this.synsetData['senses'].filter(sense => sense['id'] !== this.senseId);
          }
        });
      }

      if (updateGraph && !this.mobile) {
        this.graph.initializeFromSenseId(this.senseId);
      }
    });
    this.http.getSenseRelations(this.senseId).subscribe(results => {
      this.relations = results;
    });
  }

  ngOnDestroy() {
    this.senseIdStateSubscription.unsubscribe();
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
        senseId: this.senseId
      }
    });
  }
  selectedTabChange(event) {
    this.currentYiddishTabIndex = event.index - 1;
  }

  toggleRightPanelOpen() {
    this.state.setRightDetailPanelOpen(!this.rightPanelOpen);
    console.log(this.rightPanelOpen);
  }
}
