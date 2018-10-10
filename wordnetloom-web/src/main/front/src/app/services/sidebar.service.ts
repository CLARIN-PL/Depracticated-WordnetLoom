import {Injectable } from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {Subject} from 'rxjs/Subject';
import {HttpService} from './http.service';
import {Router} from '@angular/router';

@Injectable()
export class SidebarService {
  constructor(private http: HttpService, private router: Router) { }
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
        self.totalRecords = response['paging']['totalRecords'];
        self.recordsLoaded = Math.min(self.recordsLoaded + self.batchSize, self.totalRecords);

        if (self.page === 0) { // first batch loaded
          if (response['entries'].length > 0) {
            self.router.navigate(['detail', response['entries'][0]['id']]);
          } else {
            self.router.navigate(['detail', 'not_found']);
            self.addSearchOptions({entries: [{id: 'nothing_found', lemma: 'nothing found'}]});
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
    this.form = form;

    this.getOptions();
  }

  loadMoreOptions() {
    this.getOptions();
  }

  addSearchOptions (response) {
    this.list = this.list.concat(response.entries);
    console.log(this.list);
    this.listObservable.next(this.list);
  }

  assignSingleOptionIfEmpty(content) {
    console.log(this.list)
    if (this.list.length === 0) {
      this.list = [];
      this.list.push({
        lemma: content.lemma,
        id: content.senseId
      });
      this.listObservable.next(this.list);
    }
  }

}
