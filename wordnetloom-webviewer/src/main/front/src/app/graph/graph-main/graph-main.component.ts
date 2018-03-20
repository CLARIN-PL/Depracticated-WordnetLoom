import {Component, HostListener, OnInit, ViewEncapsulation} from '@angular/core';
import {GraphVisualService} from '../visual-service/graph-visual.service';
import {Subscription} from 'rxjs/Subscription';

declare var GraphCreator: any;


@Component({
  selector: 'app-graph-main',
  templateUrl: './graph-main.component.html',
  styleUrls: ['./graph-main.component.css'],
  encapsulation: ViewEncapsulation.None,
})

export class GraphMainComponent implements OnInit {

  graph: any;
  resizeTimeout: number;
  lastClickedNodeIdSubscription: Subscription;

  constructor(private graphService: GraphVisualService) { }

  private getSpaceForGraph() {
    const breakPoint = 768;
    if (window.innerWidth < breakPoint) {
      return [ window.innerWidth - 110, window.innerHeight - 130];
    } else {
      return [(window.innerWidth - 55) / 12 * 8 - 38, window.innerHeight - 130];
    }
  }

  ngOnInit() {
    const graphSpace = this.getSpaceForGraph();
    const width = graphSpace[0],
      height = graphSpace[1],
      showSearchBox = false;
    this.graph = new GraphCreator('graph-container', showSearchBox, width, height);
    this.graphService.initService(this.graph);
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
    this.graphService.destroy();
    this.lastClickedNodeIdSubscription.unsubscribe();
  }

}
