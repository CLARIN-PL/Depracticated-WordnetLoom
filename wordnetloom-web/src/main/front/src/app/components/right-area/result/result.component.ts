import {ChangeDetectorRef, Component, OnDestroy, OnInit} from '@angular/core';
import {HttpService} from "../../../services/http.service";
import {CurrentStateService} from "../../../services/current-state.service";
import {ActivatedRoute} from "@angular/router";
import {Subscription} from "rxjs/Subscription";
import {SenseContent} from "./sensecontent";

@Component({
  selector: 'app-result',
  templateUrl: './result.component.html',
  styleUrls: ['./result.component.css']
})
export class ResultComponent implements OnInit, OnDestroy {
  // content = {
  //   lemma: '',
  //   variant: '',
  //   fields: [],
  //   areas: []
  // };

  content: SenseContent = null;

  // currentLexicalUnitId = null;
  // lexicalUnitIdSubscription = null;

  subscription: Subscription = null;

  lemmaId: number;
  variantId: number;


  constructor(private http: HttpService,
              private currentState: CurrentStateService,
              private cd: ChangeDetectorRef,
              private route: ActivatedRoute
    ) { }

  ngOnInit() {
    // this.content.lemma = 'test lemma | abc | cde';
    // this.content.variant = '1';
    // this.content.fields.push({name: 'Semantic fields', values: ['test val1', 'test val2'] });
    // this.content.fields.push({name: 'Style', values: ['test val1', 'test val2'] });
    // this.content.fields.push({name: 'Domain', values: ['test val1', 'test val2'] });
    // this.content.fields.push({name: 'Attested', values: ['test val1', 'test val2'] });
    // this.content.fields.push({name: 'Status', values: ['test val1', 'test val2'] });
    // this.content.fields.push({name: 'Meaning', values: ['test val1', 'test val2'] });
    // this.content.areas.push({
    //   name: 'Lexical relations',
    //   fields: [
    //     { name: 'Synonimy', values: ['first field val1', 'first field val2']},
    //     { name: 'Synonimy', values: ['first field val1', 'first field val2']},
    //     { name: 'Synonimy', values: ['first field val1', 'first field val2']},
    //   ]
    // });
    this.content = null;

    this.lemmaId = +this.route.snapshot.paramMap.get('lemma_id');
    this.variantId = +this.route.snapshot.paramMap.get('variant_id');

    this.subscription = this.route.params.subscribe(params => {
      this.lemmaId = +params['lemma_id']; // (+) converts string 'id' to a number
      // this.variantId = +params['variant_id'];
      this.updateCurrentLexicalUnit();
    });
  }


  private updateCurrentLexicalUnit() {
    this.http.getLexicalUnitDetails(this.lemmaId).subscribe((response) => {
      this.content = new SenseContent(response);
      // this.content = response;
      // this.content = new SenseContent();
      // this.content.lemma = response['Lemma:'] + ' | ';// +  latin, english, evo

    });
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }
}
