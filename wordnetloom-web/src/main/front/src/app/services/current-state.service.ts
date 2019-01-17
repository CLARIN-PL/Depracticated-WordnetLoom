import {EventEmitter, Injectable} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

@Injectable()
export class CurrentStateService {

  private mobileStateBreakPoint = 768;
  private state = {
    senseId:                       {value: null, persistent: false},
    navbarOpenState:               {value: false, persistent: false},
    sidebarSearchResultsPanelOpen: {value: true, persistent: true},
    rightDetailPanelOpen:          {value: true, persistent: true},
    mobileState:                   {value: true, persistent: false},
    listAlphabetStyle:             {value: 'yivo', persistent: true}
  };

  private senseIdEmitter = new EventEmitter<any>();
  private navbarOpenEmitter = new EventEmitter<boolean>();
  private sidebarSearchResultsPanelOpenEmitter = new EventEmitter<boolean>();
  private mobileStateEmitter = new EventEmitter<boolean>();
  private listAlphabetStyleEmitter = new EventEmitter<string>();
  private rightDetailPanelOpenEmitter = new EventEmitter<boolean>();

  private routeObserver = null;

  constructor(private route: ActivatedRoute) {
    this.state.mobileState.value = window.innerWidth < this.mobileStateBreakPoint;
    window.addEventListener('resize', (event) => {
      this.onResize(event);
    });
    this.initStateFromLocalStorage();
  }

  private saveStateToLocalStorage() {
    localStorage.setItem('yiddish-dictionary-state', JSON.stringify(this.state));
  }

  private initStateFromLocalStorage() {
    const data = JSON.parse(localStorage.getItem('yiddish-dictionary-state'));
    if (!data) {
      return;
    }
    for (const key in data) {
      if (this.state[key].persistent) {
        this.state[key].value = data[key].value;
      }
    }
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
    if ( this.state.mobileState.value !== newMobileState ) {
      this.state.mobileState.value = newMobileState;
      this.mobileStateEmitter.emit(this.state.mobileState.value);
    }
  }
  // emitter getters
  getSenseIdEmitter(): EventEmitter<any> {return this.senseIdEmitter; }
  getNavbarOpenEmitter(): EventEmitter<any> {return this.navbarOpenEmitter; }
  getSidebarRearchResultsPanelOpenEmitter(): EventEmitter<any> {return this.sidebarSearchResultsPanelOpenEmitter; }
  getMobileStateEmitter(): EventEmitter<any> {return this.mobileStateEmitter; }
  getListAlphabetStyleEmitter(): EventEmitter<any> {return this.listAlphabetStyleEmitter; }
  getRightDetailPanelOpenEmitter(): EventEmitter<any> {return this.rightDetailPanelOpenEmitter; }

  // state getters
  getMobileState(): boolean { return this.state.mobileState.value; }
  getSidebarRearchResultsPanelOpen(): boolean { return this.state.sidebarSearchResultsPanelOpen.value; }
  getSenseId() { return this.state.senseId.value; }
  getListAlphabetStyle() { return this.state.listAlphabetStyle.value; }
  getRightDetailPanelOpen() { return this.state.rightDetailPanelOpen.value; }

  // setters
  setNavbarOpen(state: boolean): void {
    this.state.navbarOpenState.value = state;
    this.navbarOpenEmitter.emit(state);
  }

  setSidebarSearchResultPanelOpen(state: boolean): void {
    this.state.sidebarSearchResultsPanelOpen.value = state;
    this.sidebarSearchResultsPanelOpenEmitter.emit(state);
    this.saveStateToLocalStorage();
  }

  setSenseId(id, graphInitiated = false) {
    if (this.state.senseId.value !== id) {
      this.state.senseId.value = id;
      this.senseIdEmitter.emit([id, graphInitiated]);
    }
  }

  setListAlphabetStyle(newState) {
    this.state.listAlphabetStyle.value = newState;
    this.listAlphabetStyleEmitter.emit(newState);
    this.saveStateToLocalStorage();
  }

  setRightDetailPanelOpen(open: boolean): void {
    this.state.rightDetailPanelOpen.value = open;
    this.rightDetailPanelOpenEmitter.emit(open);
    this.saveStateToLocalStorage();
  }
}
