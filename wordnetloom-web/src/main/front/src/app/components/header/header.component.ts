import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {HttpService} from "../../services/http.service";
import {SidebarService} from "../../services/sidebar.service";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  @Input() showSearch = true;
  @ViewChild('panel') el;
  @ViewChild('expandBtn') expandBtn;

  isPanelOpened = false;
  constructor(private http: HttpService, private sidebar: SidebarService) { }

  ngOnInit() {
    this.el.hideOnLeave = () => {};
  }

  panelToggle() {
    this.el.toggle();
    this.isPanelOpened = this.el._expanded;
  }

  simpleSearch(form) {
    this.sidebar.getAllOptions (form);
  }

  hideOnLeave(clickedElement) {
    const clickedInside = this.expandBtn._elementRef.nativeElement.contains(clickedElement);
    if (!clickedInside) {
      this.el.close();
      this.isPanelOpened = this.el._expanded;
    }
  }
}
