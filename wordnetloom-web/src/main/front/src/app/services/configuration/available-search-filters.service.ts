import { Injectable } from '@angular/core';
import {HttpService} from '../http.service';

@Injectable()
export class AvailableSearchFiltersService {
  searchFields: {[id: string]: Object} = {};
  searchByString: {} = {};
  initialized = false;

  constructor(private http: HttpService) {
    this.searchByString = {apiOptions: 'stringType', name: 'String type', queryString: 'stringType',
      searchOptions: [
        // {id: 1, name: 'YIVO Spelling', description: ''},
        // {id: 2, name: 'Yiddish', description: ''},
        // {id: 3, name: 'Philological', description: ''},
        // {id: 4, name: 'Transcription', description: ''},
        // {id: 5, name: 'Phonetic', description: ''},
        // {id: 6, name: 'Transcription', description: ''},
        {id: 7, name: 'Etymological Root', description: ''}, // wajha, wpisane z palca
        {id: 8, name: 'Prefix', description: ''}, // na dół
        {id: 9, name: 'Root', description: ''}, // na dół
        {id: 10, name: 'Suffix', description: ''}, // na dół
        {id: 11, name: 'Constituent', description: ''}
      ]};
    this.searchFields['lexicon'] =               {apiOptions: 'lexicons', name: 'Lexicon', queryString: 'lexicon', searchOptions: [] };
    this.searchFields['partOfSpeech'] =          {apiOptions: 'dictionaries/parts-of-speech', name: 'Part of Speech', queryString: 'part_of_speech', searchOptions: [] };
    this.searchFields['domain'] =                {apiOptions: 'dictionaries/domains', name: 'Domain', queryString: 'domain', searchOptions: [] };
    this.searchFields['style'] =                 {apiOptions: 'dictionaries/styles', name: 'Style', queryString: 'style', searchOptions: [] }; // nie ma
    this.searchFields['status'] =                {apiOptions: 'dictionaries/statuses', name: 'Status', queryString: 'status', searchOptions: [] };
    this.searchFields['yiddishStatus'] =         {apiOptions: 'dictionaries/yiddish-statuses', name: 'Status', queryString: 'yiddish_status', searchOptions: [] };
    this.searchFields['lexicalCharacteristic'] = {apiOptions: 'dictionaries/lexical-characteristics', name: 'Lexical characteristic', queryString: 'lexical_characteristic', searchOptions: [] }; // nie ma
    this.searchFields['age'] =                   {apiOptions: 'dictionaries/ages', name: 'Age', queryString: 'age', searchOptions: [] }; // nie ma
    this.searchFields['source'] =                {apiOptions: 'dictionaries/sources', name: 'Source', queryString: 'source', searchOptions: [] }; // nie ma
    this.searchFields['yiddishDomain'] =         {apiOptions: 'dictionaries/yiddish-domains', name: 'Semantic field', queryString: 'yiddish_domain', searchOptions: [] }; // nie ma
    this.searchFields['yiddishDomainModifier'] = {apiOptions: 'dictionaries/yiddish-domain-modifiers', name: 'Semantic field modifier', queryString: 'yiddish_domain_modification', searchOptions: [] }; // nie ma

    this.searchFields['prefix'] = {apiOptions: 'dictionaries/prefixes', name: 'Prefix', queryString: 'particle_prefix', searchOptions: [] };
    this.searchFields['suffix'] = {apiOptions: 'dictionaries/suffixes', name: 'Suffix', queryString: 'particle_suffix', searchOptions: [] };

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

  getSearchByString() {
    return this.searchByString;
  }

  getSearchFields() {
    return this.searchFields;
  }
}
