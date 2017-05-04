import { Injectable } from '@angular/core';
import {Http} from '@angular/http';

@Injectable()
export class HouseService {
  constructor(private http: Http) {}

  getHouses(): any {
    return this.http.get('api/house/all');
  }
}
