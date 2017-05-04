import {Component, Injectable, Input, Output, EventEmitter} from '@angular/core';


@Injectable()
export class SharedService {
  onMainEvent: EventEmitter<any> = new EventEmitter();

  constructor() {
    console.log('shared service started');
  }
}
