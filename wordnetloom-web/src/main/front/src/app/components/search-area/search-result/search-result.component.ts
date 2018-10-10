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
  constructor(private currentState: CurrentStateService, private router: Router) { }

  ngOnInit() {
  }

  optionSelected(id) {
    this.router.navigate([id]);
  }

}
