package entities;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity(name = "work_log")
public class WorkLog {
    private Long id;
    private String consumedEnergy;
    private Timestamp dateOfAction;
    private String action;
    private String hoursOfWork;
    private String cost;

    private User user;
    private Device device;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_work_log", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "consumed_energy")
    public String getConsumedEnergy() {
        return consumedEnergy;
    }

    public void setConsumedEnergy(String consumedEnergy) {
        this.consumedEnergy = consumedEnergy;
    }

    @Column(name = "date_of_action", nullable = false)
    public Timestamp getDateOfAction() {
        return dateOfAction;
    }

    public void setDateOfAction(Timestamp dateOfAction) {
        this.dateOfAction = dateOfAction;
    }

    @Column(name = "action", nullable = false)
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @ManyToOne
    @JoinColumn(name = "id_device", nullable = false)
    public Device getDevice() {
        return device;
    }
    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    public User getUser() {
        return user;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    @Column(name = "hours_of_work")
    public String getHoursOfWork() {
        return hoursOfWork;
    }

    public void setHoursOfWork(String hoursOfWork) {
        this.hoursOfWork = hoursOfWork;
    }

    @Column(name = "cost")
    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
