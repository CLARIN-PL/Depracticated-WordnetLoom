import {Component, OnInit, ViewChild, OnDestroy} from '@angular/core';
import {SidebarService} from '../../services/sidebar.service';
import {ActivatedRoute} from '@angular/router';
import {CurrentStateService} from '../../services/current-state.service';

@Component({
  selector: 'app-unit',
  templateUrl: './unit.component.html',
  styleUrls: ['./unit.component.scss']
})
export class UnitComponent implements OnInit, OnDestroy {

  @ViewChild('sidebar') sidebarRef;
  sidebarObsv;
  sidebarLoadingObsv;
  sideBarListStyleListener;
  sidebarContent = [];
  sideBarListStyle = 'yivo';
  sideBarListLoading = false;
  recordsInfo = null;

  sidebarOpenListener;
  showingSideBar = true;

  mobile = false; // mobile state - obtained only once at init

  constructor(private sidebarService: SidebarService, private state: CurrentStateService) {}

  ngOnInit() {
    this.sidebarService.init(this.sidebarRef);
    this.sidebarContent = this.sidebarService.getList();

    this.sidebarObsv = this.sidebarService.getListObservable()
      .subscribe(data => {
        this.sidebarContent = data;
      });

    this.showingSideBar = this.state.getSidebarRearchResultsPanelOpen();
    this.sidebarOpenListener = this.state.getSidebarRearchResultsPanelOpenEmitter().subscribe(
      (state) => {
        this.showingSideBar = state;
      }
    );

    this.sideBarListStyleListener = this.state.getListAlphabetStyleEmitter().subscribe(state => {
      this.sideBarListStyle = state;
    });

    this.mobile = this.state.getMobileState();

    // timeout to deal with racing condition (angular error)
    setTimeout(() => {
      this.sidebarLoadingObsv = this.sidebarService.getListLoadinObservable()
        .subscribe(data => {
          this.sideBarListLoading = data.loading;
          this.recordsInfo = data.recordsStr;
        });
    }, 20);
  }

  optionListScrollBottom(event) {
    this.sidebarService.loadMoreOptions();
  }

  optionSelected() {
    if (this.state.getMobileState()) {
      this.state.setSidebarSearchResultPanelOpen(false);
    }
  }

  ngOnDestroy() {
    this.sidebarObsv.unsubscribe();
    this.sidebarLoadingObsv.unsubscribe();
    this.sidebarOpenListener.unsubscribe();
    this.sideBarListStyleListener.unsubscribe();
  }

  toggleSidebarShowing() {
    this.state.setSidebarSearchResultPanelOpen(!this.showingSideBar);
  }

  hideSideBar() {
    this.state.setSidebarSearchResultPanelOpen(false);
  }

  showSideBar() {
    this.state.setSidebarSearchResultPanelOpen(true);
  }

  onSelectedListStyleChange(event) {
    this.state.setListAlphabetStyle(event.value);
  }
}
