import {QueryNames} from './querynames';
import {SenseContent} from './sensecontent';
import {HttpService} from '../../../services/http.service';

export class YiddishContent {

  // http not initialized automatically, needs to be passed
  constructor(json: Object, parentSense: SenseContent, dictionarySettings: {}, private http: HttpService) {
    this.currentYiddish = json; // todo - get rid of that later

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
    this.part_of_speech = parentSense.partOfSpeech;
    this.grammaticalGender = null;
    this.flag = parentSense.flag;

    this.yiddishVariantId = 0;
    const domainName = parentSense.domain;

    this.lemma = json['latin_spelling'] +  ' ' + this.variant + ' (' + domainName + ')'
      + ' | ' + json['yiddish_spelling'] + ' | ' +  json['yivo_spelling'];

    this.grammaticalGender = '';
    if (json['grammatical_gender']) {
      this.grammaticalGender = json['grammatical_gender'].name;
    }

    this.setYiddishFields();

    this.setBasicFields(json);

    this.additionalFields = {
      'context': json['context'],
      'comment': json['comment']
    };

    console.log(this);
  }
  id: number;
  yiddishId: number;


  lemma: string;
  differentAlphabetLemmas: {};
  senseId: string;
  yiddishVariantId: number;
  variant: number;

  partOfSpeech;
  part_of_speech;
  grammaticalGender;
  flag;

  fields: Array<Object> = [];
  areas: Array<Object> = [];
  variant_type: String;
  yiddishVariant = 'Default';
  currentYiddish;
  additionalFields = {};

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
        .getAbsolute(
          `http://156.17.135.55:8080/wordnetloom-server/resources/dictionaries/${element.type}es/${element.id}`
        )
        .toPromise()
        .then(data => {
          resolve(YiddishContent.capitalizeFirstLetter(element.type) + ' | ' + data.description);
        });
    };
  }

  private getPrefixDescriptionPromise() {

    return this.http
      .getAbsolute('http://156.17.135.55:8080/wordnetloom-server/resources/dictionaries/prefixes/185')
      .toPromise()
      .then( data => {
          console.log(data);
        }
      );
  }

  private setBasicFields(json): void {
    const fieldNames = ['Domain', 'lexicon', 'Part of speech'];
    for (const name of fieldNames) {
      this.fields.push({name: name, values: [json[name]]});
    }
  }

  private getSearchFieldQuery(name: string, id: number|string) {
    return QueryNames.getQueryString(name, id);
  }

  private setYiddishFields(): void {
    const self = this;

    // todo- check later, rather than do this
    // self.currentYiddish['age'] = [self.currentYiddish['age']]; // fix for later use
    // self.currentYiddish['style'] = [self.currentYiddish['style']]; // fix for later use
    // self.currentYiddish['lexical_characteristic'] = [self.currentYiddish['lexical_characteristic']]; // fix for later use

    const fieldNames = {
      'latin_spelling': {viewName: 'Philological spelling', type: 'simple'},
      'yiddish_spelling': {viewName: 'Yiddish spelling', type: 'simple'},
      'yivo_spelling': {viewName: 'YIVO spelling', type: 'simple'},
      'part_of_speech': {viewName: 'Part of speech', type: 'inherited'},
      'grammatical_gender': {viewName: 'Grammatical qualifiers', type: 'object'},
      'inflections': {viewName: 'Inflection', type: 'array'},
      'meaning': {viewName: 'Meaning', type: 'simple'},
      'semantic_fields': {viewName: 'Semantic field', type: 'array'},        // todo -search
      'style': {viewName: 'Style', type: 'object'},                           // todo -- missing, ma być object
      'lexical_characteristic': {viewName: 'Lexical Characteristic', type: 'object'},
      'status': {viewName: 'Rootedness', type: 'object'},
      'etymology': {viewName: 'Etymology', type: 'simple'},
      'age': {viewName: 'Age', type: 'object'},
      'sources': {viewName: 'Sources', type: 'array'},
      'etymological_root': {viewName: 'Etymological Root', type: 'simple'},
      'particles': {viewName: 'Morphology', type: 'array', separator: ' | '},
      'transcriptions': {viewName: 'Phonetic transcription', type: 'array'}
      // Philological Spelling,
      // Yiddish Spelling,
      // YIVO Spelling,
      // Part of Speech,
      // Grammatical Qualifiers (​ zawartość pola Grammatical Gender​),
      // Inflection, ​
      // Meaning,
      // Semantic Field, SEARCH!!
      // Style, SEARCH!!
      // Lexical Characteristic, SEARCH!!
      // Rootedness (​ zawartość pola Status​ ), SEARCH!!
      // Etymology,
      // Age, SEARCH!!
      // Source,
      // Etymological Root, SEARCH!!
      // Morphology (​zawartość pola Particles​ ) ​ . SEARCH!!
      //
      // Wszystkie pola powinny być wyświetlane zawsze, niezależnie od tego,
      // czy coś jest w nich wybrane lub wpisane w Wordnetloomie,
      // z wyjątkiem następujących: ​ Lexical Characteristic, Context, Comment​
    };

    const fields = [];
    for (const key in fieldNames) {
      let newField;

      if (fieldNames[key].type === 'simple') {

        if (!this.currentYiddish[key]) {
          console.log(key, 'missing');
          continue;
        }

        newField = {
          name: fieldNames[key].viewName,
          values: [
            {
              name: this.currentYiddish[key],
              searchQuery: this.getSearchFieldQuery(key, this.currentYiddish[key])
            }]};
      } else if (fieldNames[key].type === 'inherited') {
        newField = {
          name: fieldNames[key].viewName,
          values: [
            {
              name: this.partOfSpeech,
              searchQuery: this.getSearchFieldQuery(name, this.currentYiddish[name])
            }]};
      } else if (fieldNames[key].type === 'object') {

          let values = [];
          if (this.currentYiddish[key]) {
            values = [{
              name: this.currentYiddish[key].name,
              searchQuery:  self.getSearchFieldQuery(key, this.currentYiddish[key].id)
            }];
          }

          newField = {
            name: fieldNames[key].viewName,
            values: values
          };

        } else if (fieldNames[key].type === 'array') {
          if (key === 'inflections' && this.currentYiddish['inflections'].length > 0) {
            newField = {
              name: fieldNames[key].viewName,
              values: this.currentYiddish['inflections'].map(function (it) {
                return {name: it.name + ' ' + it.text,
                  searchQuery: self.getSearchFieldQuery('Inflection', it.id)
                };
              })
            };
          } else if (key === 'transcriptions' && this.currentYiddish['transcriptions'].length > 0) {
            newField = {
              name: fieldNames[key].viewName,
              values: this.currentYiddish['transcriptions'].map(function (it) {
                return {
                  name: it.phonography,
                  searchQuery: self.getSearchFieldQuery('transcriptions', it.id)
                };
              })
            };
          } else if (key === 'semantic_fields' && this.currentYiddish['semantic_fields'].length > 0) {
            newField = {
              name: fieldNames[key].viewName, values: this.currentYiddish[key].map(function (it) {
                return {
                  name: it['domain'].name,
                  searchQuery: self.getSearchFieldQuery(key, it['domain'].id)
                };
              })
            };
          } else if (key === 'particles' && this.currentYiddish['particles'].length > 0) {
            newField = {
              name: fieldNames[key].viewName, values: this.currentYiddish[key]
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
              name: fieldNames[key].viewName, values: this.currentYiddish[key].map(function (it) {
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
