// https://stackoverflow.com/questions/41396435/how-to-iterate-object-object-using-ngfor-in-angular-2

import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'keyobject'
})
export class KeyobjectPipe implements PipeTransform {

  transform(value: any, args?: any): any {
    let keys = [];
    for (let key in value) {
      keys.push( key );
    }
    return keys;
  }

}
