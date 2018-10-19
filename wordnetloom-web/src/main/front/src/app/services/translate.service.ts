import {Injectable} from '@angular/core';
import {HttpService} from './http.service';


@Injectable()
export class TranslateService {

  data: any = {};
  selectedLang = null;

  cachedData = {};

  constructor(private http: HttpService) { }

  use(lang: string): Promise<{}> {
    if (this.selectedLang === lang){
      return;
    }

    this.selectedLang = lang;

    // console.log(this.cachedData);

    if (this.cachedData[this.selectedLang]) {
        this.data = this.cachedData[this.selectedLang];
        return;
    }

    return new Promise<{}> ((resolve, reject) => {
      this.http.getLang(lang)
        .subscribe(
          translation => {
            this.data =  Object.assign({}, translation || {});
            this.cachedData[lang] = this.data;
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
