export class QueryNames {

/*
* missing fields: Inflection, Semantic field
*/

  static dictionary = {
    // 'todo': 'lexiconId',
    // 'todo': 'partOfSpeechId',
    // 'todo': 'domainId',
    'Style': 'styleId',
    // 'Status': 'statusId',
    // 'todo': 'lexicalCharacteristicId',
    'Source': 'sourceId',
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
