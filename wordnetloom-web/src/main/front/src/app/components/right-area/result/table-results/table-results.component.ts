import {Component, Input } from '@angular/core';

@Component({
  selector: 'app-table-results',
  templateUrl: './table-results.component.html',
  styleUrls: ['./table-results.component.scss']
})
export class TableResultsComponent {
  @Input() content: Object[];
}
