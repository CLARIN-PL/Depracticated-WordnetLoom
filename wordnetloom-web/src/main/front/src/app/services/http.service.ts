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

  private handleError() {
    // todo
  }

  private get(uri, base?: string): Observable<any> {
    // this.slimLoadingBarService.start();

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
    const incoming = this.get('sense/' + senseId + '/relations/incoming').map(res => res);
    const outgoing = this.get('sense/' + senseId + '/relations/outgoing').map(res => res);

    return Observable.forkJoin([incoming, outgoing]);
  }

  getSearchAutocomplete(term) {
    // todo- get from search
    // const avaliable_lemmas = lemmas.filter((it) => it.startsWith(term));
    const getLemmas = (searchedTerm, maxItemsToFind) => {
      const found = [];
      let somethingFound = false; // using this to optimize search function since lemmas are sorted
      for (let i = 0; i < lemmas.length; i++) {
        if (lemmas[i].startsWith(searchedTerm.toLowerCase())) {
          found.push(lemmas[i]);
          somethingFound = true;
          if (found.length >= maxItemsToFind) { // check if list ready
            return found;
          }
        } else if (somethingFound) {
          // fired when something was found recently but not in current iter
          return found;
        }
      }
      return found;
    };
    return Observable.of(getLemmas(term, 10)).debounceTime(750);
    return Observable.of(lemmas.filter((it) => it.startsWith(term))).debounceTime(750);
    // return Observable.of(new Object()).mapTo(avaliable_lemmas);
    // return Observable.of(new Object()).mapTo([term, 'abc', 'cde']);
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

