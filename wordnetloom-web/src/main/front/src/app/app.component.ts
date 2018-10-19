import {Component, OnInit } from '@angular/core';
import {ActivatedRoute, NavigationEnd, ParamMap, Router} from '@angular/router';
import {TranslateService} from './services/translate.service';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  showSearchInHeader = true;

  constructor(private router: Router, private translate: TranslateService) {}

  ngOnInit() {
    this.initShowingHeaderSearchBar();
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

