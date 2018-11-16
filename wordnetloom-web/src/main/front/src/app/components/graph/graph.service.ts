import { Injectable } from '@angular/core';
import {BehaviorSubject} from 'rxjs';
import {Router} from '@angular/router';
import {CurrentStateService} from '../../services/current-state.service';

@Injectable({providedIn: 'root'})
export class GraphService {

  graph = null;
  currentSynsetId = null;
  private _lastClickedNodeId = new BehaviorSubject<number>(null);
  lastClickedNodeObservable = this._lastClickedNodeId.asObservable();
  selectedNodeId = null;

  constructor(private currentStateService: CurrentStateService,
              private router: Router) { }

  initService(graph) {
    const self = this;
    self.graph = graph;
    self.graph.setVisualSettings('yiddish');

    document.addEventListener('nodeClicked', e => {
      console.log('clicked node', e['detail'].node);
      self._lastClickedNodeId.next(e['detail'].node.id);
      const id = e['detail'].node.id;
      if (id !== null) {
        this.currentStateService.setSynsetId(id);
      }
    });

    self.lastClickedNodeObservable.subscribe(id => {
      if (id !== null) {
        this.router.navigate(['detail', id]);
      }
    });
  }

  isInit() {
    return this.graph != null;
  }

  changeBaseWordWithLemma(lemma) {
    this.graph.initializeFromSynsetId(10000);
  }

  getSynsetFromSenseId(id) {
    this.graph.getSynsetFromSenseId(id);
  }

  initializeFromSynsetId(id) {
    this.currentSynsetId = id;
    this.graph.setRootNodeAsLastClickedAutomatically();
    this.graph.initializeFromSynsetId(id);
  }

  destroy() {
    this.graph = null;
  }
}
