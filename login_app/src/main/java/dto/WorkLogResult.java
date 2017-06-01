package dto;

import entities.WorkLog;
import java.util.List;

public class WorkLogResult {
    private Long deviceId;
    private List<WorkLog> workLogList;
    private String devicePower;
    private String deviceName;

    public WorkLogResult(Long deviceId, List<WorkLog> workLogList, String devicePower, String deviceName) {
        this.deviceId = deviceId;
        this.workLogList = workLogList;
        this.devicePower = devicePower;
        this.deviceName = deviceName;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public List<WorkLog> getWorkLogList() {
        return workLogList;
    }

    public void setWorkLogList(List<WorkLog> workLogList) {
        this.workLogList = workLogList;
    }

    public String getDevicePower() {
        return devicePower;
    }

    public void setDevicePower(String devicePower) {
        this.devicePower = devicePower;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}
