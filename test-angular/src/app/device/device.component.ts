import {Component, OnInit} from '@angular/core';
import {Device} from './device';
import {DeviceService} from '../shared/device.service';
import {SharedService} from '../shared/shared.service';
import {Http} from '@angular/http';
import {User} from '../shared/user';
import {Router} from '@angular/router';
import {forEach} from "@angular/router/src/utils/collection";


@Component({
  selector: 'app-device',
  templateUrl: './device.component.html',
  styleUrls: ['./device.component.css']
})
export class DeviceComponent implements OnInit {

  devices: Device[] = [];

  constructor(private deviceService: DeviceService,
              private http: Http,
              private ss: SharedService,
              private router: Router,
              private user: User) {
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
      },
      (error) => {
        this.router.navigateByUrl('/login');
      }
    );
    this.http.get('api/user/checkRights').subscribe(
      (data) => {
        this.user = data.json();
        if (this.user.email === '') {
          this.router.navigateByUrl('/login');
        } else {
          switch (this.user.role) {
            case 'ROLE_OWNER': {
              this.ss.onMainEvent.emit({
                isOwner: true,
                isAuthorized: true,
                isAdmin: false,
                isChild: false,
                isGuest: false
              });
              break;
            }
            case 'ROLE_ADMIN': {
              this.ss.onMainEvent.emit({
                isOwner: false,
                isAuthorized: true,
                isAdmin: true,
                isChild: false,
                isGuest: false
              });
              break;
            }
            case 'ROLE_CHILD': {
              this.ss.onMainEvent.emit({
                isOwner: false,
                isAuthorized: true,
                isAdmin: false,
                isChild: true,
                isGuest: false
              });
              break;
            }
            case 'ROLE_GUEST': {
              this.ss.onMainEvent.emit({
                isOwner: false,
                isAuthorized: true,
                isAdmin: false,
                isChild: false,
                isGuest: true
              });
              break;
            }
          }
        }
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
