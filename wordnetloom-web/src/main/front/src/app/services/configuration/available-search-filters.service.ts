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
    this.searchFields['partOfSpeechId'] =          {apiOptions: 'dictionaries/parts-of-speech', name: 'Part of Speech', queryString: 'part_of_speech', searchOptions: [] };
    this.searchFields['domainId'] =                {apiOptions: 'dictionaries/domains', name: 'Domain', queryString: 'domain', searchOptions: [] };
    this.searchFields['styleId'] =                 {apiOptions: 'dictionaries/styles', name: 'Style', queryString: 'style', searchOptions: [] }; // nie ma
    this.searchFields['statusId'] =                {apiOptions: 'dictionaries/statuses', name: 'Status', queryString: 'status', searchOptions: [] };
    this.searchFields['lexicalCharacteristicId'] = {apiOptions: 'dictionaries/lexical-characteristics', name: 'Lexical characteristic', queryString: 'lexicalCharacteristicId', searchOptions: [] }; // nie ma
    this.searchFields['ageId'] =                   {apiOptions: 'dictionaries/ages', name: 'Age', queryString: 'ageId', searchOptions: [] }; // nie ma
    this.searchFields['sourceId'] =                {apiOptions: 'dictionaries/sources', name: 'Source', queryString: 'sourceId', searchOptions: [] }; // nie ma
    this.searchFields['yiddishDomainId'] =         {apiOptions: 'dictionaries/yiddish-domains', name: 'Semantic field', queryString: 'yiddishDomainId', searchOptions: [] }; // nie ma
    this.searchFields['domainModifierId'] =        {apiOptions: 'dictionaries/yiddish-domain-modifiers', name: 'Semantic field modifier', queryString: 'domainModifierId', searchOptions: [] }; // nie ma

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
