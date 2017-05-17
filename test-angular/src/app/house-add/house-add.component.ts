import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {Http} from '@angular/http';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {SharedService} from '../shared/shared.service';

@Component({
  selector: 'app-house-add',
  templateUrl: './house-add.component.html',
  styleUrls: ['./house-add.component.css']
})
export class HouseAddComponent implements OnInit {

  houseForm: FormGroup;

  constructor(private router: Router,
              private http: Http,
              public fb: FormBuilder,
              private ss: SharedService) {
    this.houseForm = this.fb.group({
      country: ['', Validators.required],
      city: ['', Validators.required],
      street: ['', Validators.required],
      house: ['', Validators.required],
      flat: [''],
      ownerLogin: ['', Validators.required],
    });
  }

  doAdd(event) {
    const form = this.houseForm.getRawValue();
    if (form.country.trim() !== '' && form.city.trim() !== '' && form.street.trim() !== '' && form.house.trim() !== ''
      && form.ownerLogin.trim() !== '') {
      this.http.post('/api/house/add', form)
        .subscribe(
          (data) => {
            this.router.navigateByUrl('/house/all');
          },
          (error) => {
            console.error('could not post because', error);
          }
        );
    } else {
      Materialize.toast('Please fill the required fields', 4000);
    }
  }

  ngOnInit() {
    this.ss.onMainEvent.emit(true);
  }

}
