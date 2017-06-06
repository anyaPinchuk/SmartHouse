import {Component, OnInit} from '@angular/core';
import {FormGroup, FormBuilder, Validators} from '@angular/forms';
import {Http} from '@angular/http';
import {Router} from '@angular/router';
import {AppComponent} from '../app.component';
import {SharedService} from '../shared/shared.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  regForm: FormGroup;

  ngOnInit() {
    this.ss.onMainEvent.emit(true);
  }

  constructor(public fb: FormBuilder,
              private http: Http,
              private router: Router,
              private ss: SharedService) {
    this.regForm = this.fb.group({
      email: ['', Validators.required],
      name: ['', Validators.required],
      password: ['', Validators.required],
      role: ['ROLE_ADULT']
    });
  }

  doReg(event) {
    const form = this.regForm.getRawValue();
    if (form.role === '') {
      notify('Please, choose the user role');
      return;
    } else if (form.email === '') {
      notify('Field email can not be empty');
      return;
    } else if (form.name === '') {
      notify('Field name can not be empty');
      return;
    } else if (form.password === '') {
      notify('Field password can not be empty');
      return;
    }
    this.http.post('/api/user/reg', form)
      .subscribe(
        (data) => {
          if (data.text() === 'user exists') {
            notify('User with this login already exists');
          } else {
            this.router.navigateByUrl('/device/all');
          }
        },
        error => console.error('could not post because', error),
      );
  }
}
function notify(msg) {
  Materialize.toast(msg, 4000);
}
