import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';

import { AppComponent } from './app.component';
import { GraphModule } from './graph/graph.module';
import { SearchComponent } from './components/search/search.component';
import { SynsetDetailComponent } from './components/synset-detail/synset-detail.component';
import {SlimLoadingBarModule} from 'ng2-slim-loading-bar';
import { KeyobjectPipe } from './keyobject.pipe';
import { DetailPipe } from './components/synset-detail/detail.pipe';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';

@NgModule({
  declarations: [
    AppComponent,
    SearchComponent,
    SynsetDetailComponent,
    KeyobjectPipe,
    DetailPipe
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    GraphModule,
    SlimLoadingBarModule.forRoot(),
    NgbModule.forRoot()
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
