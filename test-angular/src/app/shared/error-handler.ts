import {ErrorHandler} from '@angular/core';

export class CustomErrorHandler extends ErrorHandler {

  constructor() {
    super(true);
  }

  handleError(error) {
   // Materialize.toast('There was an error on server :(', 6000, 'rounded');
    super.handleError(error);
  }
}
