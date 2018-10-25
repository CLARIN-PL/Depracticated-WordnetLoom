import {Component, OnInit, ViewChild, OnDestroy} from '@angular/core';
import {SidebarService} from '../../services/sidebar.service';
import {ActivatedRoute} from '@angular/router';

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
  showingSideBar = true;

  constructor(private sidebarService: SidebarService, private route: ActivatedRoute) {}

  ngOnInit() {
    this.sidebarService.init(this.sidebarRef);
    this.sidebarContent = this.sidebarService.getList();

    this.sidebarObsv = this.sidebarService.getListObservable()
      .subscribe(data => {
        this.sidebarContent = data;
      });

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

  ngOnDestroy() {
    this.sidebarObsv.unsubscribe();
    this.sidebarLoadingObsv.unsubscribe();
  }

  hideSideBar() {
    this.showingSideBar = false;
  }

  showSideBar() {
    this.showingSideBar = true;
  }
  onSelectedListStyleChange(event) {
    console.log(event);
  }
}
