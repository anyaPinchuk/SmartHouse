import {AfterViewInit, Component, OnInit} from '@angular/core';
import {DeviceService} from '../shared/device.service';
import {Router} from '@angular/router';
import {SharedService} from '../shared/shared.service';
declare const Highcharts: any;

@Component({
  selector: 'app-chart',
  templateUrl: './chart.component.html',
  styleUrls: ['./chart.component.css']
})
export class ChartComponent implements OnInit, AfterViewInit {

  startDate = '';
  endDate = '';

  static renderChartPower(data) {
    const myChart = Highcharts.chart('pie1', {
      chart: {
        plotBackgroundColor: null,
        plotBorderWidth: null,
        plotShadow: false,
        type: 'pie'
      },
      title: {
        text: 'Power of devices'
      },
      tooltip: {
        pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
      },
      plotOptions: {
        pie: {
          allowPointSelect: true,
          cursor: 'pointer',
          dataLabels: {
            enabled: true,
            format: '<b>{point.name}</b>: {point.percentage:.1f} %',
            style: {
              color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
            }
          }
        }
      }
    });
    myChart.addSeries({
      name: 'Devices',
      colorByPoint: true,
      data: data
    });
  }

  static renderChartHours(data) {
    const myChart = Highcharts.chart('pie2', {
      chart: {
        plotBackgroundColor: null,
        plotBorderWidth: null,
        plotShadow: false,
        type: 'pie'
      },
      title: {
        text: 'Power of devices'
      },
      tooltip: {
        pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
      },
      plotOptions: {
        pie: {
          allowPointSelect: true,
          cursor: 'pointer',
          dataLabels: {
            enabled: true,
            format: '<b>{point.name}</b>: {point.percentage:.1f} %',
            style: {
              color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
            }
          }
        }
      }
    });
    myChart.addSeries({
      name: 'Devices',
      colorByPoint: true,
      data: data
    });
  }

  ngAfterViewInit() {
    this.deviceService.getDevices().subscribe(
      (data) => {
        const devices = data.json();
        let info = [];
        devices.forEach(obj => {
          info.push(
            {
              name: obj.name,
              y: Number(obj.power)
            });
        });
        ChartComponent.renderChartPower(info);
      },
      (error) => {
         this.router.navigateByUrl('/login');
      }
    );
  }

  constructor(private deviceService: DeviceService,
              private ss: SharedService,
              private router: Router) {
  }

  ngOnInit() {
    $('.datepicker').pickadate({
      selectMonths: true, // Creates a dropdown to control month
      selectYears: 15 // Creates a dropdown of 15 years to control year
    });
    this.ss.onMainEvent.emit(true);
  }

  buildCharts(event) {
    let year: string = $('#datepicker1').pickadate('picker').get('highlight', 'yyyy');
    let day: string = $('#datepicker1').pickadate('picker').get('highlight', 'dd');
    let month: string = $('#datepicker1').pickadate('picker').get('highlight', 'mm');
    this.startDate = year + '-' + month + '-' + day;
    year = $('#datepicker2').pickadate('picker').get('highlight', 'yyyy');
    day = $('#datepicker2').pickadate('picker').get('highlight', 'dd');
    month = $('#datepicker2').pickadate('picker').get('highlight', 'mm');
    this.endDate = year + '-' + month + '-' + day;
    console.log(this.startDate);
    console.log(this.endDate);
    this.deviceService.getDevicesByDateInterval(this.startDate, this.endDate).subscribe(
      (data) => {
        const devices = data.json();
        let info = [];
        devices.forEach(obj => {
          info.push(
            {
              name: obj.name,
              y: Number(obj.energy)
            });
        });
        ChartComponent.renderChartHours(info);
      },
      (error) => {
         this.router.navigateByUrl('/login');
      }
    );

  }

}
