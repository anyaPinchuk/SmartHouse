import {Component, OnInit} from '@angular/core';
import {SharedService} from './shared/shared.service';

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

  constructor(private ss: SharedService) {
  }


  ngOnInit(): void {
     this.ss.onMainEvent.subscribe(item => {
       this.isAuthorized = item.isAuthorized;
       this.isAdmin = item.isAdmin;
       this.isOwner = item.isOwner;
     });
  }
}
