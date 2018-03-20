import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {NgForm} from '@angular/forms';
import {GraphHttpService} from '../../graph/http-service/graph-http.service';
import {GraphVisualService} from '../../graph/visual-service/graph-visual.service';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {
  @Output() selectedLemmaEvent = new EventEmitter();
  possibleLemmas = [];
  constructor(private graphHttpService: GraphHttpService,
              private graphVisualService: GraphVisualService) { }

  ngOnInit() {
  }

  private sortPossibleLemmas(lemma, resp) {
    return resp.sort(function (a, b){
      if (a.lemma.word === lemma && b.lemma.word !== lemma) {
        return -1;
      }
      if (b.lemma.word === lemma && a.lemma.word !== lemma) {
        return 1;
      }
      if (a.lemma.word === b.lemma.word ) {
        return a.senseNumber - b.senseNumber;
      }
      if ( a.lemma.word  < b.lemma.word ) {
        return -1;
      }
      if ( a.lemma.word  > b.lemma.word  ) {
        return 1;
      }
      return 0;
    });
  }

  onSubmitLemma(f: NgForm) {
    this.graphHttpService.getSensesFromLemma(f.value.lemma).subscribe(
      (response) => {
        this.possibleLemmas = this.sortPossibleLemmas(f.value.lemma, response);
      });
  }


  selectSynset(id) {
    this.graphHttpService.getSynsetFromSenseId(id).subscribe(
      (response) => {
        this.graphVisualService.initializeFromSynsetId(response.id);
        this.selectedLemmaEvent.emit(response.id);
      }
    );
  }
}
