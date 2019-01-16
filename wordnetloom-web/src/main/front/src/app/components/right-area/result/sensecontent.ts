import {QueryNames} from './querynames';

export class SenseContent {
  lemma: string;
  senseId: string;
  variant: number;
  domain: number;
  definition: string;

  partOfSpeech;
  flag;

  areas: Array<Object> = [];

  constructor(json: Object, currentYiddishVariant=null, dictionarySettings: {}) {
    this.senseId = json['id'];
    this.variant = json['variant'];
    this.definition = json['definition'];

    this.partOfSpeech = dictionarySettings['partOfSpeech'].searchOptions.find(it => it.id === json['part_of_speech']).name;
    this.flag = dictionarySettings['lexicon'].searchOptions.find(it => it.id === json['lexicon']).name;
    this.domain =  dictionarySettings['domain'].searchOptions.find(it => it.id === json['domain']).name;

    this.lemma = json['lemma'] + ' ' + this.variant + ' (' + this.domain + ')';

    this.setContent(json, dictionarySettings);
    console.log(this);
  }

  private setContent(jsonData, dictionarySettings) {
    const self = this;

    const fieldNames = {
      'lemma': {viewName: 'Latin spelling', type: 'simple'},
      'lexicon': {viewName: 'Lexicon', type: 'simple_dict',  dictName: 'lexicon'},
      'part_of_speech': {viewName: 'Part of speech', type: 'simple_dict', dictName: 'partOfSpeech'},
      'domain': {viewName: 'Domain', type: 'simple_dict',  dictName: 'domain'},
      'definition': {viewName: 'Definition', type: 'simple'},
      'register': {viewName: 'Register', type: 'simple_dict',  dictName: 'register'},
    };
    const fields = [];

    for (const key in fieldNames) {
      let newField;

      if (fieldNames[key].type === 'simple') {

        if (!jsonData[key]) {
          console.log(key, 'missing');
          continue;
        }

        newField = {
          name: fieldNames[key].viewName,
          values: [
            {
              name: jsonData[key],
              searchQuery: null
            }]
        };
      } else if (fieldNames[key].type === 'simple_dict' && jsonData[key] ) {

        newField = {
          name: fieldNames[key].viewName,
          values: [
            {
              name: dictionarySettings[fieldNames[key].dictName].searchOptions.find(it => it.id === jsonData[key]).name,
              searchQuery: null
            }]
        };
      }
      if (newField) {
        fields.push(newField);
      }
    }
    this.areas.push({name: 'Sense content', fields: fields});
  }
}
