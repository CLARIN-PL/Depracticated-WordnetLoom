import {Component, Input, OnInit, OnDestroy} from '@angular/core';
import {HttpService} from '../../services/http.service';
import {SidebarService} from '../../services/sidebar.service';
import {TranslateService} from "../../services/translate.service";
import {CurrentStateService} from "../../services/current-state.service";
import {ActivatedRoute, NavigationEnd, Router} from "@angular/router";
import {filter} from "rxjs/operators";

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
  routeListener;
  isLandingPage: boolean;

  constructor(private state: CurrentStateService, private translate: TranslateService, private route: Router) { }

  ngOnInit() {
    this.isLandingPage = this.route.url === '/';
    this.routeListener = this.route.events.pipe(filter(event => event instanceof NavigationEnd))
      .subscribe(event => {
        this.isLandingPage = event['url'] === '/';
      });
    this.navbarOpenListener = this.state.getNavbarOpenEmitter().subscribe((state) => {
      this.navbarOpen = state;
    });
  }

  ngOnDestroy() {
    this.routeListener.unsubscribe();
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
