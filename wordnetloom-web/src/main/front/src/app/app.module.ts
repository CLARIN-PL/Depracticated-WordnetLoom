import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppComponent } from './app.component';
import { HeaderComponent } from './components/header/header.component';
import { SearchComponent } from './components/search-area/search/search.component';
import { ResultComponent } from './components/right-area/result/result.component';
import { RightAreaComponent } from './components/right-area/right-area.component';
import { SearchAreaComponent } from './components/search-area/search-area.component';
import { FormsModule } from '@angular/forms';
import { HttpService } from './services/http.service';
import { HttpModule } from '@angular/http';
import {SlimLoadingBarModule } from 'ng2-slim-loading-bar';
import { SearchResultComponent } from './components/search-area/search-result/search-result.component';
import {CurrentStateService} from './services/current-state.service';
import {RouterModule, Routes} from '@angular/router';
import {
  MatButtonModule, MatCheckboxModule, MatChipsModule, MatExpansionModule, MatInputModule, MatListModule,
  MatProgressSpinnerModule,
  MatSelectionList,
  MatSelectModule,
  MatSidenavModule, MatTabsModule, MatTooltipModule
} from '@angular/material';
import {BrowserAnimationsModule, NoopAnimationsModule} from '@angular/platform-browser/animations';
import {SidebarService} from './services/sidebar.service';
import { ClickOutsideDirective } from './directives/click-outside.directive';
import { HomeComponent } from './components/home/home.component';
import { AboutComponent } from './components/about/about.component';
import { UnitComponent } from './components/unit/unit.component';
import { ScrollBottomDirective } from './directives/scroll-bottom.directive';
import { SenseVisualizationComponent } from './components/visualizations/sense-visualization/sense-visualization.component';

const appRoutes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'about', component: AboutComponent},
  {path: 'detail', component: UnitComponent, children:[
    { path: ':lemma_id', component: ResultComponent},
    { path: 'search/:search_lemma', component: ResultComponent},
  ]}
];

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    SearchComponent,
    ResultComponent,
    SearchAreaComponent,
    RightAreaComponent,
    SearchResultComponent,
    ClickOutsideDirective,
    HomeComponent,
    AboutComponent,
    UnitComponent,
    ScrollBottomDirective,
    SenseVisualizationComponent
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
    MatTabsModule,
    MatChipsModule,
    MatProgressSpinnerModule,
    BrowserAnimationsModule
  ],
  providers: [HttpService, CurrentStateService, SidebarService],
  bootstrap: [AppComponent]
})
export class AppModule { }
