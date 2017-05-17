package repository;

import entities.Device;
import entities.WorkLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

public interface WorkLogRepository extends JpaRepository<WorkLog, Long> {
    WorkLog findFirstByDeviceIdAndActionOrderByDateOfActionDesc(Long deviceId, String action);

    @Query( value = "select * from work_log where work_log.date_of_action >= ?1 and work_log.date_of_action <= ?2 and action = ?3 and work_log.id_device = ?4",
            nativeQuery = true)
    List<WorkLog> findAllByDateOfActionIsBetweenAndActionAndDevice(Timestamp start, Timestamp end, String action, Long deviceId);
}
