import { Injectable } from '@angular/core';
import {Http} from '@angular/http';
import {Device} from '../device/device';

@Injectable()
export class DeviceService {
  constructor(private http: Http) {}

  getDevices(): any {
    return this.http.get('api/device/all');
  }

  updateDevice(device: Device ): any {
    return this.http.post('api/device/update', device);
  }
}
