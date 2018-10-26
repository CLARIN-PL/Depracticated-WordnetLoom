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
  sideBarListStyle = 'yiddish';
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

    this.sidebarOpenListener = this.state.sidebarRearchResultsPanelOpenSubscription.subscribe(
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
  }

  optionListScrollBottom(event) {
    this.sidebarService.loadMoreOptions();
  }

  optionSelected() {
    console.log('option selected');
    if (this.state.getMobileState()) {
      console.log('mobile state active');
      this.state.setSearchResultPanelOpen(false);
    }
  }

  ngOnDestroy() {
    this.sidebarObsv.unsubscribe();
    this.sidebarLoadingObsv.unsubscribe();
    this.sidebarOpenListener.unsubscribe();
  }

  hideSideBar() {
    // this.showingSideBar = false;
    this.state.setSearchResultPanelOpen(false);
  }

  showSideBar() {
    // this.showingSideBar = true;
    this.state.setSearchResultPanelOpen(true);
  }
  onSelectedListStyleChange(event) {
    console.log(event);
  }
}
