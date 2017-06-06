import {Component, OnInit} from '@angular/core';
import {SharedService} from './shared/shared.service';
import {User} from './shared/user';
import {Http} from '@angular/http';
import {DeviceService} from './shared/device.service';
import {isBoolean} from 'util';
import {TranslateService} from 'ng2-translate';
import {LocaleService} from './shared/locale.service';
import {Router} from '@angular/router';

declare const gapi;

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
  currentLang = '';

  constructor(private ss: SharedService,
              private http: Http,
              private user: User,
              private  deviceService: DeviceService,
              private translate: TranslateService,
              private localeService: LocaleService,
              private router: Router) {
    this.localeService.getLang().subscribe((response) => {
      const lang = response.text().substr(1, 2);
      console.log(lang);
      translate.setDefaultLang(lang.toLowerCase());
      translate.use(lang.toLowerCase());
      this.translate.get('LANG.' + lang.toUpperCase()).subscribe(res => {
        this.currentLang = res;
      });
    });
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

  changeLocale(lang: string) {
    this.localeService.setLang(lang).subscribe();
    this.translate.use(lang);
    this.translate.get('LANG.' + lang).subscribe(res => {
      this.currentLang = res;
    });
  }

  logout() {
    const auth2 = gapi.auth2.getAuthInstance();
    auth2.signOut().then(function () {
      console.log('User signed out');
    });

    this.http.get('api/logout').subscribe(data => {
      location.href = '/login';
    });
  }
}
