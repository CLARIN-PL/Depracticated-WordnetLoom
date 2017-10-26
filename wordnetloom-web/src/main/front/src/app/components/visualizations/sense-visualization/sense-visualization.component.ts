import {Component, HostListener, Input, OnInit, ViewEncapsulation} from '@angular/core';
import {Subscription} from 'rxjs/Subscription';
import {environment} from "../../../../environments/environment";

declare const GraphCreator: any;

@Component({
  selector: 'app-sense-visualization',
  templateUrl: './sense-visualization.component.html',
  styleUrls: ['./sense-visualization.component.css'],
  encapsulation: ViewEncapsulation.None,
})
export class SenseVisualizationComponent implements OnInit {
  @Input() senseId: number;
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
    this.graph.initializeFromSynsetId(10);
    // this.graph.initializeFromSenseId(this.senseId);

    this.graph.eventDispatch.on('ordinary-node-mousedown', function(e){
      if (e) {
        this._lastClickedNodeId.next(e.nodeId);
      }
    });
  }

  @HostListener('window:resize', ['$event'])
  onResize() {
    if (this.resizeTimeout) {
      clearTimeout(this.resizeTimeout);
    }
    this.resizeTimeout = setTimeout((() => {
      console.log('resizing');
      const graphSpace = this.getSpaceForGraph();
      const width = graphSpace[0],
        height = graphSpace[1];
      this.graph.resizeSVG(width, height);
    }).bind(this), 100);
  }

  onDestroy() {
    this.lastClickedNodeIdSubscription.unsubscribe();
    this.graph = null;
    this.graph.destroy();
  }

}
