import {Injectable } from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {Subject} from 'rxjs/Subject';
import {HttpService} from './http.service';
import {Router} from '@angular/router';
import {CurrentStateService} from './current-state.service';
import {YiddishContent} from '../components/right-area/result/yiddishcontent';
import {SenseContent} from '../components/right-area/result/sensecontent';

@Injectable()
export class SidebarService {
  constructor(private http: HttpService, private router: Router, private state: CurrentStateService) { }
  nav = null;
  list = [];
  listObservable = new Subject<any[]>();
  page = 0;
  batchSize = 50;
  form = null;
  isLoading = false;
  isLoadingObservable = new Subject<{loading: boolean, recordsStr: string}>();
  totalRecords = null;
  recordsLoaded = 0;

  init(navRef) {
    this.nav = navRef;
  }

  getListObservable (): Observable<any[]> {
    return this.listObservable.asObservable();
  }

  getListLoadinObservable (): Observable<{loading: boolean, recordsStr: string}> {
    return this.isLoadingObservable.asObservable();
  }

  private getOptions() {
    const self = this;
    if (self.isLoading) {
      return;
    }

    if (self.totalRecords !== null && self.recordsLoaded >= self.totalRecords) {
      return; // everything is already loaded
    }

    this.isLoading = true;
    this.isLoadingObservable.next({loading: this.isLoading, recordsStr: '' + self.recordsLoaded + '/' + self.totalRecords});


    self.http.getSearchOptions(this.form, this.page, this.batchSize).subscribe(
      (response) => {
        self.addSearchOptions(response);
        self.totalRecords = response['size'];
        self.recordsLoaded = Math.min(self.recordsLoaded + self.batchSize, self.totalRecords);

        if (self.page === 0) { // first batch loaded
          if (response['rows'].length > 0) {
            self.router.navigate(['detail', response['rows'][0]['id']]);
          } else {
            self.router.navigate(['detail', 'not_found']);
            self.addSearchOptions({rows: [{id: 'not_found', label: 'nothing found'}]});
          }
        }

        self.page++;
        self.isLoading = false;
        self.isLoadingObservable.next({loading: this.isLoading, recordsStr: '' + self.recordsLoaded + '/' + self.totalRecords});
      }
    );
  }

  getAllOptions (form) {
    this.list = [];
    this.page = 0;
    this.recordsLoaded = 0;
    this.totalRecords = null; // resetting total records cnt

    // if lemma is set and contains yiddish characters
    if (form.lemma && form.lemma.search(/[\u0590-\u05FF]/) >= 0) {
      this.state.setListAlphabetStyle('yiddish');
    }
    this.form = form;

    this.getOptions();
  }

  loadMoreOptions() {
    this.getOptions();
  }

  addSearchOptions (response) {
    this.list = this.list.concat(response.rows);
    this.listObservable.next(this.list);
  }

  getList() {
    return this.list;
  }

  assignSingleOptionIfEmpty(senseContent: SenseContent, yiddishContent?: YiddishContent[] | null) {
    if (this.list.length === 0) {

      if (!yiddishContent) {
        yiddishContent = [];
      }

      this.list = [];
      this.list.push({
        label: senseContent.lemma,
        id: senseContent.senseId,
        variant: senseContent.variant,
        variants: yiddishContent.map(yContent => {
          return {
            id: yContent.id,
            variant_type: yContent.variant_type,
            latin: yContent.differentAlphabetLemmas['latin'],
            yiddish: yContent.differentAlphabetLemmas['yiddish'],
            yivo: yContent.differentAlphabetLemmas['yivo']
          };
        })
      });
      this.listObservable.next(this.list);
    }
  }

  loadOptionsFromParameters(params) {
    this.getAllOptions(params);
  }
}
