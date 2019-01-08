import {Component, OnInit, ViewChild, OnDestroy} from '@angular/core';
import {SidebarService} from '../../services/sidebar.service';
import {ActivatedRoute} from '@angular/router';
import {CurrentStateService} from "../../services/current-state.service";

@Component({
  selector: 'app-unit',
  templateUrl: './unit.component.html',
  styleUrls: ['./unit.component.scss']
})
export class UnitComponent implements OnInit, OnDestroy {

  @ViewChild('sidebar') sidebarRef;
  sidebarObsv;
  sidebarLoadingObsv;
  sidebarContent = [];
  sideBarListStyle = 'yivo';
  sideBarListLoading = false;
  recordsInfo = null;

  sidebarOpenListener;
  showingSideBar = true;

  constructor(private sidebarService: SidebarService, private state: CurrentStateService) {}

  ngOnInit() {
    this.sidebarService.init(this.sidebarRef);
    this.sidebarContent = this.sidebarService.getList();

    this.sidebarObsv = this.sidebarService.getListObservable()
      .subscribe(data => {
        this.sidebarContent = data;
      });

    this.sidebarOpenListener = this.state.getSidebarRearchResultsPanelOpenEmitter().subscribe(
      (state) => {
        this.showingSideBar = state;
      }
    );

    // timeout to deal with racing condition (angular error)
    setTimeout(() => {
      this.sidebarLoadingObsv = this.sidebarService.getListLoadinObservable()
        .subscribe(data => {
          this.sideBarListLoading = data.loading;
          this.recordsInfo = data.recordsStr;
        });
    }, 20);

    console.log(this.sidebarContent);
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
  }

  hideSideBar() {
    // this.showingSideBar = false;
    this.state.setSidebarSearchResultPanelOpen(false);
  }

  showSideBar() {
    // this.showingSideBar = true;
    this.state.setSidebarSearchResultPanelOpen(true);
  }
  onSelectedListStyleChange(event) {
    console.log(event);
  }
}
