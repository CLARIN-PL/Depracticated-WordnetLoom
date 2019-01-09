import {QueryNames} from './querynames';
import {UnitComponent} from "../../unit/unit.component";

export class SenseContent {
  lemma: string;
  senseId: string;
  variant: number;
  domain: number;

  partOfSpeech;
  grammaticalGender;
  flag;

  fields: Array<Object> = [];
  areas: Array<Object> = [];
  yiddishVariant = 'Default';

  constructor(json: Object, currentYiddishVariant=null, dictionarySettings: {}) {
    console.log(json);
    this.senseId = json['id'];
    this.variant = json['variant'];
    this.partOfSpeech = dictionarySettings['partOfSpeechId'].searchOptions.find(it => it.id === json['part_of_speech']).name;
    this.grammaticalGender = null;
    this.flag = dictionarySettings['lexicon'].searchOptions.find(it => it.id === json['lexicon']).name;
    // this.domain = json['domain'];
    this.domain =  dictionarySettings['domainId'].searchOptions.find(it => it.id === json['domain']).name;

    this.lemma = json['lemma'] + ' ' + this.variant + ' (' + this.domain + ')';
    // todo - change
    // if (json['_links']['yiddish']) {
    //   if (currentYiddishVariant && json['Yiddish'][currentYiddishVariant]) {
    //     this.yiddishVariantId = currentYiddishVariant;
    //   } else {
    //     this.yiddishVariantId = 0;
    //   }
    //   this.currentYiddish = json['Yiddish'][this.yiddishVariantId];
    //   this.lemma = this.currentYiddish['Latin spelling'] +  ' ' + this.variant + ' (' + json['Domain'] + ')'
    //     + ' | ' + this.currentYiddish['Yiddish spelling'] + ' | ' +  this.currentYiddish['YIVO spelling'];
    //   this.grammaticalGender = this.currentYiddish['Grammatical gender'];
    //   this.yiddishVariant = this.currentYiddish['Yiddish variant'].replace(/_/g, ' ');
    //   this.setYiddishFields();
    // }

    this.setBasicFields(json);
  }

  private setBasicFields(json): void {
    const fieldNames = ['Domain', 'Lexicon', 'Part of speech'];
    for (const name of fieldNames){
      this.fields.push({name: name, values: [json[name]]});
    }
  }

  private getSearchFieldQuery(name: string, id: number|string) {
    return QueryNames.getQueryString(name, id);
  }

}
