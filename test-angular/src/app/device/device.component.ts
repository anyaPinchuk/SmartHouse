import {Component, OnInit} from '@angular/core';
import {Device} from './device';
import {DeviceService} from '../shared/device.service';
import {SharedService} from '../shared/shared.service';
import {Router} from '@angular/router';


@Component({
  selector: 'app-device',
  templateUrl: './device.component.html',
  styleUrls: ['./device.component.css']
})
export class DeviceComponent implements OnInit {

  devices: Device[] = [];

  constructor(private deviceService: DeviceService,
              private ss: SharedService,
              private router: Router,
              ) {
  }

  ngOnInit() {
    this.deviceService.getDevices().subscribe(
      (data) => {
        this.devices = data.json();
        for (let i = 0; i < this.devices.length; i++) {
          if (this.devices[i].name === 'TV') {
            this.devices[i].secured = false;
          } else if (this.devices[i].name.indexOf('Light') >= 0) {
            this.devices[i].secured = false;
          }
        }
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
          console.log(data);
        }
      );
  }
}
