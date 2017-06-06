package repository;

import entities.Device;
import entities.House;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    @Cacheable(value = "defaultCache", key = "'devices.' + args[0].id", unless = "#result == null")
    List<Device> findAllBySmartHouse(House house);
}
