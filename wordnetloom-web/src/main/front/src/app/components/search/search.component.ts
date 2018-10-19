import {Component, OnInit, ViewChild} from '@angular/core';
import { HttpService } from '../../services/http.service';
import {NgForm} from '@angular/forms';
import {AvailableSearchFiltersService} from '../../services/configuration/available-search-filters.service';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {
  @ViewChild('form') form: NgForm;

  constructor(private http: HttpService, private availableSearchFilters: AvailableSearchFiltersService) { }

  searchFields: {[id: string]: Object} = {};
  searchByString: {} = {};

  searchKeys = ['lexiconId', 'partOfSpeechId', 'domainId', 'styleId', 'statusId', 'lexicalCharacteristicId', 'ageId',
    'sourceId', 'yiddishDomainId', 'domainModifierId'];

  ngOnInit() {
    this.assignSearchByFields();
    this.assignSearchFields();
  }

  private assignSearchByFields() {
    this.searchByString = this.availableSearchFilters.getSearchByString();
  }

  private assignSearchFields() {
    this.searchFields = this.availableSearchFilters.getSearchFields();
  }

  get() {
    return this.form.value;

    const form = this.form.value;
    delete form['selectedSearchTypes'];  // todo -change, only temporary
    return form;
  }
}
