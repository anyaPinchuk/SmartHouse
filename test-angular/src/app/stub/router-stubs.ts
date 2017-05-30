import {Directive, Input} from '@angular/core';


@Directive({
  selector: '[routerLink]'
})
export class RouterLinkStubDirective {
  @Input() linkParams: string;

  navigatedTo: any = null;

  onClick() {
    this.navigatedTo = this.linkParams;
  }
}
