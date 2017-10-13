import {Component, OnDestroy, OnInit} from '@angular/core';
import {CurrentStateService} from "../../services/current-state.service";
import {HttpService} from "../../services/http.service";
import {ActivatedRoute, Route, Router} from "@angular/router";
import {Subscription} from "rxjs/Subscription";

@Component({
  selector: 'app-right-area',
  templateUrl: './right-area.component.html',
  styleUrls: ['./right-area.component.css']
})
export class RightAreaComponent implements OnInit, OnDestroy {
  currentLexicalUnitId = null;
  lexicalUnitIdSubscription = null;
  subscription: Subscription = null;

  lemmaId: number;
  variantId: number;

  constructor(private http: HttpService, private currentState: CurrentStateService, private route: ActivatedRoute) { }

  ngOnInit() {
    console.log('oninit called');
    this.lemmaId = +this.route.snapshot.paramMap.get('lemma_id');
    this.variantId = +this.route.snapshot.paramMap.get('variant_id');
    console.log(this.lemmaId, this.variantId);

    this.subscription = this.route.params.subscribe(params => {
      console.log(params);
      this.lemmaId = +params['id']; // (+) converts string 'id' to a number
      this.variantId = +params['id']; // (+) converts string 'id' to a number
    });
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }


}
