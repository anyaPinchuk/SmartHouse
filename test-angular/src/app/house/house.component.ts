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
      (data) => {
        this.houses = data.json();
        this.ss.onMainEvent.emit(true);
      },
      (error) => {
        this.router.navigateByUrl('/login');
      });
  }

}
