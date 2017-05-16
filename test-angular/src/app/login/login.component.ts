import {Component, OnInit} from '@angular/core';
import {FormGroup, FormBuilder, Validators, FormControl} from '@angular/forms';
import {Http} from '@angular/http';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {SharedService} from '../shared/shared.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  errorMsg = '';

  ngOnInit() {
  }

  constructor(public fb: FormBuilder,
              private route: ActivatedRoute,
              private router: Router,
              private http: Http,
              private ss: SharedService) {
    this.loginForm = this.fb.group({
      email: ['', Validators.required],
      password: ['', Validators.required],
    });
    this.route.queryParams
      .subscribe((queryParams: Params) => {
        if (queryParams['error'] === 'wrong_credentials') {
          this.errorMsg = 'User with such login and password does not exist';
        }
        const email = queryParams['email'];
        if (email) {
          (<FormControl>this.loginForm.controls['email']).setValue(email);
        }
      });
  }

  doLogin(event) {
    console.log(this.loginForm.value);
    this.ss.onMainEvent.emit(true);
    this.http.post('/api/user/login', this.loginForm)
      .subscribe(
        error => console.error('could not post because', error));
  }

}
