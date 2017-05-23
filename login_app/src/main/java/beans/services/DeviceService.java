package beans.services;

import beans.converters.DeviceConverter;
import dto.DeviceDTO;
import dto.WorkLogResult;
import entities.*;
import exceptions.ServiceException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import repository.*;

import java.io.FileOutputStream;
import java.security.Principal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DeviceService {

    private ExcelService excelService;
    private DeviceRepository deviceRepository;
    private HouseRepository houseRepository;
    private WorkLogRepository workLogRepository;
    private DeviceConverter deviceConverter;
    private UserRepository userRepository;
    private RestrictionRepository restrictionRepository;

    private static final Double COST_PER_ONE_WATT = 0.26;

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
    public void setExcelService(ExcelService excelService) {
        this.excelService = excelService;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private List<DeviceDTO> iterateOverDevices(List<Device> devices, User user) {
        List<DeviceDTO> deviceDTOS = new ArrayList<>();
        devices.forEach(obj -> {
            DeviceDTO dto = toDTO(obj);
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

    private DeviceDTO toDTO(Device device) {
        return deviceConverter.toDTO(device).orElseThrow(() -> new ServiceException("device wasn't converted"));
    }

    private Device toEntity(DeviceDTO device) {
        return deviceConverter.toEntity(device).orElseThrow(() -> new ServiceException("deviceDTO wasn't converted"));
    }

    public DeviceDTO updateDevice(DeviceDTO deviceDTO) {
        Device device = toEntity(deviceDTO);
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
        workLog.setUser(user);
        workLog.setDateOfAction(new Timestamp(System.currentTimeMillis()));
        if ("off".equalsIgnoreCase(deviceDTO.getState())) {
            workLog.setConsumedEnergy(String.valueOf(countConsumedEnergy(device)));
            workLog.setCost(Double.valueOf(workLog.getConsumedEnergy()) * COST_PER_ONE_WATT + "");
            workLog.setHoursOfWork(Long.valueOf(workLog.getConsumedEnergy()) / Long.valueOf(device.getPower()) + "");
        }
        workLogRepository.save(workLog);
        return toDTO(device);
    }

    private Long countConsumedEnergy(Device device) {
        WorkLog workLog = workLogRepository.findFirstByDeviceIdAndActionOrderByDateOfActionDesc(device.getId(), "on");
        if (workLog != null) {
            long time = workLog.getDateOfAction().getTime();
            long currentTime = System.currentTimeMillis();
            long timeOfWork = currentTime - time;
            return (timeOfWork * Integer.valueOf(device.getPower())) / 3600000;
        } else return 0L;
    }

    public List<DeviceDTO> getAllByUser(String email) {
        User user = userRepository.findUserByLogin(email);
        List<Device> devices = deviceRepository.findAllBySmartHouse(user.getSmartHouse());
        return iterateOverDevices(devices, user);
    }

    public Device saveDevice(DeviceDTO deviceDTO) {
        Device device = toEntity(deviceDTO);
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
            Device device = toEntity(deviceDTO);
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

    public List<DeviceDTO> getAllByPrincipal(Principal user) {
        User userEntity = (User) ((UsernamePasswordAuthenticationToken) user).getPrincipal();
        List<Device> devices = deviceRepository.findAllBySmartHouse(userEntity.getSmartHouse());
        return iterateOverDevices(devices, userEntity);
    }

    public List<DeviceDTO> getAll(User user) {
        if (user == null) {
            user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        List<Device> devices = deviceRepository.findAllBySmartHouse(user.getSmartHouse());
        return iterateOverDevices(devices, user);
    }

    public List<DeviceDTO> getByDateInterval(Timestamp startDate, Timestamp endDate) {
        House house = getHouse();
        List<Device> devices = deviceRepository.findAllBySmartHouse(house);
        List<DeviceDTO> deviceDTOS = new ArrayList<>();
        devices.forEach(device -> {
            List<WorkLog> logs = workLogRepository.findAllByDateOfActionIsBetweenAndActionAndDevice(
                    startDate, endDate, device.getId());
            DeviceDTO dto = toDTO(device);
            logs.forEach(log -> {
                Long result = Long.valueOf(log.getConsumedEnergy());
                result += Long.valueOf(dto.getEnergy());
                dto.setEnergy(String.valueOf(result));
            });
            if (logs.size() != 0) {
                deviceDTOS.add(dto);
            }
        });
        return deviceDTOS;
    }

    public List<WorkLogResult> getWorkLogsByDevice(Timestamp start, Timestamp end) throws Exception {
        House house = getHouse();
        List<Device> devices = deviceRepository.findAllBySmartHouse(house);
        List<WorkLog> workLogs = workLogRepository.findAllByDateOfActionBetweenAndActionAndDevice(start, end, house.getId());
        List<WorkLogResult> workLogResults = new ArrayList<>();
        excelService.writeStatistic(workLogs);
        devices.forEach(device -> {
            List<WorkLog> workLogList = workLogs.stream()
                    .filter(workLog -> workLog.getDevice().getId().equals(device.getId()))
                    .collect(Collectors.toList());
            if (workLogList.size() != 0) {
                workLogResults.add(new WorkLogResult(device.getId(), workLogList, device.getPower(), device.getName()));
            }

        });
        workLogResults.forEach(workLogResult -> {
            workLogResult.getWorkLogList().forEach(workLog -> {
                workLog.setDevice(null);
                workLog.getUser().setSmartHouse(null);
            });
        });
        return workLogResults;
    }


}
