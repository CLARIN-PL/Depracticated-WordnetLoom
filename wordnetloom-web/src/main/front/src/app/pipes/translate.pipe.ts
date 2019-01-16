import {Pipe, PipeTransform} from '@angular/core';
import {TranslateService} from '../services/translate.service';

@Pipe({
  name: 'translate',
  pure: false
})
export class TranslatePipe implements PipeTransform {

  constructor(private translate: TranslateService) {}

  transform(key: any, args?: any): any {
    return this.translate.get(key);
  }

}
