import {Component, NgZone, OnInit} from '@angular/core';
import {Http} from '@angular/http';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {SharedService} from '../shared/shared.service';
declare const gapi;

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  public data;

  ngOnInit() {
    this.ss.onMainEvent.emit(true);
  }

  constructor(private route: ActivatedRoute,
              private router: Router,
              private http: Http,
              private ss: SharedService,
              private zone: NgZone) {
    this.data = {
      email: '',
      password: ''
    };
    this.route.queryParams
      .subscribe((queryParams: Params) => {
        const email = queryParams['email'];
        if (email) {
          this.data.email = email;
        }
      });
    this.zone.run(() => {
      window['onSignIn'] = (user) => zone.run(() => this.onSignIn(user));
    });
  }

  doLogin() {
    this.ss.onMainEvent.emit(true);
    if (this.data.email === '') {
      notify('Field email can not be empty');
      return;
    } else if (this.data.password === '') {
      notify('Field password can not be empty');
      return;
    }
    $.ajax({
      type: 'POST',
      url: '/api/user/login',
      data: this.data
    }).then((data, textStatus, jqXHR) => {
      if (jqXHR.getResponseHeader('errortext')) {
        notify('User with such login and password does not exist');
      } else {
        this.router.navigateByUrl('/device/all');
      }
    }, () => {
      notify('Something goes wrong');
    });
    return false;
  }

  onSignIn(googleUser) {
    const id_token = googleUser.getAuthResponse().id_token;
    this.http.post('api/auth/get', id_token)
      .subscribe(
        data => {
          if (data.text() === 'ROLE_ADMIN') {
            this.router.navigateByUrl('/house');
          } else {
            this.router.navigateByUrl('/device/all');
          }
        },
        error => notify('Something goes wrong'));
  }

}
function notify(message) {
  Materialize.toast(message, 4000);
}
