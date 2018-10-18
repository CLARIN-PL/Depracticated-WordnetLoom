import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {HttpService} from '../../services/http.service';
import {SidebarService} from '../../services/sidebar.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  // @Input() showSearch = true;
  @Input() hideSecondRow = false;
  navbarOpen = false;

  constructor(private http: HttpService, private sidebar: SidebarService) { }

  toggleNavbar() {
    this.navbarOpen = !this.navbarOpen;
  }

  ngOnInit() {
    // this.hideOnLeave(this.el);// = () => {};
  }
}
