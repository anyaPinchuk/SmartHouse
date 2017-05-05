import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {Http} from '@angular/http';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-house-add',
  templateUrl: './house-add.component.html',
  styleUrls: ['./house-add.component.css']
})
export class HouseAddComponent implements OnInit {

  houseForm: FormGroup;

  constructor(private router: Router,
              private http: Http,
              public fb: FormBuilder) {
    this.houseForm = this.fb.group({
      country: ['', Validators.required],
      city: ['', Validators.required],
      street: ['', Validators.required],
      house: ['', Validators.required],
      flat: [''],
      ownerLogin: ['', Validators.required],
      password: ['', Validators.required],
    });
  }

  doAdd(event) {
    const form = this.houseForm.getRawValue();
    this.http.post('/api/house/add', form)
      .subscribe(
        (data) => {
          this.router.navigateByUrl('/house/all');
        },
        (error) => {
          console.error('could not post because', error);
        }
      );
  }

  ngOnInit() {
  }

}
