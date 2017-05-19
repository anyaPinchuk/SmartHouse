import {AfterViewInit, Component, OnInit} from '@angular/core';
import {DeviceService} from '../shared/device.service';
import {Router} from '@angular/router';
import {SharedService} from '../shared/shared.service';
import {Device} from '../device/device';
import {WorkLogResult} from '../shared/work-log-result';
import {ChartService} from '../shared/chart.service';


@Component({
  selector: 'app-chart',
  templateUrl: './chart.component.html',
  styleUrls: ['./chart.component.css']
})
export class ChartComponent implements OnInit, AfterViewInit {

  startDate = '';
  endDate = '';
  workLogResults: WorkLogResult[];

  ngAfterViewInit() {
    this.buildBarCharts();
    this.buildPieCharts();
  }

  constructor(private deviceService: DeviceService,
              private ss: SharedService,
              private router: Router) {
  }

  ngOnInit() {
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
    const picker = $input.pickadate('picker');
    picker.set('select', new Date());
    const picker2 = $input2.pickadate('picker');
    picker2.set('select', new Date());
    this.ss.onMainEvent.emit(true);
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
    }
    this.deviceService.getWorkLogsByDevice(this.startDate, this.endDate).subscribe(
      (data) => {
        this.workLogResults = data.json();
        if (this.workLogResults.length !== 0) {
          const myChart = ChartService.renderBarChartEnergy();
          const myChartHours = ChartService.renderBarChartHours();
          this.workLogResults.forEach(obj => {
            let info = [0, 0, 0, 0, 0, 0, 0];
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
            myChart.addSeries({
              name: obj.deviceName,
              data: info
            });
            for (let i = 0; i < info.length; i++) {
              info[i] = Math.floor(info[i] / Number(obj.devicePower));
            }
            myChartHours.addSeries({
              name: obj.deviceName,
              data: info
            });
          });
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
    }
    this.deviceService.getDevicesByDateInterval(this.startDate, this.endDate).subscribe(
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
          ChartService.renderChartHours(infoHours);
          ChartService.renderChartPower(info);
        }
      },
      (error) => {
        this.router.navigateByUrl('/login');
      }
    );

  }
}
function notify(msg) {
  Materialize.toast(msg, 4000);
}
