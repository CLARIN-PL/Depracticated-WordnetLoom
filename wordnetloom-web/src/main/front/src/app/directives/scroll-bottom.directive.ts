import {Directive, ElementRef, EventEmitter, HostListener, Output} from '@angular/core';

@Directive({
  selector: '[appScrollBottom]'
})
export class ScrollBottomDirective {

  constructor(private _elementRef: ElementRef) {
  }

  @Output()
  public appScrollBottom = new EventEmitter();

  @HostListener('scroll', ['$event'])
  public onScroll(event) {
    const tracker = event.target;
    const limit = tracker.scrollHeight - tracker.clientHeight;
    if (event.target.scrollTop === limit) {
      this.appScrollBottom.emit();
    }
  }
}
