import {Component, OnInit} from '@angular/core';
import {SharedService} from './shared/shared.service';
import {User} from './shared/user';
import {Http} from '@angular/http';

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
              private user: User) {
  }


  ngOnInit(): void {
    // this.ss.onMainEvent.subscribe(item => {
    //   this.isAuthorized = item.isAuthorized;
    //   this.isAdmin = item.isAdmin;
    //   this.isOwner = item.isOwner;
    // });
    this.ss.onMainEvent.subscribe(item => {
      this.http.get('api/user/checkRights').subscribe(
        (data) => {
          this.user = data.json();
          console.log(this.user);
          if (this.user.email === '') {
            // this.router.navigateByUrl('/login');
          } else {
            switch (this.user.role) {
              case 'ROLE_OWNER': {
                // this.ss.onMainEvent.emit({
                //   isOwner: true,
                //   isAuthorized: true,
                //   isAdmin: false,
                //   isChild: false,
                //   isGuest: false
                // });
                this.isOwner = true;
                this.isAuthorized = true;
                break;
              }
              case 'ROLE_ADMIN': {
                // this.ss.onMainEvent.emit({
                //   isOwner: false,
                this.isAuthorized = true;
                this.isAdmin = true;
                //   isChild: false,
                //   isGuest: false
                // });
                break;
              }
              case 'ROLE_CHILD': {
                this.isChild = true;
                this.isAuthorized = true;
                // this.ss.onMainEvent.emit({
                //   isOwner: false,
                //   isAuthorized: true,
                //   isAdmin: false,
                //   isChild: true,
                //   isGuest: false
                // });
                break;
              }
              case 'ROLE_GUEST': {
                this.isAuthorized = true;
                // this.ss.onMainEvent.emit({
                //   isOwner: false,
                //   isAuthorized: true,
                //   isAdmin: false,
                //   isChild: false,
                //   isGuest: true
                // });
                break;
              }
            }
          }
        }
      );
    });
  }
}
