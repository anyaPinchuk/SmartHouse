import {Component, OnInit} from '@angular/core';
import {Device} from './device';
import {DeviceService} from '../shared/device.service';
import {SharedService} from '../shared/shared.service';
import {Http} from '@angular/http';
import {User} from '../shared/user';
import {Router} from '@angular/router';


@Component({
  selector: 'app-device',
  templateUrl: './device.component.html',
  styleUrls: ['./device.component.css']
})
export class DeviceComponent implements OnInit {

  devices: Device[] = [];

  constructor(private deviceService: DeviceService, private http: Http, private ss: SharedService,
              private router: Router,
              private user: User) {
  }

  ngOnInit() {
    this.deviceService.getDevices().subscribe(
      (data) => this.devices = data.json(),
      (error) => {
        this.router.navigateByUrl('/login');
      });
    // TODO: logic for disabled devices from child and guest
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
                isAdmin: false
              });
              break;
            }
            case 'ROLE_ADMIN': {
              this.ss.onMainEvent.emit({
                isOwner: false,
                isAuthorized: true,
                isAdmin: true
              });
              break;
            }
            case 'ROLE_CHILD': {
              this.ss.onMainEvent.emit({
                isOwner: false,
                isAuthorized: true,
                isAdmin: false
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
