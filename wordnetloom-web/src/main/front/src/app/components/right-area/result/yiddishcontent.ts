import {QueryNames} from './querynames';
import {UnitComponent} from '../../unit/unit.component';
import {SenseContent} from "./sensecontent";

export class YiddishContent {
  yiddishId: number;


  lemma: string;
  senseId: string;
  yiddishVariantId: number;
  variant: number;

  partOfSpeech;
  grammaticalGender;
  flag;

  fields: Array<Object> = [];
  areas: Array<Object> = [];
  yiddishVariant = 'Default';
  currentYiddish;
  additionalFields = {};

  constructor(json: Object, parentSense: SenseContent) {
    this.currentYiddish = json; // todo - get rid of that later

    // console.log(json);
    this.yiddishId = json['id'];
    this.yiddishVariant = json['variant_type'].replace(/_/g, ' ');

    // this.lemma = json['latin_spelling'] + ' ' + this.variant + ' (' + json['domain'] + ')';
    // this.variant = json['variant_type'];
    this.variant = parentSense.variant;
    this.partOfSpeech = parentSense.partOfSpeech;
    this.grammaticalGender = null;
    this.flag = json['lexicon'];

    this.yiddishVariantId = 0;

    this.lemma = json['latin_spelling'] +  ' ' + this.variant + ' (' + json['domain'] + ')'
      + ' | ' + json['yiddish_spelling'] + ' | ' +  json['yivo_spelling'];
    this.grammaticalGender = json['Grammatical gender'];

    this.setYiddishFields();

    this.setBasicFields(json);

    this.additionalFields = {
      'context': json['context'],
      'comment': json['comment']
    };
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
      'latin_spelling': {viewName: 'Latin spelling', type: 'simple'},
      'yiddish_spelling': {viewName: 'Yiddish spelling', type: 'simple'},
      'yivo_spelling': {viewName: 'YIVO spelling', type: 'simple'},
      'part_of_speech': {viewName: 'Part of speech', type: 'inherited'},
      'grammatical_gender': {viewName: 'Grammatical qualifiers', type: 'object'},
      'inflections': {viewName: 'Inflections', type: 'array'},
      'semantic_fields': {viewName: 'Semantic fields', type: 'array'},        // todo -search
      'meaning': {viewName: 'Meaning', type: 'simple'},
      'style': {viewName: 'Style', type: 'simple'},                           // todo -- missing, ma być object
      'lexical_characteristic': {viewName: 'Lexical Characteristic', type: 'object'},
      'status': {viewName: 'Rootedness', type: 'object'},
      'etymology': {viewName: 'Etymology', type: 'simple'},
      'age': {viewName: 'Age', type: 'object'},
      'sources': {viewName: 'Sources', type: 'array'},
      'etymological_root': {viewName: 'Etymological Root', type: 'simple'}, // todo search need OBJECT
      'particles': {viewName: 'Morphology', type: 'array'},
      'transcriptions': {viewName: 'Transcriptions', type: 'array'}
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
              searchQuery: this.getSearchFieldQuery(name, this.currentYiddish[name])
            }]};
      } else if (fieldNames[key].type === 'inherited') {
        newField = {
          name: fieldNames[key].viewName,
          values: [
            {
              name: this[key],
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
                return {name: 'prefix: ' + it.name + ', value:' + it.text,
                  searchQuery: self.getSearchFieldQuery('Inflection', it.id)
                };
              })
            };
          } else if (key === 'transcriptions' && this.currentYiddish['transcriptions'].length > 0) {
            newField = {
              name: 'transcriptions', values: this.currentYiddish['transcriptions'].map(function (it) {
                return {
                  name: it.name + ': ' + it.phonography,
                  searchQuery: self.getSearchFieldQuery('transcriptions', it.id)
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

      fields.push(newField);

      // if (this.currentYiddish.length > 0) {
      //   const newField = {
      //     name: name,
      //     values: [
      //       {
      //         name: this.currentYiddish[name],
      //         searchQuery: this.getSearchFieldQuery(name, this.currentYiddish[name])
      //       }]};
      //   fields.push(newField);
      // }
    }

    // transcription
    // if (this.currentYiddish['transcriptions'].length > 0) {
    //   fields.push({
    //     name: 'transcriptions', values: this.currentYiddish['transcriptions'].map(function (it) {
    //       return {
    //         name: it.name + ': ' + it.phonography,
    //         searchQuery: self.getSearchFieldQuery('transcriptions', it.id)
    //       };
    //     })
    //   });
    // }
    // source
    // if (this.currentYiddish['sources'].length > 0) {
    //   const srcs = this.currentYiddish['sources'].map(function (it) {
    //     return {
    //       name: it.name,
    //       searchQuery:  self.getSearchFieldQuery('sources', it.id)
    //     };
    //   });
    //   fields.push({name: 'sources', values: srcs});
    // }
    //
    // //  Inflection
    // if (this.currentYiddish['inflections'].length > 0) {
    //   // field name contains prefix
    //   fields.push({
    //     name: 'inflections',
    //     values: this.currentYiddish['inflections'].map(function (it) {
    //       return {name: 'prefix: ' + it.name + ', value:' + it.text,
    //         searchQuery: self.getSearchFieldQuery('Inflection', it.id)
    //       };
    //     })
    //   });
    // }
    // semantic fied -- todo
    // if (this.currentYiddish['Semantic filed'].length > 0) {
    //   fields.push({
    //     name: 'Semantic filed',
    //     values: this.currentYiddish['Semantic filed'].map(function (it) {
    //       return {name: it.domain + ' (' + it.modifier + ')',
    //         searchQuery: self.getSearchFieldQuery('Semantic filed', it.id)
    //       };
    //     })
    //   });
    // }
    this.areas.push({name: 'Yiddish specific', fields: fields});
    console.log(this);
  }
}
