import { Injectable } from '@angular/core';
import {Http} from '@angular/http';
import {Observable} from 'rxjs/Rx';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/finally';
import 'rxjs/add/operator/catch';
import {lemmas} from './avaliable_lemmas_temp';
// import {SlimLoadingBarService} from 'ng2-slim-loading-bar';

@Injectable()
export class HttpService {
  // apiBase = '/yiddish/api/';
  // apiBase = 'http://156.17.135.29:8080/yiddish/api/';
  apiBase = 'http://156.17.135.55:8080/wordnetloom-server/resources/';

  constructor(private http: Http) {}


  private get(uri, base?: string): Observable<any> {
    // const headers = new Headers(options);
    // const opt = new RequestOptions({ headers: headers});

    if (!base) {
      base = this.apiBase;
    }

    return this.http.get(base + uri)
      .map( res => res.json())
      .finally(() => {});
  }

  getAbsolute(uri) {
    return this.get('', uri);
  }

  getSenseDetails(id) {
    return this.get('senses/' + id);
  }

  getSearchOptions(form: String, page = 0, perPage= 50) {
    let searchStr = 'senses/search?lexicon=4&';
    for (const key in form) {
      if (form[key] !== '' && form[key] !== undefined) {
        searchStr += key + '=' + form[key] + '&';
      }
    }
    return this.get(searchStr + 'limit=' + perPage + '&start=' + page * perPage );
    // return this.get(searchStr + 'per_page=' + perPage + '&page=' + page);
  }

  getGlobalOptions(searchedKey) {
    return this.get(searchedKey);
  }

  getSenseRelations(senseId) {
    return this.get('senses/' + senseId + '/relations');
  }

  getSearchAutocomplete(term) {
    const getLemmas = (searchedTerm, maxItemsToFind) => {
      const found = [];
      let somethingFound = false; // using this to optimize search function since lemmas are sorted,

      // true if searched term is yiddish
      const yiddishTerm = (searchedTerm.search(/[\u0590-\u05FF]/) >= 0);

      if (yiddishTerm) {
        for (let i = 0; i < lemmas['yiddish'].length; i++) {
        // for (const lemma of lemmas['yiddish']) {
          if (lemmas['yiddish'][i].startsWith(searchedTerm.toLowerCase())) {
            found.push({'lemma': lemmas['yiddish'][i], 'spelling': 'Yiddish'});
            somethingFound = true;
            if (found.length >= maxItemsToFind) { // check if list ready
              return found;
            }
          }
        }
      } else { // search if not yiddishTerm

        for (let i = 0; i < lemmas['latinAndYivo'].length; i++) {
          if (lemmas['latinAndYivo'][i][0].startsWith(searchedTerm.toLowerCase())) {
            found.push({
              'lemma': lemmas['latinAndYivo'][i][0],
              'spelling': (lemmas['latinAndYivo'][i][1] === 'y' ? 'Yivo' : 'Philological')
            });
            somethingFound = true;
            if (found.length >= maxItemsToFind) { // check if list ready
              return found;
            }
          } else if (somethingFound) {
            return found;
          }
        }
      }
      return found;
    };
    return Observable.of(getLemmas(term, 10)).debounceTime(750);
  }
  getLang(lang) {
    const langPath = `assets/i18n/${lang || 'en'}.json`;

    return this.http.get(langPath).map( res => res.json())
      .finally(() => {});
    // return this.get(langPath, '/');
  }

  getYiddishDetails(senseId) {
    return this.get('senses/' + senseId + '/yiddish');
  }

  getSenseGraph(senseId) {
    return this.get('senses/' + senseId + '/graph');
  }
}

