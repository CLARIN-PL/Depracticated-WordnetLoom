import {Component, OnInit, ViewChild} from '@angular/core';
import {HttpService} from "../../services/http.service";
import {SidebarService} from "../../services/sidebar.service";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  @ViewChild('panel') el;
  isPanelOpened = false;
  constructor(private http: HttpService, private sidebar: SidebarService) { }

  ngOnInit() {
    // console.log(this.el.opened());
    // console.log(this.el.opened.subscribe(() => {
    //   this.isPanelOpened = !this.el.closed.closed;
    //   console.log(this.isPanelOpened);
    // }));
  }

  panelToggle() {
    this.el.toggle();
    this.isPanelOpened = this.el._expanded;
  }

  simpleSearch(form) {
    this.sidebar.getAllOptions (form);
  }


}
