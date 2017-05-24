import {AfterViewInit, Component, OnInit} from '@angular/core';
import {DeviceService} from '../shared/device.service';
import {Router} from '@angular/router';
import {SharedService} from '../shared/shared.service';
import {WorkLogResult} from '../shared/work-log-result';
import {ChartService} from '../shared/chart.service';
import {User} from '../shared/user';

@Component({
  selector: 'app-chart',
  templateUrl: './chart.component.html',
  styleUrls: ['./chart.component.css']
})
export class ChartComponent implements OnInit, AfterViewInit {

  form = {};
  selectedUser: User = null;
  users: User[];
  startDate = '';
  endDate = '';
  workLogResults: WorkLogResult[];
  barChartEnergy: any;
  barChartHours: any;
  pieChartEnergy: any;
  pieChartHours: any;

  ngAfterViewInit() {
    this.buildBarCharts();
    this.buildPieCharts();
    this.deviceService.getAllUsers().subscribe((data) => {
      this.users = data.json();
    });
  }

  constructor(private deviceService: DeviceService,
              private ss: SharedService,
              private router: Router) {

  }

  ngOnInit() {
    this.ss.onMainEvent.emit(true);
    const that = this;
    const $input = $('#datepicker1').pickadate({
      selectMonths: true,
      selectYears: 15,
    }).on('change', function () {
      that.rebuild();
    });
    const $input2 = $('#datepicker2').pickadate({
      selectMonths: true,
      selectYears: 15,
    }).on('change', function () {
      that.rebuild();
    });
    let lastMonth = new Date();
    lastMonth.setMonth(lastMonth.getMonth() - 1);
    const picker = $input.pickadate('picker');
    picker.set('select', lastMonth);
    const picker2 = $input2.pickadate('picker');
    picker2.set('select', new Date());
  }

  rebuild() {
    this.buildPieCharts();
    this.buildBarCharts();
  }

  buildBarCharts() {
    const startEndDates = this.getDates();
    if (startEndDates.start > startEndDates.end) {
      notify('Wrong date interval');
      return;
    } else if (startEndDates.start.getMonth() === startEndDates.end.getMonth() &&
      startEndDates.start.getDay() === startEndDates.end.getDay()) {
      return;
    }
    let email = '';
    if (this.selectedUser !== null) {
      email = this.selectedUser.email;
    }
    this.deviceService.getWorkLogsByDevice(this.startDate, this.endDate, email).subscribe(
      (data) => {
        this.workLogResults = data.json();
        if (this.workLogResults.length !== 0) {
          this.barChartEnergy = ChartService.renderBarChartEnergy();
          this.barChartHours = ChartService.renderBarChartHours();
          this.workLogResults.forEach(obj => {
            let info = [0, 0, 0, 0, 0, 0, 0];
            let hoursInfo = [0, 0, 0, 0, 0, 0, 0];
            const workLogs = obj.workLogList;
            workLogs.forEach(worklog => {
              const date = new Date(worklog.dateOfAction);
              switch (date.getDay()) {
                case 0: {
                  info[0] = (info[0] + Number(worklog.consumedEnergy)) / 2;
                  break;
                }
                case 1: {
                  info[1] += (info[1] + Number(worklog.consumedEnergy)) / 2;
                  break;
                }
                case 2: {
                  info[2] += (info[2] + Number(worklog.consumedEnergy)) / 2;
                  break;
                }
                case 3: {
                  info[3] += (info[3] + Number(worklog.consumedEnergy)) / 2;
                  break;
                }
                case 4: {
                  info[4] += (info[4] + Number(worklog.consumedEnergy)) / 2;
                  break;
                }
                case 5: {
                  info[5] += (info[5] + Number(worklog.consumedEnergy)) / 2;
                  break;
                }
                case 6: {
                  info[6] += (info[6] + Number(worklog.consumedEnergy)) / 2;
                  break;
                }
              }
            });
            this.barChartEnergy.addSeries({
              name: obj.deviceName,
              data: info
            });
            for (let i = 0; i < info.length; i++) {
              hoursInfo[i] = Math.floor(info[i] / Number(obj.devicePower));
            }
            this.barChartHours.addSeries({
              name: obj.deviceName,
              data: hoursInfo
            });
          });
        } else {
          document.getElementById('pie1').innerText = ''; // works!
          this.barChartEnergy = null;
          this.barChartHours = null;
          this.pieChartEnergy = null;
          this.pieChartHours = null;
        }
      },
      (error) => {
        this.router.navigateByUrl('/login');
      }
    );
  }

