import { Injectable } from '@angular/core';
import {HttpService} from '../http.service';

@Injectable()
export class AvailableSearchFiltersService {
  searchFields: {[id: string]: Object} = {};
  // searchByString: {} = {};
  initialized = false;

  constructor(private http: HttpService) {
    this.searchFields = {
      'lexicon':               {apiOptions: 'lexicons', name: 'Lexicon', queryString: 'lexicon', searchOptions: [] },
      'partOfSpeech':          {apiOptions: 'dictionaries/parts-of-speech', name: 'Part of Speech', queryString: 'part_of_speech', searchOptions: [] },
      'domain':                {apiOptions: 'dictionaries/domains', name: 'Domain', queryString: 'domain', searchOptions: [] },
      'style':                 {apiOptions: 'dictionaries/styles', name: 'Style', queryString: 'style', searchOptions: [] },
      'status':                {apiOptions: 'dictionaries/statuses', name: 'Status', queryString: 'status', searchOptions: [] },
      'yiddishStatus':         {apiOptions: 'dictionaries/yiddish-statuses', name: 'Rootedness', queryString: 'yiddish_status', searchOptions: [] },
      'lexicalCharacteristic': {apiOptions: 'dictionaries/lexical-characteristics', name: 'Lexical characteristic', queryString: 'lexical_characteristic', searchOptions: [] },
      'age':                   {apiOptions: 'dictionaries/ages', name: 'Age', queryString: 'age', searchOptions: [] },
      'source':                {apiOptions: 'dictionaries/sources', name: 'Source', queryString: 'source', searchOptions: [] },
      'yiddishDomain':         {apiOptions: 'dictionaries/yiddish-domains', name: 'Semantic field', queryString: 'yiddish_domain', searchOptions: [] },
      'yiddishDomainModifier': {apiOptions: 'dictionaries/yiddish-domain-modifiers', name: 'Semantic field modifier', queryString: 'yiddish_domain_modification', searchOptions: [] },
      'prefix':                {apiOptions: 'dictionaries/prefixes', name: 'Prefix', queryString: 'particle_prefix', searchOptions: [] },
      'suffix':                {apiOptions: 'dictionaries/suffixes', name: 'Suffix', queryString: 'particle_suffix', searchOptions: [] },
      'gramaticalGender':      {apiOptions: 'dictionaries/grammatical-genders', name: 'Grammatical qualifiers', queryString: 'grammatical_gender', searchOptions: [] },
    };
    this.assignSearchFields();
  }

  private assignSearchFields() {
    for (const key in this.searchFields) {
      const field =  this.searchFields[key];
      this.http.getGlobalOptions(field['apiOptions']).subscribe(
        (response) => {
          // this.searchFields[key]['searchOptions'] = response.entries;
          this.searchFields[key]['searchOptions'] = response;
        },
        (error) => {
          console.log(error);
        }
      );
    }
    this.initialized = true;
  }

  isInitialized() {
    return this.initialized;
  }

  getSearchFields() {
    return this.searchFields;
  }
}
