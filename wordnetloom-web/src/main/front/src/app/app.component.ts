import {Component, OnInit } from '@angular/core';
import {ActivatedRoute, ParamMap, Router} from '@angular/router';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  showSearchInHeader = true;

  constructor(private route: ActivatedRoute) {}

  ngOnInit() {
    // this.route.params.subscribe(params => {
    //   console.log(params);
    //
    // });
    // this.route.data
    //   .subscribe((data) => {
    //     console.log(data);
    //   });

    // this.route.paramMap
    //   .switchMap((params: ParamMap) => {
    //
    //     console.log(params);
    //   });
  }
    // this.route.params.subscribe(params => {
    //   console.log(params);
    //   if (params['lemma_id']) {
    //     this.showSearchInHeader = true;
    //   } else {
    //     this.showSearchInHeader = false;
    //   }
    // });

}

