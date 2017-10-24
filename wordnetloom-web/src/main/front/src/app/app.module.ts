import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppComponent } from './app.component';
import { HeaderComponent } from './components/header/header.component';
import { SearchComponent } from './components/left-area/search/search.component';
import { ResultComponent } from './components/right-area/result/result.component';
import { RightAreaComponent } from './components/right-area/right-area.component';
import { LeftAreaComponent } from './components/left-area/left-area.component';
import { FormsModule } from '@angular/forms';
import { HttpService } from './services/http.service';
import { HttpModule } from '@angular/http';
import {SlimLoadingBarModule } from 'ng2-slim-loading-bar';
import { SearchResultComponent } from './components/left-area/search-result/search-result.component';
import {CurrentStateService} from './services/current-state.service';
import {RouterModule, Routes} from '@angular/router';
import {
  MatButtonModule, MatCheckboxModule, MatExpansionModule, MatInputModule, MatListModule, MatSelectionList,
  MatSelectModule,
  MatSidenavModule, MatTooltipModule
} from '@angular/material';
import {BrowserAnimationsModule, NoopAnimationsModule} from '@angular/platform-browser/animations';
import {SidebarService} from './services/sidebar.service';

const appRoutes: Routes = [
  {path: ':lemma_id', component: ResultComponent},
  {path: ':lemma_id/:variant_id', component: ResultComponent}
];

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    SearchComponent,
    ResultComponent,
    LeftAreaComponent,
    RightAreaComponent,
    SearchResultComponent
  ],
  imports: [
    RouterModule.forRoot(
      appRoutes,
      // {enableTracing: true} // only for debug
    ),
    BrowserModule,
    FormsModule,
    HttpModule,
    SlimLoadingBarModule.forRoot(),
    MatButtonModule,
    MatCheckboxModule,
    MatInputModule,
    MatSelectModule,
    MatSidenavModule,
    MatListModule,
    MatExpansionModule,
    MatTooltipModule,
    BrowserAnimationsModule
  ],
  providers: [HttpService, CurrentStateService, SidebarService],
  bootstrap: [AppComponent]
})
export class AppModule { }
