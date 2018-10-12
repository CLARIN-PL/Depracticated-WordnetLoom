import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {Router} from '@angular/router';

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

  constructor(private router: Router) { }

  ngOnInit() {
  }

  onSearch(form) {
    console.log(form);
    if (form.lemma.length > 0) {
      this.router.navigate(['detail', 'search', form.lemma]);
    }
  }

  showKeyboardSelectorClicked(event, val) {
    console.log(event, val);
  }

  showAdvancedOptionsChange(event, val) {
    // console.log(event, val);
    this.panelToggle();
  }

  panelToggle() {
    this.el.toggle();
    this.showAdvancedOptions = this.el._expanded;
    console.log(this.el);
  }

}
