import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {HttpService} from '../../services/http.service';
import {SidebarService} from '../../services/sidebar.service';
import {TranslateService} from "../../services/translate.service";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  // @Input() showSearch = true;
  @Input() hideSecondRow = false;
  navbarOpen = false;

  constructor(private http: HttpService, private sidebar: SidebarService, private translate: TranslateService) { }

  toggleNavbar() {
    this.navbarOpen = !this.navbarOpen;
  }

  selectLang(lang) {
    this.translate.use(lang);
  }

  ngOnInit() {
    // this.hideOnLeave(this.el);// = () => {};
  }
}
