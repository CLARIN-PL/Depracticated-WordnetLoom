import {Injectable, ViewChild} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {Subject} from 'rxjs/Subject';
import {HttpService} from "./http.service";

@Injectable()
export class SidebarService {
  constructor(private http: HttpService) { }
  nav = null;
  list = [];
  listObservable = new Subject<any[]>();

  init(navRef) {
    this.nav = navRef;
  }

  getListObservable (): Observable<any[]> {
    return this.listObservable.asObservable();
  }

  getAllOptions (form) {
    const self = this;
    this.list = [];
    let page = 0;

    const partialResultCallback = function(){
      self.http.getSearchOptions(form, page, 50).subscribe(
        (response) => {
          self.addSearchOptions(response);
          page++;
          if (response.entries.length > 0) {
            partialResultCallback();
          }
        }
      );
    };
    partialResultCallback();
  }

  addSearchOptions (response) {
    this.list = this.list.concat(response.entries);
    this.listObservable.next(this.list);
    // this.nav.open();
  }

}
