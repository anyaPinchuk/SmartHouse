package entities;

import javax.persistence.*;

@Entity(name = "device")
public class Device {
    private Long id;
    private String name;
    private String model;
    private String state;
    private String power;
    private House smartHouse;

    public Device() {
    }

    public Device(Long id, String name, String model, String state, String power) {
        this.id = id;
        this.name = name;
        this.model = model;
        this.state = state;
        this.power = power;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_device", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "model")
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Column(name = "state")
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Column(name = "power", nullable = false)
    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    @ManyToOne
    @JoinColumn(name = "id_house")
    public House getSmartHouse() {
        return smartHouse;
    }

    public void setSmartHouse(House smartHouse) {
        this.smartHouse = smartHouse;
    }
}
