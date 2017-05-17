import {Component, OnInit} from '@angular/core';
import {SharedService} from './shared/shared.service';
import {User} from './shared/user';
import {Http} from '@angular/http';
import {DeviceService} from './shared/device.service';
import {isBoolean} from 'util';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  public errorMsg = '';
  public isAuthorized = false;
  public isOwner = false;
  public isAdmin = false;
  public isChild = false;

  constructor(private ss: SharedService,
              private http: Http,
              private user: User,
              private  deviceService: DeviceService) {
  }


  ngOnInit(): void {
    this.ss.onMainEvent.subscribe(item => {
      if (isBoolean(item)) {
        this.http.get('api/user/checkRights').subscribe(
          (data) => {
            this.user = data.json();
            if (this.user.email !== '') {
              if (!this.deviceService.isConnected) {
                this.deviceService.connect();
              }
              switch (this.user.role) {
                case 'ROLE_OWNER': {
                  this.isOwner = true;
                  this.isAuthorized = true;
                  break;
                }
                case 'ROLE_ADMIN': {
                  this.isAuthorized = true;
                  this.isAdmin = true;
                  break;
                }
                case 'ROLE_CHILD': {
                  this.isChild = true;
                  this.isAuthorized = true;
                  break;
                }
                case 'ROLE_GUEST': {
                  this.isAuthorized = true;
                  break;
                }
              }
            }
          }
        );
      }
    });
  }
}
