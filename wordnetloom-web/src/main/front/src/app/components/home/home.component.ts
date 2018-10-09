import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  constructor(private router: Router) { }
  useKeyboard = true;
  ngOnInit() {
  }

  onSearch(form) {
    console.log(form);
    if (form.lemma.length > 0) {
      this.router.navigate(['detail', 'search', form.lemma]);
    }

  }
}
