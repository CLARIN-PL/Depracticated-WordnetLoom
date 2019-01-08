import {QueryNames} from './querynames';
import {UnitComponent} from "../../unit/unit.component";

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

  constructor(json: Object) {
    this.currentYiddish = json; // todo - get rid of that later

    console.log(json);
    this.yiddishId = json['id'];
    this.yiddishVariant = json['variant_type'].replace(/_/g, ' ');

    // this.lemma = json['latin_spelling'] + ' ' + this.variant + ' (' + json['domain'] + ')';
    this.variant = json['variant_type'];
    this.partOfSpeech = json['part_of_speech'];
    this.grammaticalGender = null;
    this.flag = json['lexicon'];

    this.yiddishVariantId = 0;

      this.lemma = json['latin_spelling'] +  ' ' + this.variant + ' (' + json['domain'] + ')'
        + ' | ' + json['yiddish_spelling'] + ' | ' +  json['yivo_spelling'];
      this.grammaticalGender = json['Grammatical gender'];

      this.setYiddishFields();

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

  private setYiddishFields(): void {
    const self = this;
    const fieldNames = {
      'latin_spelling': 'Latin spelling',
      'yiddish_spelling': 'Yiddish spelling',
      'yivo_spelling': 'YIVO spelling',

      // part of speech
      // gramatical qualifiers

      'grammatical_gender': 'Grammatical gender',
      'dialectal': 'Dialectal', // todo -- missing

      'meaning': 'Meaning',
      'lexical_characteristic': 'Lexical Characteristic', // todo --missing
      'style': 'Style', // todo -- missing
      'status': 'Rootedness',

      'etymology': 'Etymology',

      'age': 'Age'

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
    for (const key in fieldNames){
      console.log(this.currentYiddish);
      console.log(name);

      const newField = {
        name: fieldNames[key],
        values: [
          {
            name: this.currentYiddish[key],
            searchQuery: this.getSearchFieldQuery(name, this.currentYiddish[name])
          }]};
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
    if (this.currentYiddish['transcriptions'].length > 0) {
      fields.push({
        name: 'transcriptions', values: this.currentYiddish['transcriptions'].map(function (it) {
          return {
            name: it.name + ': ' + it.phonography,
            searchQuery: self.getSearchFieldQuery('transcriptions', it.id)
          };
        })
      });
    }
    // source
    if (this.currentYiddish['sources'].length > 0) {
      const srcs = this.currentYiddish['sources'].map(function (it) {
        return {
          name: it.name,
          searchQuery:  self.getSearchFieldQuery('sources', it.id)
        };
      });
      fields.push({name: 'sources', values: srcs});
    }

    //  Inflection
    if (this.currentYiddish['inflections'].length > 0) {
      // field name contains prefix
      fields.push({
        name: 'inflections',
        values: this.currentYiddish['inflections'].map(function (it) {
          return {name: 'prefix: ' + it.name + ', value:' + it.text,
            searchQuery: self.getSearchFieldQuery('Inflection', it.id)
          };
        })
      });
    }
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
  }
}
