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
        {id: 1, name: 'YIVO Spelling', description: ''},
        {id: 2, name: 'Yiddish', description: ''},
        {id: 3, name: 'Philological', description: ''},
        {id: 4, name: 'Transcription', description: ''},
        {id: 5, name: 'Phonetic', description: ''},
        {id: 6, name: 'Transcription', description: ''},
        {id: 7, name: 'Etymological Root', description: ''},
        {id: 8, name: 'Prefix', description: ''},
        {id: 9, name: 'Root', description: ''},
        {id: 10, name: 'Suffix', description: ''},
        {id: 11, name: 'Constituent', description: ''}
      ]};
    this.searchFields['lexiconId'] =               {apiOptions: 'lexicon', name: 'Lexicon', queryString: 'lexiconId', searchOptions: [] };
    this.searchFields['partOfSpeechId'] =          {apiOptions: 'partofspeech', name: 'Part of Speech', queryString: 'partOfSpeechId', searchOptions: [] };
    this.searchFields['domainId'] =                {apiOptions: 'domain', name: 'Domain', queryString: 'domainId', searchOptions: [] };
    this.searchFields['styleId'] =                 {apiOptions: 'dictionary/style', name: 'Style', queryString: 'styleId', searchOptions: [] };
    this.searchFields['statusId'] =                {apiOptions: 'dictionary/status', name: 'Status', queryString: 'status', searchOptions: [] };
    this.searchFields['lexicalCharacteristicId'] = {apiOptions: 'dictionary/lexical-characteristic', name: 'Lexical characteristic', queryString: 'lexicalCharacteristicId', searchOptions: [] };
    this.searchFields['ageId'] =                   {apiOptions: 'dictionary/age', name: 'Age', queryString: 'ageId', searchOptions: [] };
    this.searchFields['sourceId'] =                {apiOptions: 'dictionary/source', name: 'Source', queryString: 'sourceId', searchOptions: [] };
    this.searchFields['yiddishDomainId'] =         {apiOptions: 'dictionary/semantic-field', name: 'Semantic field', queryString: 'yiddishDomainId', searchOptions: [] };
    this.searchFields['domainModifierId'] =        {apiOptions: 'dictionary/semantic-field/modifier', name: 'Domain modifier', queryString: 'domainModifierId', searchOptions: [] };

    this.assignSearchFields();
  }

  private assignSearchFields() {
    for (const key in this.searchFields) {
      const field =  this.searchFields[key];
      this.http.getGlobalOptions(field['apiOptions']).subscribe(
        (response) => {
          this.searchFields[key]['searchOptions'] = response.entries;
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
