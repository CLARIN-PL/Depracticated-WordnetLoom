import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-table-results',
  templateUrl: './table-results.component.html',
  styleUrls: ['./table-results.component.scss']
})
export class TableResultsComponent implements OnInit {
  @Input() content: Object[];
  constructor() { }

  ngOnInit() {
    console.log(this.content);
  }

}
