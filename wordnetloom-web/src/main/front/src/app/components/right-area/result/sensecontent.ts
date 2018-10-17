import {QueryNames} from './querynames';
import {UnitComponent} from "../../unit/unit.component";

export class SenseContent {
  lemma: string;
  senseId: number;
  yiddishVariantId: number;
  variant: number;
  currentYiddish: Object;

  partOfSpeech;
  grammaticalGender;
  flag;

  fields: Array<Object> = [];
  areas: Array<Object> = [];
  yiddishVariant = 'Default';

  constructor(json: Object, currentYiddishVariant=null) {
    this.senseId = json['Id'];
    this.variant = json['Sense number'];
    this.partOfSpeech = json['Part of speech'];
    this.grammaticalGender = null;
    this.flag = json['Lexicon'];

    this.lemma = json['Lemma'] + ' ' + this.variant + ' (' + json['Domain'] + ')';
    if (json['Yiddish'].length > 0) {
      if (currentYiddishVariant && json['Yiddish'][currentYiddishVariant]) {
        this.yiddishVariantId = currentYiddishVariant;
      } else {
        this.yiddishVariantId = 0;
      }
      this.currentYiddish = json['Yiddish'][this.yiddishVariantId];
      this.lemma = this.currentYiddish['Latin spelling'] +  ' ' + this.variant + ' (' + json['Domain'] + ')'
        + ' | ' + this.currentYiddish['Yiddish spelling'] + ' | ' +  this.currentYiddish['YIVO spelling'];
      this.grammaticalGender = this.currentYiddish['Grammatical gender'];
      this.yiddishVariant = this.currentYiddish['Yiddish variant'].replace(/_/g, ' ');
      this.setYiddishFields();
    }

    this.setBasicFields(json);
  }

  private setBasicFields(json): void {
    const fieldNames = ['Domain', 'Lexicon', 'Part of speech'];
    for (const name of fieldNames){
      this.fields.push({name: name, values: [json[name]]});
    }
  }

  private getSearchFieldQuery(name: string, id: number|string) {
    console.log(name, id);
    return QueryNames.getQueryString(name, id);
  }

  private setYiddishFields(): void {
    const self = this;
    const fieldNames = ['Latin spelling', 'Yiddish spelling', 'Dialectal', 'Grammatical gender', 'Meaning', 'Lexical Characteristic',
      'Style', 'Status', 'Age'];

    const fields = [];
    console.log(this.currentYiddish);
    for (const name of fieldNames){
      if (this.currentYiddish[name].length > 0) {
        const newField = {
          name: name,
          values: [
            {
              name: this.currentYiddish[name],
              searchQuery: this.getSearchFieldQuery(name, this.currentYiddish[name])
            }]};
        fields.push(newField);
      }
    }

    // transcription
    if (this.currentYiddish['Transcription'].length > 0) {
      fields.push({
        name: 'Transcription', values: this.currentYiddish['Transcription'].map(function (it) {
          return {
            name: it.type + ': ' + it.value,
            searchQuery: self.getSearchFieldQuery('Transcription', it.id)
          };
        })
      });
    }
    // source
    if (this.currentYiddish['Source'].length > 0) {
      const srcs = this.currentYiddish['Source'].map(function (it) {
        return {
          name: it.name,
          searchQuery:  self.getSearchFieldQuery('Source', it.id)
        };
      });
      fields.push({name: 'Source', values: srcs});
    }

    //  Inflection
    if (this.currentYiddish['Inflection'].length > 0) {
      fields.push({
        name: 'Inflection',
        values: this.currentYiddish['Inflection'].map(function (it) {
          return {name: 'prefix: ' + it.prefix + ', value:' + it.text,
            searchQuery: self.getSearchFieldQuery('Inflection', it.id)
          };
        })
      });
    }
    // semantic fied
    if (this.currentYiddish['Semantic filed'].length > 0) {
      fields.push({
        name: 'Semantic filed',
        values: this.currentYiddish['Semantic filed'].map(function (it) {
          return {name: it.domain + ' (' + it.modifier + ')',
            searchQuery: self.getSearchFieldQuery('Semantic filed', it.id)
          };
        })
      });
    }
    this.areas.push({name: 'Yiddish specific', fields: fields});
  }
}
