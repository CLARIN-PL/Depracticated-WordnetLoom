import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppComponent } from './app.component';
import { HeaderComponent } from './components/header/header.component';
import { SearchComponent } from './components/search/search.component';
import { ResultComponent } from './components/right-area/result/result.component';
import { RightAreaComponent } from './components/right-area/right-area.component';
import { FormsModule } from '@angular/forms';
import { HttpService } from './services/http.service';
import { HttpModule } from '@angular/http';
import {CurrentStateService} from './services/current-state.service';
import {AvailableSearchFiltersService} from './services/configuration/available-search-filters.service';
import {RouterModule, Routes} from '@angular/router';
import {
  MatButtonModule, MatCheckboxModule, MatChipsModule, MatExpansionModule, MatInputModule, MatListModule,
  MatFormFieldModule,
  MatProgressSpinnerModule,
  MatSelectionList,
  MatSelectModule,
  MatSidenavModule, MatTabsModule, MatTooltipModule,
  MatButtonToggleModule,
} from '@angular/material';
import {BrowserAnimationsModule, NoopAnimationsModule} from '@angular/platform-browser/animations';
import {SidebarService} from './services/sidebar.service';
import { ClickOutsideDirective } from './directives/click-outside.directive';
import { HomeComponent } from './components/home/home.component';
import { AboutComponent } from './components/about/about.component';
import { UnitComponent } from './components/unit/unit.component';
import { ScrollBottomDirective } from './directives/scroll-bottom.directive';
import { SenseVisualizationComponent } from './components/visualizations/sense-visualization/sense-visualization.component';
import { IKeyboardLayouts, KeyboardClassKey, keyboardLayouts, MAT_KEYBOARD_LAYOUTS, MatKeyboardModule } from '@ngx-material-keyboard/core';
import { InputWithKeyboardComponent } from './components/input-with-keyboard/input-with-keyboard.component';
import { ClickOutsideModule } from 'ng-click-outside';

const appRoutes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'about', component: AboutComponent},
  {path: 'detail', component: UnitComponent, children: [
    { path: 'search_params', component: ResultComponent},
    { path: ':lemma_id', component: ResultComponent},
    { path: 'search/:search_lemma', component: ResultComponent},

  ]}
];

