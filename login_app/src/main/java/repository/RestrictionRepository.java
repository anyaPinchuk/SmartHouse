package repository;

import entities.Restriction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestrictionRepository extends JpaRepository<Restriction, Long>{
    Restriction findByDeviceIdAndAndUserId(Long deviceId, Long userId);
}
