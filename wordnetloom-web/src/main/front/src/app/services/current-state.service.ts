import {EventEmitter, Injectable} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

@Injectable()
export class CurrentStateService {
  private senseId = null;
  private navbarOpenState: boolean;
  private sidebarSearchResultsPanelOpen: boolean;
  private mobileStateBreakPoint = 768;
  private mobileState: boolean;

  private senseIdEmitter = new EventEmitter<any>();
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
      const senseId = params['lemma_id'];

      if (senseId === null || senseId === 'not_found') {
        this.setSenseId(null, true);
      } else {
        this.setSenseId(senseId);
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
  getSenseIdEmitter(): EventEmitter<any> {return this.senseIdEmitter; }
  getNavbarOpenEmitter(): EventEmitter<any> {return this.navbarOpenEmitter; }
  getSidebarRearchResultsPanelOpenEmitter(): EventEmitter<any> {return this.sidebarSearchResultsPanelOpenEmitter; }
  getMobileStateEmitter(): EventEmitter<any> {return this.mobileStateEmitter; }

  // state getters
  getMobileState(): boolean { return this.mobileState; }
  getSenseId() { return this.senseId; }


  // setters
  setNavbarOpen(state: boolean): void {
    this.navbarOpenState = state;
    this.navbarOpenEmitter.emit(state);
  }

  setSidebarSearchResultPanelOpen(state: boolean): void {
    this.sidebarSearchResultsPanelOpen = state;
    this.sidebarSearchResultsPanelOpenEmitter.emit(state);
  }

  setSenseId(id, graphInitiated = false) {
    if (this.senseId !== id) {
      this.senseId = id;
      this.senseIdEmitter.emit([id, graphInitiated]);
    }
  }
}
