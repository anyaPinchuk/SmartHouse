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

  constructor(private router: Router, private http: Http, public fb: FormBuilder, private ss: SharedService) {
    this.deviceForm = this.fb.group({
      name: ['', Validators.required],
      power: ['', Validators.required],
      model: ['']
    });
  }

  ngOnInit() {
    // this.ss.onMainEvent.emit({
    //   isOwner: true,
    //   isAuthorized: true,
    //   isAdmin: false
    // });
  }

  doAdd(event) {
    const form = this.deviceForm.getRawValue();
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
  }

}
