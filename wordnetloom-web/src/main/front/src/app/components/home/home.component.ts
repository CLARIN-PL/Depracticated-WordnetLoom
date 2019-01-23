import {Component, OnDestroy, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {CurrentStateService} from '../../services/current-state.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['home.component.scss']
})
export class HomeComponent implements OnInit, OnDestroy {

  advancedPanelOpened = false;
  useKeyboard = true;
  mobile = true;
  mobileListener = null;

  constructor(private state: CurrentStateService) {}

  ngOnInit() {
    this.mobile = this.state.getMobileState();
    this.mobileListener = this.state.getMobileStateEmitter().subscribe(mobileState => {
      this.mobile = mobileState;
    });
  }

  ngOnDestroy() {
    this.mobileListener.unsubscribe();
  }

  advancedPanelToggled(opened) {
    this.advancedPanelOpened = opened;
  }

  usingKeyboardToggled(usingKeyboard) {
    this.useKeyboard = usingKeyboard;
  }
}
