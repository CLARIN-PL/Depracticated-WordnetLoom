export class QueryNames {

/*
* missing fields: Inflection, Semantic field
*/

  static dictionary = {
    // 'todo': 'lexiconId',
    // 'todo': 'partOfSpeechId',
    // 'todo': 'domainId',
    'style': 'style',
    // 'Status': 'statusId',
    // 'todo': 'lexicalCharacteristicId',
    'sources': 'source',
    'age': 'age',
    'grammatical_gender': 'grammatical_gender',
    'status': 'status',
    'lexical_characteristic': 'lexical_characteristic',
    'etymological_root': 'etymological_root'
    // 'todo': 'yiddishDomainId',
    // 'todo': 'domainModifierId',
  };

  static getQueryString(name, id) {
    if (!QueryNames.dictionary[name]) {
      return '';
    }
    const ret = {};
    ret[QueryNames.dictionary[name]] = id;
    return ret;
  }
}
