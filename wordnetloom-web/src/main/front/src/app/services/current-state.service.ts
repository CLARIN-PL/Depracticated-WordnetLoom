import {EventEmitter, Injectable, HostListener} from '@angular/core';

@Injectable()
export class CurrentStateService {
  lexicalUnitId = null;
  lexicalUnitSubscription = new EventEmitter<any>();

  navbarOpenState: boolean;
  navbarOpenSubscription = new EventEmitter<boolean>();

  sidebarRearchResultsPanelOpen: boolean;
  sidebarRearchResultsPanelOpenSubscription = new EventEmitter<boolean>();

  mobileState: boolean;
  // mobileStateBreakPoint = 576;
  mobileStateBreakPoint = 768;
  mobileStateSubscription = new EventEmitter<boolean>();


  constructor() {
    this.mobileState = window.innerWidth < this.mobileStateBreakPoint;
    window.addEventListener('resize', (event) => {
      this.onResize(event);
    });
  }

  onResize(event) {
    const newMobileState = window.innerWidth < this.mobileStateBreakPoint;
    if ( this.mobileState !== newMobileState ){
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

  setLexicalUnit(id) {
    this.lexicalUnitId = id;
    this.lexicalUnitSubscription.emit(id);
  }

  getLexicalUnitId() {
    return this.lexicalUnitId;
  }

  getLexicalUnitIdSubscription() {
    return this.lexicalUnitSubscription;
  }
}
