import {Component, OnInit} from '@angular/core';
import {Device} from './device';
import {DeviceService} from '../shared/device.service';
import {SharedService} from '../shared/shared.service';
import {Router} from '@angular/router';
import {isBoolean} from 'util';


@Component({
  selector: 'app-device',
  templateUrl: './device.component.html',
  styleUrls: ['./device.component.css']
})
export class DeviceComponent implements OnInit {

  devices: Device[] = [];

  constructor(private deviceService: DeviceService,
              private ss: SharedService,
              private router: Router) {
  }

  ngOnInit() {
    this.ss.onMainEvent.subscribe(item => {
      if (!isBoolean(item)) {
        this.devices = item;
      }
    });
    this.deviceService.getDevices().subscribe(
      (data) => {
        this.devices = data.json();
        this.ss.onMainEvent.emit(true);
      },
      (error) => {
        this.router.navigateByUrl('/login');
      }
    );
  }

  turnAction(event, device, stateInput) {
    device.state = stateInput.checked ? 'on' : 'off';

    this.deviceService.updateDevice(device)
      .subscribe(
        (data) => {
            console.log(device);
            this.deviceService.send(JSON.stringify(device), '/api/notifyOwner');
        }
      );
  }
}
