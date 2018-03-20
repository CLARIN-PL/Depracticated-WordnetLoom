import { Component } from '@angular/core';
import {GraphVisualService} from './graph/visual-service/graph-visual.service';
import {GraphHttpService} from './graph/http-service/graph-http.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  currentNode = null;
  private lastClickedNodeIdSubscription;

  constructor(private graphVisualService: GraphVisualService,
              private graphHttpService: GraphHttpService) {}

  ngOnInit() {
    this.lastClickedNodeIdSubscription = this.graphVisualService.lastClickedNodeObservable
      .subscribe(id => this.currentNode = id );
  }

  handleSelectLemmaEvent(nodeId) {
    this.currentNode = nodeId;
  }
}
