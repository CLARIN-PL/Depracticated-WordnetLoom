import {EventEmitter, Injectable} from '@angular/core';

@Injectable()
export class CurrentStateService {
  lexicalUnitId = null;
  lexicalUnitSubscription = new EventEmitter<any>();


  constructor() { }

  setLexicalUnit(id) {
    this.lexicalUnitId = id;
    this.lexicalUnitSubscription.emit(id);
  }

  getLexicalUnitId() {
    return this.lexicalUnitId;
  }

  getLexicalUnitIdSubscription() {
    return this.lexicalUnitSubscription;
  }
}
