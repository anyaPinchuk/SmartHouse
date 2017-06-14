package dto;

import java.io.Serializable;

public class ImageDTO implements Serializable{
    private String startDate;
    private String endDate;
    private String barChartHours;
    private String barChartEnergy;
    private String pieChartHours;
    private String pieChartEnergy;
    private String splineChart;
    private String userName;

    public ImageDTO() {
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getBarChartHours() {
        return barChartHours;
    }

    public void setBarChartHours(String barChartHours) {
        this.barChartHours = barChartHours;
    }

    public String getBarChartEnergy() {
        return barChartEnergy;
    }

    public void setBarChartEnergy(String barChartEnergy) {
        this.barChartEnergy = barChartEnergy;
    }

    public String getPieChartHours() {
        return pieChartHours;
    }

    public void setPieChartHours(String pieChartHours) {
        this.pieChartHours = pieChartHours;
    }

    public String getPieChartEnergy() {
        return pieChartEnergy;
    }

    public void setPieChartEnergy(String pieChartEnergy) {
        this.pieChartEnergy = pieChartEnergy;
    }

    public String getSplineChart() {
        return splineChart;
    }

    public void setSplineChart(String splineChart) {
        this.splineChart = splineChart;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
