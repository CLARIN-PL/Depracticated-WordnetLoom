import {EventEmitter, Injectable} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

@Injectable()
export class CurrentStateService {
  private synsetId = null;
  private navbarOpenState: boolean;
  private sidebarSearchResultsPanelOpen: boolean;
  private mobileStateBreakPoint = 768;
  private mobileState: boolean;

  private synsetIdEmitter = new EventEmitter<any>();
  private navbarOpenEmitter = new EventEmitter<boolean>();
  private sidebarSearchResultsPanelOpenEmitter = new EventEmitter<boolean>();
  private mobileStateEmitter = new EventEmitter<boolean>();

  private routeObserver = null;

  constructor(private route: ActivatedRoute) {
    this.mobileState = window.innerWidth < this.mobileStateBreakPoint;
    window.addEventListener('resize', (event) => {
      this.onResize(event);
    });
  }

  setResultComponentRouteObserver(routeObserver) {
    this.routeObserver = routeObserver;
    this.routeObserver.params.subscribe( params => {
      const synsetId = params['lemma_id'];

      console.log(params);

      // if (isNaN(synsetId)) { // todo - check for isNaN unwanted behaviur
      if (synsetId === null || synsetId === 'not_found') {
        this.setSynsetId(null);
      } else {
        this.setSynsetId(synsetId);
      }
    });
  }

  onResize(event) {
    const newMobileState = window.innerWidth < this.mobileStateBreakPoint;
    if ( this.mobileState !== newMobileState ) {
      this.mobileState = newMobileState;
      this.mobileStateEmitter.emit(this.mobileState);
    }
  }
  // emitter getters
  getSynsetIdEmitter(): EventEmitter<any> {return this.synsetIdEmitter; }
  getNavbarOpenEmitter(): EventEmitter<any> {return this.navbarOpenEmitter; }
  getSidebarRearchResultsPanelOpenEmitter(): EventEmitter<any> {return this.sidebarSearchResultsPanelOpenEmitter; }
  getMobileStateEmitter(): EventEmitter<any> {return this.mobileStateEmitter; }

  // state getters
  getMobileState(): boolean { return this.mobileState; }
  getSynsetId() { return this.synsetId; }


  // setters
  setNavbarOpen(state: boolean): void {
    this.navbarOpenState = state;
    this.navbarOpenEmitter.emit(state);
  }

  setSidebarSearchResultPanelOpen(state: boolean): void {
    this.sidebarSearchResultsPanelOpen = state;
    this.sidebarSearchResultsPanelOpenEmitter.emit(state);
  }

  setSynsetId(id, graphInitiated = false) {
    if (this.synsetId !== id) {
      this.synsetId = id;
      this.synsetIdEmitter.emit([id, graphInitiated]);
    }
  }
}
