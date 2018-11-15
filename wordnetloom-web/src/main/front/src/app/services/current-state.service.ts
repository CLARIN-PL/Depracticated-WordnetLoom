import {EventEmitter, Injectable, HostListener} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs';

@Injectable()
export class CurrentStateService {
  synsetId = null;
  synsetSubscription = new EventEmitter<any>();

  routeObserver = null;

  navbarOpenState: boolean;
  navbarOpenSubscription = new EventEmitter<boolean>();

  sidebarRearchResultsPanelOpen: boolean;
  sidebarRearchResultsPanelOpenSubscription = new EventEmitter<boolean>();

  mobileState: boolean;
  // mobileStateBreakPoint = 576;
  mobileStateBreakPoint = 768;
  mobileStateSubscription = new EventEmitter<boolean>();

  constructor(private route: ActivatedRoute) {
    this.mobileState = window.innerWidth < this.mobileStateBreakPoint;
    window.addEventListener('resize', (event) => {
      this.onResize(event);
    });
  }

  setResultComponentRouteObserver(routeObserver) {
    this.routeObserver = routeObserver;
    this.routeObserver.params.subscribe( params => {
      const synsetId = +params['lemma_id']; // (+) converts string 'id' to a number

      if (isNaN(synsetId)) {
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
      this.mobileStateSubscription.emit(this.mobileState);
    }
  }

  getMobileState(): boolean {
    return this.mobileState;
  }

  setNavbarOpen(state: boolean): void {
    this.navbarOpenState = state;
    this.navbarOpenSubscription.emit(state);
  }

  setSearchResultPanelOpen(state: boolean): void {
    this.sidebarRearchResultsPanelOpen = state;
    this.sidebarRearchResultsPanelOpenSubscription.emit(state);
  }

  setSynsetId(id) {
    if (this.synsetId === id) return;

    this.synsetId = id;
    this.synsetSubscription.emit(id);
  }

  getSynsetId() {
    return this.synsetId;
  }

  getSynsetIdSubscription() {
    return this.synsetSubscription;
  }
}
