import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {SidebarService} from "./services/sidebar.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
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

