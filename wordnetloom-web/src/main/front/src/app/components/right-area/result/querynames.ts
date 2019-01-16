export class QueryNames {
  static dictionary = {
    'style': 'style',
    'sources': 'source',
    'age': 'age',
    'grammatical_gender': 'grammatical_gender',
    'status': 'yiddish_status',
    'lexical_characteristic': 'lexical_characteristic',
    'etymological_root': 'etymological_root',
    'semantic_fields': 'yiddish_domain',
    'particle_prefix': 'particle_prefix',
    'particle_root': 'particle_root',
    'particle_suffix': 'particle_suffix',
    'particle_constituent': 'particle_constituent',
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
