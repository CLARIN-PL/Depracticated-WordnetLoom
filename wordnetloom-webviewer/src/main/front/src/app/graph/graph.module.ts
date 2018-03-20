import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GraphVisualService } from './visual-service/graph-visual.service';
import {GraphMainComponent} from './graph-main/graph-main.component';
import {GraphHttpService} from './http-service/graph-http.service';
import {SlimLoadingBarModule} from 'ng2-slim-loading-bar';

@NgModule({
  imports: [
    CommonModule,
    SlimLoadingBarModule.forRoot()
  ],
  declarations: [GraphMainComponent],
  providers: [GraphVisualService, GraphHttpService],
  exports: [GraphMainComponent]
})
export class GraphModule {}
