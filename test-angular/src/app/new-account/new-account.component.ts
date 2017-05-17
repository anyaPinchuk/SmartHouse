import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {Observable} from 'rxjs/Observable';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {Http} from '@angular/http';

@Component({
  selector: 'app-new-account',
  templateUrl: './new-account.component.html',
  styleUrls: ['./new-account.component.css']
})
export class NewAccountComponent implements OnInit {
  userForm: FormGroup;
  errorMsg = '';

  constructor(private route: ActivatedRoute,
              public fb: FormBuilder,
              private http: Http,
              private router: Router) {
    this.userForm = this.fb.group({
      email: [''],
      password: ['', Validators.required],
      confirmPassword: ['', Validators.required],
    });
    this.route.queryParams
      .subscribe((queryParams: Observable<Params>) => {
        const email = queryParams['email'];
        this.errorMsg = queryParams['error'];
        if (email) {
          (<FormControl>this.userForm.controls['email']).setValue(email);
        }
      });
  }

  ngOnInit() {
  }

  doCreate(event) {
    const form = this.userForm.getRawValue();
    if ((form.password === form.confirmPassword) && (form.password.trim() !== '' && form.confirmPassword.trim() !== '')) {
      this.http.post('/api/user/new', form)
        .subscribe(
          (data) => {
            if (data.json().email === '') {
              this.errorMsg = 'User with this login already exists';
            } else {
              this.router.navigateByUrl('/login?email=' + data.json().email);
            }
          },
          error => console.error('could not post because', error));
    } else {
      this.errorMsg = 'Password fields can be equal!';
    }
  }
}
