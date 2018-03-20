import {Component, Input, OnInit} from '@angular/core';
import {GraphHttpService} from '../../graph/http-service/graph-http.service';


@Component({
  selector: 'app-synset-detail',
  templateUrl: './synset-detail.component.html',
  styleUrls: ['./synset-detail.component.css']
})
export class SynsetDetailComponent implements OnInit {
  @Input() synsetId;
  private senseId;
  private senseObject;
  private synsetDetails;

  tabs = [{name: 'Synset', active: true, title: ''}, {name: 'Properties', active: false, title: ''}];
  senses = [];
  realations = {from: null, to: null};
  constructor(private graphHttpService: GraphHttpService) { }
  ngOnInit() {
  }

  ngOnChanges(changes: any) {
    if (changes.synsetId.currentValue) {
      this.assignSynsetSenses(this.synsetId);
      this.assignSynsetDetails(this.synsetId);
    }
  }

  showSenseDetails(sense) {
    const id = sense.id;
    this.senseObject = sense;
    this.senseId = id;
    this.realations = {from: null, to: null};
    this.graphHttpService.getSenseRelationsFrom(id).subscribe(
      (response) => {
        this.realations.from = this.prepareRelations(response);
      }
    );
    this.graphHttpService.getSenseRelationsTo(id).subscribe(
      (response) => {
        this.realations.to = this.prepareRelations(response);
      }
    );
    // this.assignSynsetDetails(id);
  }

  prepareRelations(rels) {
    let groupedRels = {};
    rels.forEach((r) => {
      groupedRels[r.relation.name] ? groupedRels[r.relation.name].push(r) : groupedRels[r.relation.name] = [r];
    });
    return groupedRels;
  }

  assignSynsetDetails(id) {
    this.graphHttpService.getSynsetAttributes(id).subscribe(
      (response) => {
        this.synsetDetails = response;
      }
    );
  }

  assignSynsetSenses(id) {
    this.graphHttpService.getSynsetSenses(id).subscribe(
      (response) => {
        this.senses = response;
        if (this.senses.length > 0) {
          this.showSenseDetails(this.senses[0]);
        }
      }
    );
  }

  selectTab(tab) {
    this.tabs.forEach((tab) => {
      tab.active = false;
    });
    tab.active = true;
  }

}
