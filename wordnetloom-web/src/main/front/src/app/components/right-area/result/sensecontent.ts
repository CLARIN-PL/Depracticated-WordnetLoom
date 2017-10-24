export class SenseContent {
  lemma: string;
  senseId: number;
  yiddishVariantId: number;
  variant: number;
  currentYiddish: Object;

  fields: Array<Object> = [];
  areas: Array<Object> = [];

  constructor(json: Object, currentYiddishVariant=null) {
    this.senseId = json['Id'];
    this.variant = json['Sense number'];

    if (json['Yiddish'].length > 0) {
      if (currentYiddishVariant && json['Yiddish'][currentYiddishVariant]) {
        this.yiddishVariantId = currentYiddishVariant;
      } else {
        this.yiddishVariantId = 0;
      }
      this.currentYiddish = json['Yiddish'][this.yiddishVariantId];
      this.lemma = this.currentYiddish['Latin spelling']
        + ' | ' + this.currentYiddish['Yiddish spelling'] + ' | ' +  this.currentYiddish['YIVO spelling'];
      this.setYiddishFields();
    } else {
      this.lemma = json['Lemma'];
    }

    this.setBasicFields(json);
  }

  private setBasicFields(json): void {
    const fieldNames = ['Domain', 'Lexicon', 'Part of speech'];
    for (const name of fieldNames){
      this.fields.push({name: name, values: [json[name]]});
    }
  }

  private setYiddishFields(): void {
    const fieldNames = ['Yiddish variant', 'Dialectal', 'Grammatical gender', 'Meaning', 'Lexical Characteristic',
      'Style', 'Status', 'Age'];

    const fields = [];
    for (const name of fieldNames){
      fields.push({name: name, values: [this.currentYiddish[name]]});
    }

    // transcription'
    fields.push({name: 'Transcription', values: this.currentYiddish['Transcription'].map(function(it){return it.type + ': ' + it.value;})});

    // var srcs =.reduce(function(str, it){return str + ',' + it; });
    const srcs =  this.currentYiddish['Source'].map(function(it){
      return it.name;
    }).join(',');
    fields.push({name: 'Source', values: [srcs]});

    //  'Inflection'
    fields.push({name: 'Inflection',
      values: this.currentYiddish['Inflection'].map(function(it){
        return 'prefix: ' + it.prefix + ', value:' + it.text;
      })});

    // semantic fied
    fields.push({name: 'Semantic filed', values: this.currentYiddish['Semantic filed'].map(function(it){return it.domain + ' (' + it.modifier + ')' ;})});

    this.areas.push({name: 'Yiddish specific', fields: fields});
  }
}
