import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {Router} from '@angular/router';
import {isNullOrUndefined} from "util";

@Component({
  selector: 'app-input-with-keyboard',
  templateUrl: './input-with-keyboard.component.html',
  styleUrls: ['./input-with-keyboard.component.css']
})
export class InputWithKeyboardComponent implements OnInit {
  @Input() model: string;
  useKeyboard = true;
  showAdvancedOptions = false;
  @ViewChild('panel') el;
  @ViewChild('advancedFilters') advancedFilters;

  constructor(private router: Router) { }

  ngOnInit() {
  }

  onSearch(form) {
    const advancedFilters = this.advancedFilters.get();

    if (form.lemma.length > 0) {
      this.router.navigate(['detail', 'search', form.lemma]);
    }
  }

  showKeyboardSelectorClicked(event, val) {
    console.log(event, val);
  }

  toggleAdvancedOptionsChange(event, val) {
    console.log('show advanced options');
    this.panelToggle();
  }


  panelToggle(visible?: boolean) {
    if (isNullOrUndefined(visible)) {
      this.el.toggle();
    } else if (visible) {
      this.el.show();
    }
    else {
      this.el.close();
    }
    this.showAdvancedOptions = this.el._expanded;
  }

  onClickedOutsideAdvancedSearchPanel(event) {

    const classNames = event.className;

    if (!classNames) {
      return;
    }
    if ( classNames.indexOf('cdk-overlay-backdrop') > -1 ||
      classNames.indexOf('mat-option-text') > -1 ||
      classNames.indexOf('mat-option') > -1 ||
      classNames.indexOf('ignore-btn-outside-click') > -1 ) {
      return;
    }

    this.el.close();
    this.showAdvancedOptions = this.el._expanded;
  }
}
