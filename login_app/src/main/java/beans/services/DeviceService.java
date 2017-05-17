package beans.services;

import beans.converters.DeviceConverter;
import dto.DeviceDTO;
import entities.*;
import exceptions.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import repository.*;

import java.security.Principal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class DeviceService {

    private DeviceRepository deviceRepository;
    private HouseRepository houseRepository;
    private WorkLogRepository workLogRepository;
    private DeviceConverter deviceConverter;
    private UserRepository userRepository;
    private RestrictionRepository restrictionRepository;

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

    @Autowired
    public void setRestrictionRepository(RestrictionRepository restrictionRepository) {
        this.restrictionRepository = restrictionRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private List<DeviceDTO> iterateOverDevices(List<Device> devices, User user) {
        List<DeviceDTO> deviceDTOS = new ArrayList<>();
        devices.forEach(obj -> {
            DeviceDTO dto = deviceConverter.toDTO(obj).orElseThrow(() -> new ServiceException("device wasn't converted"));
            Restriction restriction = restrictionRepository.findByDeviceIdAndAndUserId(obj.getId(), user.getId());
            if (restriction != null) {
                dto.setStartTime(restriction.getStartTime());
                dto.setEndTime(restriction.getEndTime());
                dto.setHours(restriction.getHours());
                dto.setSecured(restriction.getSecured());
            }
            deviceDTOS.add(dto);
        });
        return deviceDTOS;
    }

    public List<DeviceDTO> getAll() {
        List<Device> devices = deviceRepository.findAllBySmartHouse(getHouse());
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return iterateOverDevices(devices, user);
    }

    public DeviceDTO updateDevice(DeviceDTO deviceDTO) {
        Device device = deviceConverter.toEntity(deviceDTO).orElseThrow(() -> new ServiceException("device wasn't converted"));
        device = deviceRepository.findOne(device.getId());
        device.setState(deviceDTO.getState());
        deviceRepository.save(device);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Restriction restriction = restrictionRepository.findByDeviceIdAndAndUserId(deviceDTO.getId(), user.getId());
        if (restriction != null) {
            restriction.setSecured(deviceDTO.getSecured());
            restrictionRepository.save(restriction);
        }
        WorkLog workLog = new WorkLog();
        workLog.setAction(device.getState());
        workLog.setConsumedEnergy("");
        workLog.setDevice(device);
        workLog.setDateOfAction(new Timestamp(System.currentTimeMillis()));
        if ("off".equalsIgnoreCase(deviceDTO.getState())) {
            workLog.setConsumedEnergy(countConsumedEnergy(device));
        }
        workLogRepository.save(workLog);
        return deviceConverter.toDTO(device).get();
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

    public List<DeviceDTO> getAllByUser(String email) {
        User user = userRepository.findUserByLogin(email);
        List<Device> devices = deviceRepository.findAllBySmartHouse(getHouse());
        return iterateOverDevices(devices, user);
    }

    public Device saveDevice(DeviceDTO deviceDTO) {
        Device device = deviceConverter.toEntity(deviceDTO).orElseThrow(() -> new ServiceException("device wasn't converted"));
        device.setState("off");
        device.setSmartHouse(getHouse());
        return deviceRepository.save(device);
    }

    private House getHouse() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return houseRepository.findHouseById(user.getSmartHouse().getId());
    }

    public void updateDevices(List<DeviceDTO> deviceDTOs) {
        String email = deviceDTOs.get(0).getEmail();
        User user = userRepository.findUserByLogin(email);
        deviceDTOs.forEach(deviceDTO -> {
            Device device = deviceConverter.toEntity(deviceDTO).orElseThrow(() -> new ServiceException("device wasn't converted"));
            Restriction fromDB = restrictionRepository.findByDeviceIdAndAndUserId(device.getId(), user.getId());
            if (fromDB != null) {
                fromDB.setStartTime(deviceDTO.getStartTime());
                fromDB.setEndTime(deviceDTO.getEndTime());
                fromDB.setSecured(deviceDTO.getSecured());
            } else
                fromDB = new Restriction(deviceDTO.getStartTime(), deviceDTO.getEndTime(),
                        deviceDTO.getHours(), deviceDTO.getSecured(), user, device);
            if (deviceDTO.getSecured()) {

                saveDevice(deviceDTO);
            }
            restrictionRepository.save(fromDB);
        });
    }

    public List<DeviceDTO> getAll(Principal user) {
        User userEntity = (User) ((UsernamePasswordAuthenticationToken) user).getPrincipal();
        List<Device> devices = deviceRepository.findAllBySmartHouse(userEntity.getSmartHouse());
        return iterateOverDevices(devices, userEntity);
    }

    public List<DeviceDTO> getAll(User user) {
        List<Device> devices = deviceRepository.findAllBySmartHouse(user.getSmartHouse());
        return iterateOverDevices(devices, user);
    }

    public List<DeviceDTO> getByDateInterval(Timestamp startDate, Timestamp endDate) {
        House house = getHouse();
        List<Device> devices = deviceRepository.findAllBySmartHouse(house);
        List<DeviceDTO> deviceDTOS = new ArrayList<>();
        devices.forEach(device -> {
            List<WorkLog> logs = workLogRepository.findAllByDateOfActionIsBetweenAndActionAndDevice(
                    startDate, endDate, "off", device.getId());
            DeviceDTO dto = deviceConverter.toDTO(device).orElseThrow(() -> new ServiceException("device wasn't converted"));
            logs.forEach(log -> {
                Long result = Long.valueOf(log.getConsumedEnergy());
                result += Long.valueOf(dto.getEnergy());
                dto.setEnergy(String.valueOf(result));
            });
            deviceDTOS.add(dto);
        });
        return deviceDTOS;
    }
}
