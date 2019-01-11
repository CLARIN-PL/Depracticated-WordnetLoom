import {AfterViewInit, Component, ElementRef, Inject, Input, OnInit, ViewChild} from '@angular/core';
import {Subscription} from 'rxjs';
import {GraphService} from '../graph.service';
import { ElementQueries, ResizeSensor } from 'css-element-queries' ;
import {HttpService} from "../../../services/http.service";

// declare const GraphCreator: any;

@Component({
  selector: 'app-graph-main',
  templateUrl: './graph-main.component.html',
  styleUrls: ['./graph-main.component.css']
})
export class GraphMainComponent implements OnInit, AfterViewInit {
  @Input() showMiniMap = true;
  @Input() updateSpaceForGraphAfterInit = false;
  @Input() senseId = null;
  // generating random id for new instances of graph
  graphContainerId = 'graph-container' + Math.random().toString(36).substr(2);
  graph: any;
  @ViewChild('graphContainerDiv') graphContainerDiv: ElementRef;
  resizeTimeout: number;
  lastClickedNodeIdSubscription: Subscription;
  breakPoint = 768;


  constructor(@Inject('GraphCreator') public graphCreator: any, private graphService: GraphService, private http: HttpService) { }

  private getSpaceForGraph() {
    const breakPoint = this.breakPoint;
    if (window.innerWidth < breakPoint) {
      return [ window.innerWidth - 110, window.innerHeight - 130];
    } else {
      return [(window.innerWidth - 55) / 12 * 8 - 38, window.innerHeight - 130];
    }
  }

  ngOnInit() {}

  ngAfterViewInit() {
    this.initGraph();

    ElementQueries.listen();
    ElementQueries.init();

    const resizeSensor = new ResizeSensor(this.graphContainerDiv.nativeElement, this.graphContainerResized.bind(this));
  }

  initGraph() {
    const self = this;
    const graphSpace = this.getSpaceForGraph();
    const height = this.graphContainerDiv.nativeElement.offsetHeight,
      width = this.graphContainerDiv.nativeElement.offsetWidth,
      showSearchBox = false;

    this.graph = new this.graphCreator.GraphCreator(this.graphContainerId, showSearchBox, width, height);

    this.graph.api.getGraph = function(senseId, callback) {
      console.log(senseId, callback);
      this._getJson(self.http.apiBase + 'senses/{id}/graph'.replace('{id}', senseId), function(json) {
        if (json) {
          callback(json);
        } else {
          callback(null);
        }
      }, true);
    };

    if (this.updateSpaceForGraphAfterInit) {
      setTimeout(() => {
        this.graph.resizeSVG(width + 100, height);
      }, 2000);
    }

    if (this.showMiniMap && window.innerWidth >= this.breakPoint) {
      //                  scale, parent, top, right, bottom, left
      this.graph.showMiniMap(.25, null, '5px', null, null, '5px');
    }
    this.graphService.initService(this.graph);
    this.graphContainerDiv.nativeElement.onresize = this.graphContainerResized;

    // initialize graph
    if (this.senseId) {
      this.graphService.initializeFromSenseId(this.senseId);
    }
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
      const width = event.width,
        height = event.height;
      this.graph.resizeSVG(width, height);
    }).bind(this), 250);
  }

}
