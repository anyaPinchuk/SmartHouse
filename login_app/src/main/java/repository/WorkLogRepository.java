package repository;

import entities.WorkLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkLogRepository  extends JpaRepository<WorkLog, Long> {
    WorkLog findByDeviceIdAndActionOrderByDateOfActionDesc(Long deviceId, String action);
}
