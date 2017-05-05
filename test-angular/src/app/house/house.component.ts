import {Component, OnInit} from '@angular/core';
import {HouseService} from '../shared/house.service';
import {House} from './house';
import {Router} from '@angular/router';
import {Http} from '@angular/http';
import {User} from '../shared/user';
import {SharedService} from '../shared/shared.service';

@Component({
  selector: 'app-house',
  templateUrl: './house.component.html',
  styleUrls: ['./house.component.css']
})
export class HouseComponent implements OnInit {

  houses: House[];

  constructor(private houseService: HouseService,
              private http: Http,
              private router: Router,
              private ss: SharedService,
              private user: User) {
  }

  ngOnInit() {
    this.houseService.getHouses().subscribe(
      (data) => this.houses = data.json(),
      (error) => {
        this.router.navigateByUrl('/login');
      });
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
            case 'ROLE_GUEST': {
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

}
