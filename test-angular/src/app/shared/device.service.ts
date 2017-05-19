import {Injectable} from '@angular/core';
import {Http} from '@angular/http';
import {Device} from '../device/device';
import {SharedService} from '../shared/shared.service';
import {User} from './user';
declare const SockJS;
declare const Stomp;

@Injectable()
export class DeviceService {
  private url = 'http://localhost:8080/';
  public stompClient: any;
  private sendUrl = '/api/devices';
  isConnected = false;

  constructor(private http: Http,
              private user: User,
              private ss: SharedService) {
  }

  getDevices(): any {
    return this.http.get('api/device/all');
  }

  updateDevice(device: Device): any {
    return this.http.post('api/device/update', device);
  }

  getUsers(): any {
    return this.http.get('api/user/all');
  }

  getDevicesByUser(user): any {
    return this.http.get('api/device/get?email=' + user.email);
  }

  saveDevices(devices: Device[]) {
    return this.http.post('api/device/saveAll', devices);
  }

  connect() {
    const that = this;
    const socket = new SockJS(this.url);
    this.stompClient = Stomp.over(socket);
    this.stompClient.connect({}, function (frame) {
      that.isConnected = true;
      that.stompClient.subscribe('/api/connect');
      that.stompClient.subscribe('/user/queue/updateDevices', function (devices) {
        that.ss.onMainEvent.emit(JSON.parse(devices.body));
      });
      that.stompClient.subscribe('/topic/devices', function (devices) {
        that.ss.onMainEvent.emit(JSON.parse(devices.body));
      });
      that.stompClient.subscribe('/user/queue/getNotify', function (msg) {
        notify(JSON.parse(msg.body));
      });
    }, function (err) {
      console.log('err', err);
    });

  }


  onConnect() {
    this.stompClient.subscribe('/api/connect', function (id) {
      this.user.sessionID = id.body;
    });
  }

  send(msg, url): any {
    this.stompClient.send(url, {}, msg);
    return this.stompClient;
  }

  getDevicesByDateInterval(start, end): any {
    return this.http.get('/api/device/getByDate?startDate=' + start + '&endDate=' + end);
  }

  getWorkLogsByDevice(startDate: string, endDate: string) {
    return this.http.get('/api/device/getWorkLogs?startDate=' + startDate + '&endDate=' + endDate );
  }
}


function notify(device) {
  const email = device.email;
  Materialize.toast('User ' + email + ' turned ' + device.state + ' ' + device.name, 4000);
}
