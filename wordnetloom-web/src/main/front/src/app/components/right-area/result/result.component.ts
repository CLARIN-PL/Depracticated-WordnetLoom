import {ChangeDetectorRef, Component, OnDestroy, OnInit} from '@angular/core';
import {HttpService} from '../../../services/http.service';
import {CurrentStateService} from '../../../services/current-state.service';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs/Subscription';
import {SenseContent} from './sensecontent';

@Component({
  selector: 'app-result',
  templateUrl: './result.component.html',
  styleUrls: ['./result.component.css']
})
export class ResultComponent implements OnInit, OnDestroy {
  content: SenseContent = null;
  subscription: Subscription = null;

  lemmaId: number;
  variantId: number;


  constructor(private http: HttpService,
              private currentState: CurrentStateService,
              private cd: ChangeDetectorRef,
              private route: ActivatedRoute
    ) { }

  ngOnInit() {
    this.content = null;

    this.lemmaId = +this.route.snapshot.paramMap.get('lemma_id');
    this.variantId = +this.route.snapshot.paramMap.get('variant_id');

    this.subscription = this.route.params.subscribe(params => {
      this.lemmaId = +params['lemma_id']; // (+) converts string 'id' to a number
      this.updateCurrentLexicalUnit();
    });
  }


  private updateCurrentLexicalUnit() {
    this.http.getLexicalUnitDetails(this.lemmaId).subscribe((response) => {
      this.content = new SenseContent(response);
    });
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }
}
