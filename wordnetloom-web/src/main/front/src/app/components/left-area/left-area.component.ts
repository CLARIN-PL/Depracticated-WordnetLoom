import {Component, EventEmitter, OnInit, Output} from '@angular/core';

@Component({
  selector: 'app-left-area',
  templateUrl: './left-area.component.html',
  styleUrls: ['./left-area.component.css']
})
export class LeftAreaComponent implements OnInit {
  @Output() selectedOptionId = new EventEmitter<any>();

  constructor() { }

  searchOptions = [];

  ngOnInit() {
  }

  onSearched(data) {
    this.searchOptions = data.entries;
  }

}
