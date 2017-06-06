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
  pages: number[] = [];
  currentPage = 1;
  searchParam = '';

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
    this.ss.onMainEvent.emit(true);
    this.deviceService.getPagesCount().subscribe(
      (resp) => {
        const countOfElems = resp.text();
        let countOfPages = 0;
        if (countOfElems % 5 === 0) {
          countOfPages = countOfElems / 5;
        } else {
          countOfPages = countOfElems / 5 + 1;
        }
        for (let i = 1; i <= countOfPages; i++) {
          this.pages.push(i);
        }
      }
    );
    this.deviceService.getDevices(1).subscribe(
      (data) => {
        this.devices = data.json();
      }
    );
  }

  choosePage(page, event) {
    if (page < 1 || page > this.pages.length) {
      return;
    }
    this.currentPage = page;
    if (this.searchParam !== '') {
      this.deviceService.find(this.searchParam, this.currentPage).subscribe(
        (data) => {
          this.devices = data.json();
        }
      );
    } else {
      this.deviceService.getDevices(this.currentPage).subscribe(
        (data) => {
          this.devices = data.json();
        },
        (error) => {
          this.router.navigateByUrl('/login');
        }
      );
    }

  }

  turnAction(event, device, stateInput) {
    device.state = stateInput.checked ? 'on' : 'off';

    this.deviceService.updateDevice(device)
      .subscribe(
        (data) => {
          this.deviceService.send(JSON.stringify(device), '/api/notifyOwner');
        }
      );
  }

  onChange(searchParam) {
    this.deviceService.getPagesCountWithSearch(searchParam).subscribe(
      (resp) => {
        const countOfElems = resp.text();
        let countOfPages = 0;
        if (countOfElems % 5 === 0) {
          countOfPages = countOfElems / 5;
        } else {
          countOfPages = countOfElems / 5 + 1;
        }
        this.pages = [];
        for (let i = 1; i <= countOfPages; i++) {
          this.pages.push(i);
        }
      }
    );
    this.deviceService.find(searchParam, this.currentPage).subscribe(
      (data) => {
        this.devices = data.json();
      }
    );
  }
}
