package entities;

import javax.persistence.*;

@Entity(name = "restriction")
public class Restriction {
    private Long id;
    private String startTime;
    private String endTime;
    private String hours;
    private Boolean secured;

    private User user;
    private Device device;

    public Restriction() {
    }

    public Restriction(String startTime, String endTime, String hours, Boolean secured, User user, Device device) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.hours = hours;
        this.secured = secured;
        this.user = user;
        this.device = device;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_restriction", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "start_time")
    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @Column(name = "end_time")
    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Column(name = "hours")
    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    @Column(name = "secured", nullable = false)
    public Boolean getSecured() {
        return secured;
    }

    public void setSecured(Boolean secured) {
        this.secured = secured;
    }

    @ManyToOne
    @JoinColumn(name = "id_user")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name = "id_device")
    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
}
