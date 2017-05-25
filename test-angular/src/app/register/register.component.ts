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
  errorMsg = '';

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
      this.errorMsg = 'Please, choose the user role';
      return;
    } else if (form.email === '') {
      this.errorMsg = 'Field email can not be empty';
      return;
    } else if (form.name === '') {
      this.errorMsg = 'Field name can not be empty';
      return;
    } else if (form.password === '') {
      this.errorMsg = 'Field password can not be empty';
      return;
    } else {
      this.errorMsg = '';
    }
    this.http.post('/api/user/reg', form)
      .subscribe(
        (data) => {
          if (data.text() === 'user exists') {
            this.errorMsg = 'User with this login already exists';
          } else {
            this.router.navigateByUrl('/device/all');
          }
        },
        error => console.error('could not post because', error),
      );
  }
}
