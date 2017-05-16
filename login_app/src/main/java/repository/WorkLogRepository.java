package repository;

import entities.Device;
import entities.WorkLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface WorkLogRepository  extends JpaRepository<WorkLog, Long> {
    WorkLog findFirstByDeviceIdAndActionOrderByDateOfActionDesc(Long deviceId, String action);

    List<WorkLog> findAllByDateOfActionIsBetweenAndActionAndDevice(Timestamp start, Timestamp end, String action, Device device);
}
