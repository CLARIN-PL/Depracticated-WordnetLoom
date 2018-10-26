import {Component, ElementRef, Input, OnInit, ViewChild, Output, EventEmitter} from '@angular/core';
import {isNullOrUndefined} from 'util';
import {SidebarService} from "../../services/sidebar.service";
import {HttpService} from "../../services/http.service";
import {FormControl, FormGroup} from "@angular/forms";
import { CurrentStateService } from '../../services/current-state.service'

@Component({
  selector: 'app-input-with-keyboard',
  templateUrl: './input-with-keyboard.component.html',
  styleUrls: ['./input-with-keyboard.component.scss']
})
export class InputWithKeyboardComponent implements OnInit {
  @Input() model: string;
  @Input('floatingSearch') floatingAdvancedSearchPanel  = true;

  @Output() advancedPanelOpen = new EventEmitter<boolean>();
  @Output() usingKeyboard = new EventEmitter<boolean>();

  @ViewChild('panel') el;
  @ViewChild('inputWithKeyboard') inputWithKeyboard: ElementRef;
  @ViewChild('advancedFilters') advancedFilters;

  useKeyboard = true;
  showAdvancedOptions = false;
  searchFormGroup = new FormGroup({
    searchTerm: new FormControl(''),
    showAdvancedOptions: new FormControl(),
    useKeyboard: new FormControl(true)
  });
  // searchTerm: FormControl = new FormControl();
  autoCompleteOptions = [];

  constructor(private state: CurrentStateService, private sidebar: SidebarService, private http: HttpService) { }

  ngOnInit() {
    this.searchFormGroup.controls.searchTerm.valueChanges.subscribe(
      term => {
        if (term !== '') {
          this.http.getSearchAutocomplete(term).subscribe(
            data => {
              this.autoCompleteOptions = data as any[];
            });
        }
      });
  }

  onSearch() {
    const searchTerm = this.searchFormGroup.value.searchTerm;
    const advancedFilters = this.advancedFilters.get();
    if (searchTerm.length > 0) {
      advancedFilters['lemma'] = searchTerm;
    }
    this.sidebar.getAllOptions(advancedFilters);
    this.state.setNavbarOpen(false); // hide navbar
    this.state.setSearchResultPanelOpen(true); // show sidebar with results
  }

  showKeyboardSelectorClicked(showKeyboard) {
    if (showKeyboard) {
      setTimeout(() => {
        this.inputWithKeyboard.nativeElement.focus();
      }, 100);
    }
    this.usingKeyboard.emit(showKeyboard);
  }

  toggleAdvancedOptionsChange(event, val) {
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
    this.advancedPanelOpen.emit(this.showAdvancedOptions);
  }

  onClickedOutsideAdvancedSearchPanel(event) {

    const classNames = event.className;

    if (!classNames) {
      return;
    }

    if (
      (typeof classNames === 'object') ||
      ( classNames.indexOf('cdk-overlay-backdrop') > -1 ||
      classNames.indexOf('mat-option-text') > -1 ||
      classNames.indexOf('mat-option') > -1 ||
      classNames.indexOf('ignore-btn-outside-click') > -1 )) {
      return;
    }

    this.el.close();
    this.showAdvancedOptions = this.el._expanded;
  }
}
