import {Component, Input, OnInit} from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-input-with-keyboard',
  templateUrl: './input-with-keyboard.component.html',
  styleUrls: ['./input-with-keyboard.component.css']
})
export class InputWithKeyboardComponent implements OnInit {
  @Input() model: string;
  useKeyboard = true;
  constructor(private router: Router) { }

  ngOnInit() {
  }

  onSearch(form) {
    console.log(form);
    if (form.lemma.length > 0) {
      this.router.navigate(['detail', 'search', form.lemma]);
    }
  }

  checkboxClicked(event, val) {
    console.log(event, val);
  }

}
