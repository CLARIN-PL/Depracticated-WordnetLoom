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

  advancedSearchKeys = [
    'partOfSpeech',
    'style',
    'yiddishStatus',
    'lexicalCharacteristic',
    'yiddishDomain',
    'yiddishDomainModifier',
    'gramaticalGender'
  ];

  basicSearchKeys = [
    'prefixes',
    'suffixes',
    'particle_root',
    'etymological_root',
    'particle_constituent'
  ];

  ngOnInit() {
    this.assignSearchFields();
  }

  private assignSearchFields() {
    this.searchFields = this.availableSearchFilters.getSearchFields();
  }

  get() {
    return this.form.value;
  }



}
