import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import { HttpService } from '../../../services/http.service';
import {SidebarService} from '../../../services/sidebar.service';
import {MatKeyboardService} from '@ngx-material-keyboard/core';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {
  @Output() onSearchSubmit = new EventEmitter<any>();
  constructor(private http: HttpService, private sidebar: SidebarService, private keyboardService: MatKeyboardService) { }

  searchFields: {[id: string]: Object} = {};
  searchByString: {} = {};
  searchFormFields: Array<Object> = [];
  searchFieldsDict = ['style', 'status', 'lexical-characteristic',
    'age', 'source', 'semantic-field', 'semantic-field/modifier'];

  searchFieldsGlob = ['lexicon', 'partofspeech', 'domain'];

  searchKeys = ['lexiconId', 'partOfSpeechId', 'domainId', 'styleId', 'statusId', 'lexicalCharacteristicId', 'ageId',
    'sourceId', 'yiddishDomainId', 'domainModifierId'];

  states= [{name: 'a', code: 'a'}];

  test = null;

  ngOnInit() {

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
    console.log(this.searchFields);

  }

  onSubmit(form) {
    console.log(form);
    return;
    this.sidebar.getAllOptions(form);
  }
}
