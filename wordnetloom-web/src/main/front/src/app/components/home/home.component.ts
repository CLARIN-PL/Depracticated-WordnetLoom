import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['home.component.scss']
})
export class HomeComponent implements OnInit {

  advancedPanelOpened = false;
  useKeyboard = true;

  constructor() { }

  ngOnInit() {
  }

  advancedPanelToggled(opened) {
    this.advancedPanelOpened = opened;
  }

  usingKeyboardToggled(usingKeyboard) {
    this.useKeyboard = usingKeyboard;
  }
}
