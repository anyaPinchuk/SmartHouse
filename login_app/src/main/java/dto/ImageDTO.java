package dto;

import java.io.Serializable;

public class ImageDTO implements Serializable{
    private String barChartHours;
    private String barChartEnergy;
    private String pieChartHours;
    private String pieChartEnergy;

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
}
