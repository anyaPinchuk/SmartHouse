declare const Highcharts: any;


export class ChartService {

  static renderChartPower(data) {
    const myChart = Highcharts.chart('pie1', {
      chart: {
        plotBackgroundColor: null,
        plotBorderWidth: null,
        plotShadow: false,
        type: 'pie'
      },
      exporting: {
        enabled: false
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

    return myChart;
  }

  static renderChartHours(data) {
    const myChart = Highcharts.chart('pie2', {
      chart: {
        plotBackgroundColor: null,
        plotBorderWidth: null,
        plotShadow: false,
        type: 'pie'
      },
      exporting: {
        enabled: false
      },
      title: {
        text: 'Working hours of devices'
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
    return myChart;
  }

  static renderBarChartHours() {
    return Highcharts.chart('pie3', {
      chart: {
        type: 'column'
      },
      exporting: {
        enabled: false
      },
      title: {
        text: 'Device chart by working hours'
      },
      xAxis: {
        categories: ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday']
      },
      yAxis: {
        min: 0,
        title: {
          text: 'Hours'
        },
        stackLabels: {
          enabled: true,
          style: {
            fontWeight: 'bold',
            color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
          }
        }
      },
      legend: {
        align: 'right',
        x: -30,
        verticalAlign: 'bottom',
        y: 25,
        floating: true,
        backgroundColor: (Highcharts.theme && Highcharts.theme.background2) || 'white',
        borderColor: '#CCC',
        borderWidth: 1,
        shadow: false
      },
      tooltip: {
        headerFormat: '<b>{point.x}</b><br/>',
        pointFormat: '{series.name}: {point.y}<br/>Total: {point.stackTotal}'
      },
      plotOptions: {
        column: {
          stacking: 'normal',
          dataLabels: {
            enabled: true,
            color: (Highcharts.theme && Highcharts.theme.dataLabelsColor) || 'white'
          }
        }
      },
    });
  }

  static renderBarChartEnergy() {
    return Highcharts.chart('pie4', {
      chart: {
        type: 'column'
      },
      title: {
        text: 'Device chart by consumed energy'
      },
      exporting: {
        enabled: false
      },
      xAxis: {
        categories: ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday']
      },
      yAxis: {
        min: 0,
        title: {
          text: 'Consumed energy, W'
        },
        stackLabels: {
          enabled: true,
          style: {
            fontWeight: 'bold',
            color: (Highcharts.theme && Highcharts.theme.textColor) || 'gray'
          }
        }
      },
      legend: {
        align: 'right',
        x: -30,
        verticalAlign: 'bottom',
        y: 25,
        floating: true,
        backgroundColor: (Highcharts.theme && Highcharts.theme.background2) || 'white',
        borderColor: '#CCC',
        borderWidth: 1,
        shadow: false
      },
      tooltip: {
        headerFormat: '<b>{point.x}</b><br/>',
        pointFormat: '{series.name}: {point.y}<br/>Total: {point.stackTotal}'
      },
      plotOptions: {
        column: {
          stacking: 'normal',
          dataLabels: {
            enabled: true,
            color: (Highcharts.theme && Highcharts.theme.dataLabelsColor) || 'white'
          }
        }
      },
    });
  }

  static renderSplineChart() {
     return Highcharts.chart('spline', {
      chart: {
        type: 'spline'
      },
      title: {
        text: 'Total consumption of energy'
      },
      xAxis: {
      },
      yAxis: {
        title: {
          text: 'Power'
        },
        labels: {
          formatter: function () {
            return this.value + 'W';
          }
        }
      },
      tooltip: {
        crosshairs: true,
        shared: true
      },
      plotOptions: {
        spline: {
          marker: {
            radius: 4,
            lineColor: '#666666',
            lineWidth: 1
          }
        }
      }
    });
  }

}
