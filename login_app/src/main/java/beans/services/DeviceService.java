package beans.services;

import beans.converters.DeviceConverter;
import dto.DeviceDTO;
import entities.Device;
import entities.House;
import entities.User;
import entities.WorkLog;
import exceptions.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import repository.DeviceRepository;
import repository.HouseRepository;
import repository.WorkLogRepository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class DeviceService {

    private DeviceRepository deviceRepository;
    private HouseRepository houseRepository;
    private WorkLogRepository workLogRepository;
    private DeviceConverter deviceConverter;


    @Autowired
    public void setWorkLogRepository(WorkLogRepository workLogRepository) {
        this.workLogRepository = workLogRepository;
    }

    @Autowired
    public void setDeviceRepository(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Autowired
    public void setHouseRepository(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;
    }

    @Autowired
    public void setDeviceConverter(DeviceConverter deviceConverter) {
        this.deviceConverter = deviceConverter;
    }

    public List<DeviceDTO> getAll() {
        List<Device> devices = deviceRepository.findAllBySmartHouse(getHouse());
        List<DeviceDTO> deviceDTOS = new ArrayList<>();
        devices.forEach(obj -> {
            deviceDTOS.add(deviceConverter.toDTO(obj).orElseThrow(() ->  new ServiceException("device wasn't converted")));
        });
        return deviceDTOS;
    }

    public Device updateDevice(DeviceDTO deviceDTO) {
        Device device = deviceConverter.toEntity(deviceDTO).orElseThrow(() ->  new ServiceException("device wasn't converted"));
        device = deviceRepository.findOne(device.getId());
        device.setState(deviceDTO.getState());
        deviceRepository.save(device);
        WorkLog workLog = new WorkLog();
        workLog.setAction(device.getState());
        workLog.setConsumedEnergy("");
        workLog.setDevice(device);
        workLog.setDateOfAction(new Timestamp(System.currentTimeMillis()));
        if ("off".equalsIgnoreCase(deviceDTO.getState())) {
            workLog.setConsumedEnergy(countConsumedEnergy(device));
        }
        workLogRepository.save(workLog);
        return device;
    }

    private String countConsumedEnergy(Device device) {
        WorkLog workLog = workLogRepository.findFirstByDeviceIdAndActionOrderByDateOfActionDesc(device.getId(), "on");
        if (workLog != null) {
            long time = workLog.getDateOfAction().getTime();
            long currentTime = System.currentTimeMillis();
            long timeOfWork = currentTime - time;
            long result = (timeOfWork * Integer.valueOf(device.getPower())) / 3600000;
            return String.valueOf(result);
        } else return "0";
    }

    public Device saveDevice(DeviceDTO deviceDTO) {
        Device device = deviceConverter.toEntity(deviceDTO).orElseThrow(() ->  new ServiceException("device wasn't converted"));
        device.setState("off");
        device.setSmartHouse(getHouse());
        return deviceRepository.save(device);
    }

    private House getHouse() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return houseRepository.findHouseById(user.getSmartHouse().getId());
    }
}
