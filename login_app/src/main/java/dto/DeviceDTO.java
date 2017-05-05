package dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class DeviceDTO {
    private Long id;
    @NotNull
    @Size(max = 30, min = 4)
    private String name;
    @Size(max = 20, min = 2)
    private String model;
    @Size(max = 3, min = 2)
    private String state;
    @NotNull
    @Size(max = 5)
    private String power;
    private Boolean isSecured = true;

    public DeviceDTO() {
    }

    public DeviceDTO(Long id, String name, String model, String state, String power) {
        this.id = id;
        this.name = name;
        this.model = model;
        this.state = state;
        this.power = power;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public Boolean getSecured() {
        return isSecured;
    }

    public void setSecured(Boolean secured) {
        isSecured = secured;
    }
}
