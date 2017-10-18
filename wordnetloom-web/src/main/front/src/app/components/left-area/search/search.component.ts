import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import { HttpService } from '../../../services/http.service';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {
  @Output() onSearchSubmit = new EventEmitter<any>();
  constructor(private http: HttpService) { }

  searchFields: {[id: string]: Object} = {};

  searchFormFields: Array<Object> = [];
  searchFieldsDict = ['style', 'status', 'lexical-characteristic',
    'age', 'source', 'semantic-field', 'semantic-field/modifier'];

  searchFieldsGlob = ['lexicon', 'partofspeech', 'domain'];

  searchKeys = ['lexiconId', 'partOfSpeechId', 'domainId', 'styleId', 'statusId', 'lexicalCharacteristicId', 'ageId', 'sourceId', 'yiddishDomainId', 'domainModifierId'];

  ngOnInit() {
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
        });
    }

  }

  onSubmit(form) {
    this.http.getSearchOptions(form).subscribe(
      (response) => {
        this.onSearchSubmit.emit(response);
      }
    );
  }
}
