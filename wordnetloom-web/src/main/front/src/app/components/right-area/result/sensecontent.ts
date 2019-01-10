import {QueryNames} from './querynames';

export class SenseContent {
  lemma: string;
  senseId: string;
  variant: number;
  domain: number;
  definition: string;

  partOfSpeech;
  grammaticalGender;
  flag;

  fields: Array<Object> = [];
  areas: Array<Object> = [];
  yiddishVariant = 'Default';

  constructor(json: Object, currentYiddishVariant=null, dictionarySettings: {}) {
    this.senseId = json['id'];
    this.variant = json['variant'];
    this.definition = json['definition'];
    this.grammaticalGender = null;

    // todo -- check if fields exist first
    this.partOfSpeech = dictionarySettings['partOfSpeechId'].searchOptions.find(it => it.id === json['part_of_speech']).name;
    this.flag = dictionarySettings['lexicon'].searchOptions.find(it => it.id === json['lexicon']).name;
    this.domain =  dictionarySettings['domainId'].searchOptions.find(it => it.id === json['domain']).name;

    this.lemma = json['lemma'] + ' ' + this.variant + ' (' + this.domain + ')';
    this.setBasicFields(json);
  }

  private setBasicFields(json): void {
    const fieldNames = ['Domain', 'Lexicon', 'Part of speech'];
    for (const name of fieldNames) {
      this.fields.push({name: name, values: [json[name]]});
    }
  }

  private getSearchFieldQuery(name: string, id: number|string) {
    return QueryNames.getQueryString(name, id);
  }

}
