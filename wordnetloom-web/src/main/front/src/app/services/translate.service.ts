import { Injectable } from '@angular/core';
import {HttpService} from './http.service';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TranslateService {

  data: any = {};

  constructor(private http: HttpService) { }

  use(lang: string): Promise<{}> {
    return new Promise<{}> ((resolve, reject) => {
      this.http.getLang(lang)
        .subscribe(
          translation => {
            console.log(translation);
            this.data =  Object.assign({}, translation || {});
            resolve(this.data);
          },
          error => {
            this.data = {};
            resolve(this.data);
        });
    });
  }

  get(key) {
    return this.data[key] || key;
  }
}
