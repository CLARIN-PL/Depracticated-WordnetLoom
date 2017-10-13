import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import { HttpService } from '../../../services/http.service';


@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {
  @Output() onSearchSubmit = new EventEmitter<any>();
  constructor(private http: HttpService) { }

  ngOnInit() {
  }

  onSubmit(form) {
    this.http.getSearchOptions().subscribe(
      (response) => {
        this.onSearchSubmit.emit(response);
      }
    );
  }
}
