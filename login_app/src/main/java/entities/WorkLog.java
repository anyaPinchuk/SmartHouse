package entities;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity(name = "work_log")
public class WorkLog {
    private Long id;
    private String consumedEnergy;
    private Timestamp dateOfAction;
    private String action;

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

    public void setDevice(Device device) {
        this.device = device;
    }
}
