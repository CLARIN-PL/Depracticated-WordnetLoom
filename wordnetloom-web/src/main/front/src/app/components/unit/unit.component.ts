import {Component, OnInit, ViewChild} from '@angular/core';
import {SidebarService} from "../../services/sidebar.service";

@Component({
  selector: 'app-unit',
  templateUrl: './unit.component.html',
  styleUrls: ['./unit.component.css']
})
export class UnitComponent implements OnInit {

  @ViewChild('sidebar') sidebarRef;
  sidebarObsv;
  sidebarContent = [];

  constructor(private sidebarService: SidebarService) {}

  ngOnInit() {
    this.sidebarService.init(this.sidebarRef);
    this.sidebarObsv = this.sidebarService.getListObservable()
      .subscribe(data => {
        console.log(data);
        this.sidebarContent = data;
      });
  }

}