  getDates() {
    let year: string = $('#datepicker1').pickadate('picker').get('highlight', 'yyyy');
    let day: string = $('#datepicker1').pickadate('picker').get('highlight', 'dd');
    let month: string = $('#datepicker1').pickadate('picker').get('highlight', 'mm');
    this.startDate = year + '-' + month + '-' + day;
    const start = new Date(Number(year), Number(month) - 1, Number(day));
    year = $('#datepicker2').pickadate('picker').get('highlight', 'yyyy');
    day = $('#datepicker2').pickadate('picker').get('highlight', 'dd');
    month = $('#datepicker2').pickadate('picker').get('highlight', 'mm');
    this.endDate = year + '-' + month + '-' + day;
    const end = new Date(Number(year), Number(month) - 1, Number(day));
    return {
      start: start, end: end
    };
  }

  buildPieCharts() {
    const startEndDates = this.getDates();
    if (startEndDates.start > startEndDates.end) {
      notify('Wrong date interval');
      return;
    } else if (startEndDates.start.getMonth() === startEndDates.end.getMonth() &&
      startEndDates.start.getDay() === startEndDates.end.getDay()) {
      return;
    }
    let email = '';
    if (this.selectedUser !== null) {
      email = this.selectedUser.email;
    }
    this.deviceService.getDevicesByDateInterval(this.startDate, this.endDate, email).subscribe(
      (data) => {
        const devices = data.json();
        const info = [];
        const infoHours = [];
        devices.forEach(obj => {
          info.push(
            {
              name: obj.name,
              y: Number(obj.energy)
            });
          infoHours.push(
            {
              name: obj.name,
              y: Math.floor(Number(obj.energy) / Number(obj.power))
            });
        });
        if (devices.length !== 0) {
          this.pieChartHours = ChartService.renderChartHours(infoHours);
          this.pieChartEnergy = ChartService.renderChartPower(info);
        }
      },
      (error) => {
        this.router.navigateByUrl('/login');
      }
    );
  }

  getReport(event) {
    const startEndDates = this.getDates();
    if (startEndDates.start > startEndDates.end) {
      notify('Wrong date interval');
      return;
    } else if (startEndDates.start.getMonth() === startEndDates.end.getMonth() &&
      startEndDates.start.getDay() === startEndDates.end.getDay()) {
      return;
    }
    const svgBarHours = this.barChartHours.getSVG({
      exporting: {
        sourceWidth: this.barChartHours.chartWidth,
        sourceHeight: this.barChartHours.chartHeight
      }
    });
    const svgPieHours = this.pieChartHours.getSVG({
      exporting: {
        sourceWidth: this.pieChartHours.chartWidth,
        sourceHeight: this.pieChartHours.chartHeight
      }
    });
    const svgBarEnergy = this.barChartEnergy.getSVG({
      exporting: {
        sourceWidth: this.barChartEnergy.chartWidth,
        sourceHeight: this.barChartEnergy.chartHeight
      }
    });
    const svgPieEnergy = this.pieChartEnergy.getSVG({
      exporting: {
        sourceWidth: this.pieChartEnergy.chartWidth,
        sourceHeight: this.pieChartEnergy.chartHeight
      }
    });
    const that = this;
    const render_width = 500;
    const render_height = render_width * this.barChartHours.chartHeight / this.barChartHours.chartWidth;
    const arr = [];
    const promises = [];
    for (let i = 0; i < 4; i++) {
      const canvas = document.createElement('canvas');
      canvas.height = render_height;
      canvas.width = render_width;
      arr.push(canvas);
    }
    const arraySVG = [svgBarHours, svgBarEnergy, svgPieEnergy, svgPieHours];
    let i = 0;
    arr.forEach(canvas => {
      promises.push(loadImage(canvas, render_width, render_height, arraySVG[i]));
      i++;
    });
    Promise.all(promises).then(function (results) {
      that.deviceService.exportImage({
        startDate: that.startDate,
        endDate: that.endDate,
        pieChartEnergy: results[3].toDataURL(),
        pieChartHours: results[2].toDataURL(),
        barChartHours: results[0].toDataURL(),
        barChartEnergy: results[1].toDataURL()
      }).subscribe(data => {
        window.open('/api/device/report', '_blank');
      });
    });
  }

  changeUser(email) {
    if (email === 'Choose user') {
      this.selectedUser = null;
    } else {
      this.users.forEach(user => {
        if (email === String(user.email)) {
          this.selectedUser = user;
        }
      });
    }
    this.rebuild();
  }
}


function loadImage(canvas, render_width, render_height, svgBarHours) {
  return new Promise(function (resolve, reject) {
    const image = new Image();
    image.src = 'data:image/svg+xml;base64,' + window.btoa(svgBarHours);
    image.onload = function () {
      canvas.getContext('2d').drawImage(this, 0, 0, render_width, render_height);
      resolve(canvas);
    };
    image.onerror = function (e) {
      reject(e);
    };
  });
}

function notify(msg) {
  Materialize.toast(msg, 4000);
}


