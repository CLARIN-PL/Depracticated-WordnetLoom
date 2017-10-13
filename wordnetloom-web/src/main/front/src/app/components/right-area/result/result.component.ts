import {ChangeDetectorRef, Component, OnDestroy, OnInit} from '@angular/core';
import {HttpService} from "../../../services/http.service";
import {CurrentStateService} from "../../../services/current-state.service";

@Component({
  selector: 'app-result',
  templateUrl: './result.component.html',
  styleUrls: ['./result.component.css']
})
export class ResultComponent implements OnInit, OnDestroy {
  content = {
    lemma: '',
    variant: '',
    fields: [],
    areas: []
  };

  currentLexicalUnitId = null;
  lexicalUnitIdSubscription = null;
  constructor(private http: HttpService, private currentState: CurrentStateService, private cd: ChangeDetectorRef) { }

  ngOnInit() {
    this.content.lemma = 'test lemma | abc | cde';
    this.content.variant = '1';
    this.content.fields.push({name: 'Semantic fields', values: ['test val1', 'test val2'] });
    this.content.fields.push({name: 'Style', values: ['test val1', 'test val2'] });
    this.content.fields.push({name: 'Domain', values: ['test val1', 'test val2'] });
    this.content.fields.push({name: 'Attested', values: ['test val1', 'test val2'] });
    this.content.fields.push({name: 'Status', values: ['test val1', 'test val2'] });
    this.content.fields.push({name: 'Meaning', values: ['test val1', 'test val2'] });
    this.content.areas.push({
      name: 'Lexical relations',
      fields: [
        { name: 'Synonimy', values: ['first field val1', 'first field val2']},
        { name: 'Synonimy', values: ['first field val1', 'first field val2']},
        { name: 'Synonimy', values: ['first field val1', 'first field val2']},
      ]
    });

    // console.log(JSON.stringify(this.content));
    this.content = null;
    this.lexicalUnitIdSubscription =  this.currentState.getLexicalUnitIdSubscription();
    this.lexicalUnitIdSubscription.subscribe((id) => {
      this.currentLexicalUnitId = id;
      this.updateCurrentLexicalUnit();
    });
  }


  private updateCurrentLexicalUnit() {
    this.http.getLexicalUnitDetails(this.currentLexicalUnitId).subscribe((response) => {
      console.log(JSON.stringify(response));
      this.content = response;
    });
  }

  ngOnDestroy() {
    this.lexicalUnitIdSubscription.unsubscribe();
  }
}
