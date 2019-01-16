import {Component, Input} from '@angular/core';

@Component({
  selector: 'app-relations-view',
  templateUrl: './relations-view.component.html',
  styleUrls: ['./relations-view.component.scss']
})
export class RelationsViewComponent {
  @Input() relations;
}
