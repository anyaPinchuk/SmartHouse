import {Component, OnInit} from '@angular/core';
import {FormGroup, FormBuilder, Validators} from '@angular/forms';
import {Http} from '@angular/http';
import {Router} from '@angular/router';
import {SharedService} from '../shared/shared.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;

  ngOnInit() {
  }

  constructor(public fb: FormBuilder, private router: Router, private http: Http, private ss: SharedService) {
    this.loginForm = this.fb.group({
      email: ['', Validators.required],
      password: ['', Validators.required],
    });
  }

  doLogin(event) {
    console.log(this.loginForm.value);
    this.http.post('/api/user/login', this.loginForm)
      .subscribe(
        error => console.error('could not post because', error));
  }

}
