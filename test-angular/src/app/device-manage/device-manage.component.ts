import {Component, OnInit} from '@angular/core';
import {Device} from '../device/device';
import {DeviceService} from '../shared/device.service';
import {SharedService} from '../shared/shared.service';
import {Router} from '@angular/router';


@Component({
  selector: 'app-device-manage',
  templateUrl: './device-manage.component.html',
  styleUrls: ['./device-manage.component.css']
})
export class DeviceManageComponent implements OnInit {
  users: Account[];
  selectedUser: Account = new Account();
  selectedDevice: Device = new Device();
  devices: Device[];
  errorMsg = '';

  constructor(private deviceService: DeviceService,
              private ss: SharedService,
              private router: Router) {
  }

  ngOnInit() {
    $(document).ready(function () {
      $('.modal').modal();
    });
    this.deviceService.getUsers().subscribe((data) => {
      this.users = data.json();
      this.selectedUser = this.users[0];
      this.ss.onMainEvent.emit(true);
    }, (error) => {
      this.router.navigateByUrl('/login');
    });
  }

  selectUser(user) {
    this.selectedUser = user;
    this.deviceService.getDevicesByUser(this.selectedUser).subscribe((data) => {
      this.devices = data.json();
      this.selectedDevice = this.devices[0];
    });
  }

  changeDevice(id) {
    this.devices.forEach(device => {
      if (id === String(device.id)) {
        this.selectedDevice = device;
      }
    });
  }

  changeInput(name, input) {
    const regexp = /([01]\d|2[0-3]):([0-5]\d)/;
    switch (name) {
      case 'startTime': {
        if (regexp.test(input.value)) {
          this.selectedDevice.startTime = input.value;
          this.errorMsg = '';
        } else {
          this.errorMsg = 'Wrong value for start time';
        }
        break;
      }
      case 'endTime': {
        if (regexp.test(input.value)) {
          this.selectedDevice.endTime = input.value;
          this.errorMsg = '';
        } else {
          this.errorMsg = 'Wrong value for end time';
        }
        break;
      }
      case 'hours': {
        if (/^\d*$/.test(input.value)) {
          this.selectedDevice.hours = input.value;
          this.errorMsg = '';
        } else {
          this.errorMsg = 'Wrong value for hours';
        }
        break;
      }
    }
    if (this.selectedDevice.startTime !== '' && this.selectedDevice.endTime !== '') {
      const startHours = this.selectedDevice.startTime.split(':')[0];
      const startMinutes = this.selectedDevice.startTime.split(':')[1];
      const endHours = this.selectedDevice.endTime.split(':')[0];
      const endMinutes = this.selectedDevice.endTime.split(':')[1];
      if (Number(startHours) > Number(endHours)) {
        this.selectedDevice.startTime = '';
        this.selectedDevice.endTime = '';
        this.errorMsg = 'Wrong time interval';
      } else if (Number(startHours) === Number(endHours)) {
        if (Number(startMinutes) >= Number(endMinutes)) {
          this.selectedDevice.startTime = '';
          this.selectedDevice.endTime = '';
          this.errorMsg = 'Wrong time interval';
        }
      }
    }

  }

  saveChanges(event) {
    this.devices[0].email = this.selectedUser.email;
    this.deviceService.saveDevices(this.devices).subscribe();
    this.errorMsg = '';
  }
}

export class Account {
  email: string;
  role: string;
  dateOfRegistration: Date;
}