const customLayouts: IKeyboardLayouts = {
  ...keyboardLayouts,
  'yiddish_original': {
    'name': 'Yiddish layout',
    'keys': [
      [
        [';', '~'],
        ['1', '!', '\u05B2', '\u05B2'],
        ['2', '@', '\u05B3', '\u05B3'],
        ['3', '#', '\u05B1', '\u05B1'],
        ['4', '$', '\u05B4', '\u05B4'],
        ['5', '%', '\u05B5', '\u05B5'],
        ['6', '^', '\u05B7', '\u05B7'],
        ['7', '&', '\u05B8', '\u05B8'],
        ['8', '*', '\u05BB', '\u05BB'],
        ['9', ')', '\u05B6', '\u05B6'],
        ['0', '(', '\u05B0', '\u05B0'],
        ['-', '_', '\u05BF', '\u05BF'],
        ['=', '+', '\u05B9', '\u05B9'],
        [KeyboardClassKey.Bksp, KeyboardClassKey.Bksp, KeyboardClassKey.Bksp, KeyboardClassKey.Bksp]
      ],
      [
        [KeyboardClassKey.Tab, KeyboardClassKey.Tab, KeyboardClassKey.Tab, KeyboardClassKey.Tab],
        ['/', '', '\u05F4', '\u05F4'],
        ['\'', '', '\u05F3', '\u05F3'],
        ['\u05E7', '', '\u20AC'],
        ['\u05E8'],
        ['\u05D0', '', '\u05D0\u05B7', '\uFB2E'],
        ['\u05D8', '', '\u05D0\u05B8', '\uFB2F'],
        ['\u05D5', '\u05D5\u05B9', '\u05D5\u05BC', '\uFB35'],
        ['\u05DF', '', '\u05D5\u05D5', '\u05F0'],
        ['\u05DD', '', '\u05BC'],
        ['\u05E4', '', '\u05E4\u05BC', '\uFB44'],
        [']', '}', '\u201E', '\u201D'],
        ['[', '{', '\u201A', '\u2019'],
        ['\\', '|', '\u05BE', '\u05BE']
      ],
      [
        [KeyboardClassKey.Caps, KeyboardClassKey.Caps, KeyboardClassKey.Caps, KeyboardClassKey.Caps],
        ['\u05E9', '\u05E9\u05C1', '\u05E9\u05C2', '\uFB2B'],
        ['\u05D3', '', '\u20AA'],
        ['\u05D2', '\u201E'],
        ['\u05DB', '', '\u05DB\u05BC', '\uFB3B'],
        ['\u05E2', '', '', '\uFB20'],
        ['\u05D9', '', '\u05D9\u05B4', '\uFB1D'],
        ['\u05D7', '', '\u05F2\u05B7', '\uFB1F'],
        ['\u05DC', '\u05DC\u05B9', '\u05D5\u05D9', '\u05F1'],
        ['\u05DA', '', '', '\u05F2'],
        ['\u05E3', ':', '\u05E4\u05BF', '\uFB4E'],
        [',', '"', ';', '\u05B2'],
        [KeyboardClassKey.Enter, KeyboardClassKey.Enter, KeyboardClassKey.Enter, KeyboardClassKey.Enter]
      ],
      [
        [KeyboardClassKey.Shift, KeyboardClassKey.Shift, KeyboardClassKey.Shift, KeyboardClassKey.Shift],
        ['\u05D6', '', '\u2013', '\u2013'],
        ['\u05E1', '', '\u2014', '\u2014'],
        ['\u05D1', '\u05DC\u05B9', '\u05D1\u05BF', '\uFB4C'],
        ['\u05D4', '', '\u201D', '\u201C'],
        ['\u05E0', '', '\u059C', '\u059E'],
        ['\u05DE', '', '\u2019', '\u2018'],
        ['\u05E6', '', '\u05E9\u05C1', '\uFB2A'],
        ['\u05EA', '>', '\u05EA\u05BC', '\uFB4A'],
        ['\u05E5', '<'],
        ['.', '?', '\u2026'],
        [KeyboardClassKey.Shift, KeyboardClassKey.Shift, KeyboardClassKey.Shift, KeyboardClassKey.Shift]
      ],
      [
        [KeyboardClassKey.Space, KeyboardClassKey.Space, KeyboardClassKey.Space, KeyboardClassKey.Space],
        [KeyboardClassKey.Alt, KeyboardClassKey.Alt, KeyboardClassKey.Alt, KeyboardClassKey.Alt]
      ]
    ],
    'lang': ['yi']
  },
  'yiddish': {
    'name': 'Yiddish layout',
    'keys': [
      [
        ['\u05E7', '', '\u20AC'],
        ['\u05E8'],
        ['\u05D0', '', '\u05D0\u05B7', '\uFB2E'],
        ['\u05D8', '', '\u05D0\u05B8', '\uFB2F'],
        ['\u05D5', '\u05D5\u05B9', '\u05D5\u05BC', '\uFB35'],
        ['\u05DF', '', '\u05D5\u05D5', '\u05F0'],
        ['\u05DD', '', '\u05BC'],
        ['\u05E4', '', '\u05E4\u05BC', '\uFB44'],
      ],
      [
        ['\u05E9', '\u05E9\u05C1', '\u05E9\u05C2', '\uFB2B'],
        ['\u05D3', '', '\u20AA'],
        ['\u05D2', '\u201E'],
        ['\u05DB', '', '\u05DB\u05BC', '\uFB3B'],
        ['\u05E2', '', '', '\uFB20'],
        ['\u05D9', '', '\u05D9\u05B4', '\uFB1D'],
        ['\u05D7', '', '\u05F2\u05B7', '\uFB1F'],
        ['\u05DC', '\u05DC\u05B9', '\u05D5\u05D9', '\u05F1'],
        ['\u05DA', '', '', '\u05F2'],
        ['\u05E3', ':', '\u05E4\u05BF', '\uFB4E'],
      ],
      [
        ['\u05D6', '', '\u2013', '\u2013'],
        ['\u05E1', '', '\u2014', '\u2014'],
        ['\u05D1', '\u05DC\u05B9', '\u05D1\u05BF', '\uFB4C'],
        ['\u05D4', '', '\u201D', '\u201C'],
        ['\u05E0', '', '\u059C', '\u059E'],
        ['\u05DE', '', '\u2019', '\u2018'],
        ['\u05E6', '', '\u05E9\u05C1', '\uFB2A'],
      ],
      [
        // small, caps, alternative
        ['č', '', '\u20AC'],
        ['\u01C6', '\u01C5'], // ['dž'],
        ['š'],
        ['ž'],
      ],
    ],
    'lang': ['yi']
  }
};

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    SearchComponent,
    ResultComponent,
    RightAreaComponent,
    ClickOutsideDirective,
    HomeComponent,
    AboutComponent,
    UnitComponent,
    ScrollBottomDirective,
    SenseVisualizationComponent,
    InputWithKeyboardComponent
  ],
  imports: [
    RouterModule.forRoot(
      appRoutes,
      // {enableTracing: true} // only for debug
    ),
    BrowserModule,
    FormsModule,
    HttpModule,
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
    MatFormFieldModule,
    BrowserAnimationsModule,
    MatKeyboardModule,
    MatButtonToggleModule,
    ClickOutsideModule
  ],
  providers: [HttpService, CurrentStateService, SidebarService, AvailableSearchFiltersService,
      { provide: MAT_KEYBOARD_LAYOUTS, useValue: customLayouts }
    ],
  bootstrap: [AppComponent]
})
export class AppModule { }
