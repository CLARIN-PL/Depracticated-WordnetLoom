import {Component, Input, OnInit, OnDestroy} from '@angular/core';
import {HttpService} from '../../services/http.service';
import {SidebarService} from '../../services/sidebar.service';
import {TranslateService} from "../../services/translate.service";
import {CurrentStateService} from "../../services/current-state.service";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit, OnDestroy {
  // @Input() showSearch = true;
  @Input() hideSecondRow = false;
  navbarOpen = false;
  navbarOpenListener;


  constructor(private state: CurrentStateService, private translate: TranslateService) { }

  ngOnInit() {
    this.navbarOpenListener = this.state.getNavbarOpenEmitter().subscribe((state) => {
      this.navbarOpen = state;
    });
  }

  ngOnDestroy() {
    this.navbarOpenListener.unsubscribe();
  }

  toggleNavbar() {
    this.state.setNavbarOpen(!this.navbarOpen);
  }

  selectLang(lang) {
    this.translate.use(lang);
  }

  hideNav() {
    this.state.setNavbarOpen(false);
  }

}
