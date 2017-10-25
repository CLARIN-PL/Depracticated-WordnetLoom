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
  content: SenseContent[];
  subscription: Subscription = null;

  lemmaId: number;
  yiddishContentPresent = false;

  constructor(private http: HttpService,
              private currentState: CurrentStateService,
              private cd: ChangeDetectorRef,
              private route: ActivatedRoute
    ) { }

  ngOnInit() {
    this.content = [];

    this.lemmaId = +this.route.snapshot.paramMap.get('lemma_id');

    this.subscription = this.route.params.subscribe(params => {
      this.lemmaId = +params['lemma_id']; // (+) converts string 'id' to a number
      this.updateCurrentLexicalUnit();
    });
  }


  private updateCurrentLexicalUnit() {
    this.content = [];
    this.http.getLexicalUnitDetails(this.lemmaId).subscribe((response) => {
      if (response['Yiddish'].length > 0) {
        for(let i = 0; i < response['Yiddish'].length; i++) {
          this.yiddishContentPresent = true;
          this.content.push(new SenseContent(response, i));
        }
      } else {
        this.yiddishContentPresent = false;
        this.content.push(new SenseContent(response));
      }
    });
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }
}
