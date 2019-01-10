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

  advancedSearchKeys = [
    'partOfSpeech',
    'style',
    'yiddishStatus',
    'lexicalCharacteristic',
    'age',
    'source',
    'yiddishDomain',
    'yiddishDomainModifier'
  ];

  basicSearchKeys = [
    'prefixes',
    'suffixes',
    'particle_root',
    'etymological_root',
    'particle_constituent'
  ];

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
    console.log(this.form);
    console.log(this.form.value);
    return this.form.value;

    // const form = this.form.value;
    // delete form['selectedSearchTypes'];  // todo -change, only temporary
    // return form;
  }



}
