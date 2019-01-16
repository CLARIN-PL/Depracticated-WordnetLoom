import {QueryNames} from './querynames';
import {SenseContent} from './sensecontent';
import {HttpService} from '../../../services/http.service';

export class YiddishContent {

  id: number;
  yiddishId: number;


  lemma: string;
  differentAlphabetLemmas: {};
  senseId: string;
  yiddishVariantId: number;
  variant: number;

  partOfSpeech;
  grammaticalGender;
  flag;

  areas: Array<Object> = [];
  variant_type: String;
  yiddishVariant = 'Default';
  additionalFields = {};

  // http not initialized automatically, needs to be passed
  constructor(json: Object, parentSense: SenseContent, dictionarySettings: {}, private http: HttpService) {
    this.id = json['id'];
    this.yiddishId = json['id'];
    this.variant_type = json['variant_type'];
    this.differentAlphabetLemmas = {
      'latin': json['latin_spelling'],
      'yivo': json['yivo_spelling'],
      'yiddish': json['yiddish_spelling'],
    };
    this.yiddishVariant = json['variant_type'].replace('Yiddish_', '').replace(/_/g, ' ');

    this.variant = parentSense.variant;
    this.partOfSpeech = parentSense.partOfSpeech;
    this.flag = parentSense.flag;

    this.yiddishVariantId = 0;
    const domainName = parentSense.domain;

    this.lemma = json['latin_spelling'] +  ' ' + this.variant + ' (' + domainName + ')'
      + ' | ' + json['yiddish_spelling'] + ' | ' +  json['yivo_spelling'];

    this.grammaticalGender = '';
    if (json['grammatical_gender']) {
      this.grammaticalGender = json['grammatical_gender'].name;
    }

    this.setYiddishFields(json);

    this.additionalFields = {
      'context': json['context'],
      'comment': json['comment']
    };
  }

  private static sortParticles(it0, it1) {
    const values = {
      'prefix': 0,
      'root': 5,
      'constituent': 7,
      'suffix': 10
    };
    return values[it0.type] - values[it1.type];
  }

  static capitalizeFirstLetter(string) {
    return string.charAt(0).toUpperCase() + string.slice(1);
  }

  private assignTooltipForParticle(element) {
    const self = this;

    if (element.type === 'root' || element.type === 'constituent') {
      return (resolve, reject) => {
        resolve(YiddishContent.capitalizeFirstLetter(element.type));
      };
    }

    return (resolve, reject) => {
      self.http
        .getDictionaryItem(`${element.type}es`, element.id)
        .toPromise()
        .then(data => {
          resolve(YiddishContent.capitalizeFirstLetter(element.type) + ' | ' + data.description);
        });
    };
  }

  private getSearchFieldQuery(name: string, id: number|string) {
    return QueryNames.getQueryString(name, id);
  }

  private setYiddishFields(jsonData): void {
    const self = this;

    const fieldNames = {
      'latin_spelling': {viewName: 'Philological spelling', type: 'simple'},
      'yiddish_spelling': {viewName: 'Yiddish spelling', type: 'simple'},
      'yivo_spelling': {viewName: 'YIVO spelling', type: 'simple'},
      'part_of_speech': {viewName: 'Part of speech', type: 'inherited'},
      'grammatical_gender': {viewName: 'Grammatical qualifiers', type: 'object'},
      'inflections': {viewName: 'Inflection', type: 'array'},
      'meaning': {viewName: 'Meaning', type: 'simple'},
      'semantic_fields': {viewName: 'Semantic field', type: 'array'},
      'style': {viewName: 'Style', type: 'object'},
      'lexical_characteristic': {viewName: 'Lexical Characteristic', type: 'object'},
      'status': {viewName: 'Rootedness', type: 'object'},
      'etymology': {viewName: 'Etymology', type: 'simple'},
      'age': {viewName: 'Age', type: 'object'},
      'sources': {viewName: 'Sources', type: 'array'},
      'etymological_root': {viewName: 'Etymological Root', type: 'simple'},
      'particles': {viewName: 'Morphology', type: 'array', separator: ' | '},
      'transcriptions': {viewName: 'Phonetic transcription', type: 'array'}
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
              searchQuery: this.getSearchFieldQuery(key, jsonData[key])
            }]};
      } else if (fieldNames[key].type === 'inherited') {
        newField = {
          name: fieldNames[key].viewName,
          values: [
            {
              name: this.partOfSpeech,
              searchQuery: this.getSearchFieldQuery(name, jsonData[name])
            }]};
      } else if (fieldNames[key].type === 'object') {

          let values = [];
          if (jsonData[key]) {
            values = [{
              name: jsonData[key].name,
              searchQuery:  self.getSearchFieldQuery(key, jsonData[key].id)
            }];
          }

          newField = {
            name: fieldNames[key].viewName,
            values: values
          };

        } else if (fieldNames[key].type === 'array') {
          if (key === 'inflections' && jsonData['inflections'].length > 0) {
            newField = {
              name: fieldNames[key].viewName,
              values: jsonData['inflections'].map(function (it) {
                return {name: it.name + ' ' + it.text,
                  searchQuery: self.getSearchFieldQuery('Inflection', it.id)
                };
              })
            };
          } else if (key === 'transcriptions' && jsonData['transcriptions'].length > 0) {
            newField = {
              name: fieldNames[key].viewName,
              values: jsonData['transcriptions'].map(function (it) {
                return {
                  name: it.phonography,
                  searchQuery: self.getSearchFieldQuery('transcriptions', it.id)
                };
              })
            };
          } else if (key === 'semantic_fields' && jsonData['semantic_fields'].length > 0) {
            newField = {
              name: fieldNames[key].viewName, values: jsonData[key].map(function (it) {
                return {
                  name: it['domain'].name,
                  searchQuery: self.getSearchFieldQuery(key, it['domain'].id)
                };
              })
            };
          } else if (key === 'particles' && jsonData['particles'].length > 0) {
            newField = {
              name: fieldNames[key].viewName, values: jsonData[key]
                .sort(YiddishContent.sortParticles)
                .map(function (it) {
                  return {
                    name: it.value,
                    tooltip: new Promise<string>(self.assignTooltipForParticle(it)),
                    searchQuery: self.getSearchFieldQuery('particle_' + it.type, it.id ? it.id : it.value)
                  };
              })
            };
          } else {
            newField = {
              name: fieldNames[key].viewName, values: jsonData[key].map(function (it) {
                return {
                  name: it.name,
                  searchQuery: self.getSearchFieldQuery(key, it.id)
                };
              })
            };
          }
      }

      // don't show [Grammatical qualifiers, Inflection, Lexical Characteristic, Synonyms, Context, Comment] if it's empty
      if (newField.values.length === 0 &&
        ['Grammatical qualifiers', 'Inflection', 'Lexical Characteristic'].indexOf(newField.name) > -1) {
        continue;
      }

      newField.separator = fieldNames[key].separator;
      fields.push(newField);
    }

    this.areas.push({name: 'Yiddish specific', fields: fields});
  }
}
