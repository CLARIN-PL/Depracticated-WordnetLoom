import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA} from '@angular/material';

export interface ModalOptions {
  maxWidth: string;
  height: string;
  width: string;
}

@Component({
  selector: 'app-graph-modal',
  templateUrl: './graph-modal.component.html',
  styleUrls: ['./graph-modal.component.css']
})
export class GraphModalComponent implements OnInit {

  selectedUnitName: string = 'unit name';

  constructor(@Inject(MAT_DIALOG_DATA) public data: ModalOptions) { }

  ngOnInit() {}

}
