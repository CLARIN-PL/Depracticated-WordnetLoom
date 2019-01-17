import {Component, OnInit } from '@angular/core';
import {ActivatedRoute, NavigationEnd, ParamMap, Router} from '@angular/router';
import {TranslateService} from './services/translate.service';
import {CurrentStateService} from './services/current-state.service';
import {Subscription} from 'rxjs';
import { LoadingBarService } from '@ngx-loading-bar/core';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  showSearchInHeader = true;
  mobile = true;
  mobileStateSubscription: Subscription = null;
  constructor(private router: Router,
              private translate: TranslateService,
              private currentState: CurrentStateService,
              private loadingBar: LoadingBarService) {}

  ngOnInit() {
    this.initShowingHeaderSearchBar();
    this.mobileStateSubscription = this.currentState.getMobileStateEmitter().subscribe(
      mobileState => this.mobile = mobileState
      );

    // this.loadingBar.start();

  }

  initShowingHeaderSearchBar() {
    this.showSearchInHeader = (this.router.url !== '/');
    this.router.events.subscribe((event) => {
      if (!(event instanceof NavigationEnd)) {
        return;
      }
      this.showSearchInHeader = (event['url'] !== '/');  // hide search header on main page
    });
  }
}

