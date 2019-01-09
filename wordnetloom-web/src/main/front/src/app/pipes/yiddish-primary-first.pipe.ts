import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'yiddishPrimaryFirst'
})
export class YiddishPrimaryFirstPipe implements PipeTransform {

  transform(value: any[], args?: any): any {
    if (!value) {
      return value;
    }
    if (value[0].variant_type === 'Yiddish_Primary_Lemma') {
      return value;
    }
    const yiddishIdx = value.findIndex(variant => variant.variant_type === 'Yiddish_Primary_Lemma');
    value.unshift(value.splice(yiddishIdx, 1)[0]); // return Yiddish primary lemma as first
    return value;
  }

}
