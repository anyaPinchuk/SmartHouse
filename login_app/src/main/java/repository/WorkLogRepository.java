package repository;

import dto.WorkLogResult;
import entities.WorkLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

public interface WorkLogRepository extends JpaRepository<WorkLog, Long> {
    WorkLog findFirstByDeviceIdAndActionOrderByDateOfActionDesc(Long deviceId, String action);

    @Query(value = "SELECT * FROM work_log WHERE work_log.date_of_action >= ?1 AND work_log.date_of_action <= ?2" +
            " AND action = 'off' AND work_log.id_device = ?3",
            nativeQuery = true)
    List<WorkLog> findAllByDateOfActionIsBetweenAndActionAndDevice(Timestamp start, Timestamp end, Long deviceId);

    @Query(value = "SELECT id_work_log, date_of_action, action, sum(consumed_energy) AS consumed_energy, work_log.id_device " +
            "FROM work_log INNER JOIN device ON device.id_device = work_log.id_device " +
            "WHERE work_log.date_of_action >= ?1 AND work_log.date_of_action <= ?2 " +
            "AND action = 'off' and device.id_house = ?3 " +
            "GROUP BY day(date_of_action) , month(date_of_action), year(date_of_action), id_device", nativeQuery = true)
    List<WorkLog> findAllByDateOfActionBetweenAndActionAndDevice(Timestamp start, Timestamp end, Long houseId);
}
