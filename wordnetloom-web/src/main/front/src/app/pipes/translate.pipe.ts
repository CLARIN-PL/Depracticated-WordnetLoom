import { Pipe, PipeTransform } from '@angular/core';
import {TranslateService} from '../services/translate.service';

@Pipe({
  name: 'translate'
})
export class TranslatePipe implements PipeTransform {

  constructor(private translate: TranslateService) {}

  transform(key: any, args?: any): any {
    // console.log(key);
    // console.log(this.translate.data)
    // return this.translate.get(key);
    return this.translate.data[key] || key;
  }

}
