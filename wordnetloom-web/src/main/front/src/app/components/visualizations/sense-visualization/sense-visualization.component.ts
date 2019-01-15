import {Component, HostListener, Input, OnDestroy, OnInit, ViewEncapsulation} from '@angular/core';
import {Subscription} from 'rxjs/Subscription';
import {environment} from '../../../../environments/environment';

declare const GraphCreator: any;

@Component({
  selector: 'app-sense-visualization',
  templateUrl: './sense-visualization.component.html',
  styleUrls: ['./sense-visualization.component.css'],
  encapsulation: ViewEncapsulation.None,
})
export class SenseVisualizationComponent implements OnInit, OnDestroy {
  @Input() senseId: string;
  @Input() visible: boolean;


  graph: any;
  resizeTimeout: number;
  lastClickedNodeIdSubscription: Subscription;

  constructor() { }

  private getSpaceForGraph() {
    const breakPoint = 768;

    if (window.innerWidth < breakPoint) {
      return [ window.innerWidth - 110, 500];
    } else {
      return [(window.innerWidth) / 12 * 10 - 50, 500];
    }
  }

  ngOnInit() {
    if (environment.production) {
      return;
    }
    const graphSpace = this.getSpaceForGraph();
    const width = graphSpace[0],
      height = graphSpace[1],
      showSearchBox = false;
    this.graph = new GraphCreator('graph-container', showSearchBox, width, height);
    this.graph.setLanguage('en');
    this.graph.initializeFromSynsetId(this.senseId);

    this.graph.eventDispatch.on('ordinary-node-mousedown', function(e) {
      if (e) {
        this._lastClickedNodeId.next(e.nodeId);
      }
    });
  }

  ngOnDestroy() {
    this.lastClickedNodeIdSubscription.unsubscribe();
    this.graph.destroy();
    this.graph = null;
  }

}
