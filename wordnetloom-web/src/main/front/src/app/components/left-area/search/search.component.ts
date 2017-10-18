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
    // console.log(form);
    // const response = JSON.parse('{"paging":{"totalRecords":26},"entries":[{"id":11020,"lemma":"zamek 1 (wytw)"},{"id":11021,"lemma":"zamek 2 (wytw)"},{"id":67927,"lemma":"zamek 3 (wytw)"},{"id":63884,"lemma":"zamek 4 (wytw)"},{"id":75994,"lemma":"zamek 5 (wytw)"},{"id":22013,"lemma":"zamek 6 (wytw)"},{"id":63886,"lemma":"zamek 7 (zdarz)"},{"id":683788,"lemma":"zamek angielski 1 (wytw)"},{"id":11022,"lemma":"zamek błyskawiczny 1 (wytw)"},{"id":664904,"lemma":"zamek czterotaktowy 1 (wytw)"},{"id":701943,"lemma":"zamek do drzwi 1 (!nie ustawiony!)"},{"id":683789,"lemma":"zamek dwutaktowy 1 (wytw)"},{"id":683790,"lemma":"zamek francuski 1 (wytw)"},{"id":683791,"lemma":"zamek hiszpański 1 (wytw)"},{"id":683793,"lemma":"zamek kapiszonowy 1 (wytw)"},{"id":683187,"lemma":"zamek klinowy 1 (wytw)"},{"id":359180,"lemma":"zamek kołowy 1 (wytw)"},{"id":683794,"lemma":"zamek lontowy 1 (wytw)"},{"id":683795,"lemma":"zamek niderlandzki 1 (wytw)"},{"id":654169,"lemma":"Zamek Ogrodzieniec 1 (msc)"},{"id":683796,"lemma":"zamek półswobodny 1 (wytw)"},{"id":683797,"lemma":"zamek ryglowany 1 (wytw)"},{"id":359171,"lemma":"zamek skałkowy 1 (wytw)"},{"id":664905,"lemma":"zamek ślizgowo-obrotowy 1 (wytw)"},{"id":683799,"lemma":"zamek śrubowy 1 (wytw)"},{"id":683798,"lemma":"zamek swobodny 1 (wytw)"}]}');
    // this.onSearchSubmit.emit(response);
    this.http.getSearchOptions(form.search).subscribe(
      (response) => {
        console.log(response);
        this.onSearchSubmit.emit(response);
      }
    );
  }
}
