import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {CurrentStateService} from '../../../services/current-state.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-search-result',
  templateUrl: './search-result.component.html',
  styleUrls: ['./search-result.component.css']
})
export class SearchResultComponent implements OnInit {
  @Input() options;
  cnt = 0;
  constructor(private currentState: CurrentStateService, private router: Router) { }


  ngOnInit() {
  }

  optionSelected(id) {
    // this.cnt++;
    this.router.navigate([id]);
  }

}
