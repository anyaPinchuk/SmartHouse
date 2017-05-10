import {Component, OnInit} from '@angular/core';
import {SharedService} from '../shared/shared.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Http} from '@angular/http';
import {Router} from '@angular/router';

@Component({
  selector: 'app-device-add',
  templateUrl: './device-add.component.html',
  styleUrls: ['./device-add.component.css']
})
export class DeviceAddComponent implements OnInit {

  deviceForm: FormGroup;
  errorMsg = '';

  constructor(private router: Router,
              private http: Http,
              public fb: FormBuilder,
              private ss: SharedService) {
    this.deviceForm = this.fb.group({
      name: ['', Validators.required],
      power: ['', Validators.required],
      model: ['']
    });
  }

  ngOnInit() {
    this.ss.onMainEvent.emit(true);
  }

  doAdd(event) {
    const form = this.deviceForm.getRawValue();
    if (form.name !== '' && form.power !== '') {
      this.http.post('/api/device/add', form)
        .subscribe(
          (data) => {
            this.router.navigateByUrl('/device/all');
          },
          (error) => {
            console.error('could not post because', error);
            this.errorMsg = 'The fields are incorrect';
          }
        );
    } else {
      this.errorMsg = 'Wrong name or power value!';
    }
  }

}
