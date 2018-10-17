import {Component, ElementRef, HostListener, OnInit, ViewChild} from '@angular/core';
import {Subscription} from 'rxjs';
import {GraphService} from '../graph.service';

declare const GraphCreator: any;

@Component({
  selector: 'app-graph-main',
  templateUrl: './graph-main.component.html',
  styleUrls: ['./graph-main.component.css']
})
export class GraphMainComponent implements OnInit {

  graph: any;
  @ViewChild('graphContainerDiv') graphContainerDiv: ElementRef;
  resizeTimeout: number;
  lastClickedNodeIdSubscription: Subscription;
  breakPoint = 768;


  constructor(private graphService: GraphService) { }

  private getSpaceForGraph() {
    const breakPoint = this.breakPoint;
    if (window.innerWidth < breakPoint) {
      return [ window.innerWidth - 110, window.innerHeight - 130];
    } else {
      return [(window.innerWidth - 55) / 12 * 8 - 38, window.innerHeight - 130];
    }
  }

  ngOnInit() {
    const graphSpace = this.getSpaceForGraph();
    const height = this.graphContainerDiv.nativeElement.offsetHeight,
      width = this.graphContainerDiv.nativeElement.offsetWidth,
      showSearchBox = false;

    this.graph = new GraphCreator.GraphCreator('graph-container', showSearchBox, width, height);

    if (window.innerWidth >= this.breakPoint) {
      //                  scale, parent, top, right, bottom, left
      this.graph.showMiniMap(.25, null, '5px', null, null, '5px');
    }
    this.graphService.initService(this.graph);
    this.graphService.initializeFromSynsetId(10);
    console.log(this.graph);
  }

  onDestroy() {
    this.graphService.destroy();
    this.lastClickedNodeIdSubscription.unsubscribe();
  }

  graphContainerResized(event) {
    if (this.resizeTimeout) {
      clearTimeout(this.resizeTimeout);
    }
    this.resizeTimeout = setTimeout((() => {
      console.log(event);
      // const graphSpace = this.getSpaceForGraph();
      const width = event.newWidth,
        height = event.newHeight;
      this.graph.resizeSVG(width, height);
    }).bind(this), 250);
  }


}
