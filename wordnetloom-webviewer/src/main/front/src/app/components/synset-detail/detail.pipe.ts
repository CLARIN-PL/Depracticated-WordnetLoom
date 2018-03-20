import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'detailPipe'
})
export class DetailPipe implements PipeTransform {

  transform(value: any, args?: any): any {
    let ret = '';

    switch (value.type.id) {
      case 6: { // isabstract
        ret = value.value === 1 ? 'True' : 'False';
        break;
      }
      case 16: { // owner
        ret = value.value.replace('.', ' ');
        break;
      }
      default: {
        ret = value.value;
      }
    }
    return ret;
  }

}
