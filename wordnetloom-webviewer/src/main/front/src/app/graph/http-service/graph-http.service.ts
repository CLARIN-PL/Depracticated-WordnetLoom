import { Injectable } from '@angular/core';
import {Http} from '@angular/http';
import {Observable} from 'rxjs/Rx';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/finally';
import 'rxjs/add/operator/catch';
import {SlimLoadingBarService} from 'ng2-slim-loading-bar';

@Injectable()
export class GraphHttpService {
  apiBase = 'http://api.slowosiec.clarin-pl.eu/plwordnet-api/';

  constructor(private http: Http,  private slimLoadingBarService: SlimLoadingBarService) { }

  private handleError() {
    // todo
  }

  private httpGet(uriEnd, options = {}): Observable<any> {
    this.slimLoadingBarService.start();

    // const headers = new Headers(options);
    // const opt = new RequestOptions({ headers: headers});

    return this.http.get(this.apiBase + uriEnd)
      .map( res => res.json())
      .finally(() => this.slimLoadingBarService.complete() );
  }

  getSensesFromLemma(lemma) {
    return this.httpGet('synsets/search?lemma=' + lemma);
  }

  getSenseFromId(id) {
    return this.httpGet('senses/' + id);
  }

  getSynsetFromSenseId(id) {
    return this.httpGet('senses/' + id + '/synset');
  }

  getSynsetSenses(synsetId) {
    // http://api.slowosiec.clarin-pl.eu/plwordnet-api/synsets/6047/senses
    return this.httpGet('synsets/' + synsetId + '/senses');
  }

  getSenseOwningSynsetId(senseId) {
    return this.httpGet('senses/' + senseId + '/synset');
  }

  getSenseRelations(senseId) {
    return this.httpGet('senses/' + senseId + '/relations');
  }

  getSenseRelationsFrom(senseId) {
    return this.httpGet('senses/' + senseId + '/relations/from');
  }

  getSenseRelationsTo(senseId) {
    return this.httpGet('senses/' + senseId + '/relations/from');
  }

  getSynsetRelations(synsetId) {
    return this.httpGet('synsets/' + synsetId + '/relations');
  }

  getSynsetRelationsFrom(synsetId) {
    return this.httpGet('synsets/' + synsetId + '/relations/from');
  }

  getSynsetRelationsTo(synsetId) {
    return this.httpGet('synsets/' + synsetId + '/relations/to');
  }

  getSynsetAttributes(synsetId) {
    return this.httpGet('synsets/' + synsetId + '/attributes');
  }

}
