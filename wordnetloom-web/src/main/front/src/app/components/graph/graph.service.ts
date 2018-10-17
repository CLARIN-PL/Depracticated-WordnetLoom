import { Injectable } from '@angular/core';
import {BehaviorSubject} from 'rxjs';

@Injectable({providedIn: 'root'})
export class GraphService {

  graph = null;
  currentSynsetId = null;
  private _lastClickedNodeId= new BehaviorSubject<number>(null);
  lastClickedNodeObservable = this._lastClickedNodeId.asObservable();

  constructor() { }

  initService(graph) {
    const self = this;
    self.graph = graph;
    self.graph.setVisualSettings('yiddish');

    document.addEventListener('nodeClicked', e => {
      self._lastClickedNodeId.next(e['detail'].node.id);
    });
  }

  isInit() {
    return this.graph != null;
  }

  changeBaseWordWithLemma(lemma) {
    console.log('change word to ' + lemma);
    console.log(this.graph);
    this.graph.initializeFromSynsetId(10000);
  }

  getSynsetFromSenseId(id) {
    this.graph.getSynsetFromSenseId(id);
  }

  initializeFromSynsetId(id) {
    this.currentSynsetId = id;
    this.graph.initializeFromSynsetId(id);
  }

  destroy() {
    this.graph = null;
  }
}
