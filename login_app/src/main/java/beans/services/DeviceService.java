package beans.services;

import beans.converters.DeviceConverter;
import dto.DeviceDTO;
import dto.WorkLogResult;
import entities.*;
import entities.Device;
import entities.solr.SolrDevice;
import exceptions.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import repository.*;
import repository.DeviceRepository;
import solr.SolrDeviceRepository;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DeviceService {

    private ReportService excelService;
    private DeviceRepository deviceRepository;
    private HouseRepository houseRepository;
    private WorkLogRepository workLogRepository;
    private DeviceConverter deviceConverter;
    private UserRepository userRepository;
    private RestrictionRepository restrictionRepository;
    private SolrDeviceRepository solrDeviceRepository;

    private static final Double COST_PER_ONE_WATT = 0.26;

    @Autowired
    public void setWorkLogRepository(WorkLogRepository workLogRepository) {
        this.workLogRepository = workLogRepository;
    }

    @Autowired
    public void setSolrDeviceRepository(SolrDeviceRepository solrDeviceRepository) {
        this.solrDeviceRepository = solrDeviceRepository;
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
    public void setExcelService(ReportService excelService) {
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

    private DeviceDTO toDTO(SolrDevice device) {
        return deviceConverter.toDTO(device).orElseThrow(() -> new ServiceException("solr device wasn't converted"));
    }

    private Device toEntity(DeviceDTO device) {
        return deviceConverter.toEntity(device).orElseThrow(() -> new ServiceException("deviceDTO wasn't converted"));
    }

    public DeviceDTO updateDevice(DeviceDTO deviceDTO) {
        Device device = toEntity(deviceDTO);
        device = deviceRepository.findOne(device.getId());
        device.setState(deviceDTO.getState());
        deviceRepository.save(device);
        solrDeviceRepository.save(new SolrDevice(String.valueOf(device.getId()), device.getName(),
                device.getModel(), device.getState(), device.getPower(), String.valueOf(device.getSmartHouse().getId())));
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
            workLog.setCost(String.format("%.3f", Double.valueOf(workLog.getConsumedEnergy()) * COST_PER_ONE_WATT));
            workLog.setHoursOfWork(Long.toString(Long.valueOf(workLog.getConsumedEnergy()) / Long.valueOf(device.getPower())));
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
        deviceRepository.save(device);
        if (device.getId() != null) {
            solrDeviceRepository.save(new SolrDevice(String.valueOf(device.getId()), device.getName(),
                    device.getModel(), device.getState(), device.getPower(), String.valueOf(device.getSmartHouse().getId())));
        }
        return device;
    }

    public Long getPagesCount() {
        return solrDeviceRepository.countByHouseId(getHouse().getId().toString());
    }

    public int getPagesCountWithSearch(String searchParam) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<SolrDevice> solrDevices = solrDeviceRepository.find(searchParam, String.valueOf(user.getSmartHouse().getId()));
        return solrDevices.size();
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

//    public List<DeviceDTO> getAll() {
//        User userFromClient = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        List<Device> devices = deviceRepository.findAllBySmartHouse(userFromClient.getSmartHouse());
//        return iterateOverDevices(devices, userFromClient);
//    }

    public List<DeviceDTO> getByDateInterval(Timestamp startDate, Timestamp endDate, String email) {
        House house = getHouse();
        User user = userRepository.findUserByLogin(email);
        List<Device> devices = deviceRepository.findAllBySmartHouse(house);
        List<DeviceDTO> deviceDTOS = new ArrayList<>();
        devices.forEach(device -> {
            List<WorkLog> logs;
            if (user == null)
                logs = workLogRepository.findAllByDateOfActionIsBetweenAndDevice(startDate, endDate, device.getId());
            else
                logs = workLogRepository.findAllByDateOfActionIsBetweenAndDeviceAndUserId(startDate, endDate, device.getId(), user.getId());
            DeviceDTO dto = toDTO(device);
            logs.forEach(log -> {
                Long result = Long.valueOf(log.getConsumedEnergy());
                result += Long.valueOf(dto.getEnergy());
                dto.setEnergy(String.valueOf(result));
            });
            if (!logs.isEmpty()) {
                deviceDTOS.add(dto);
            }
        });
        return deviceDTOS;
    }

    public List<WorkLogResult> getWorkLogsByDevice(Timestamp start, Timestamp end, String email) {
        House house = getHouse();
        User user = userRepository.findUserByLogin(email);
        List<Device> devices = deviceRepository.findAllBySmartHouse(house);
        List<WorkLog> workLogs;
        if (user == null)
            workLogs = workLogRepository.findAllByDateOfActionBetweenAndDevice(start, end, house.getId());
        else
            workLogs = workLogRepository.findAllByDateOfActionBetweenAndDeviceAndUserId(start, end, house.getId(), user.getId());
        List<WorkLogResult> workLogResults = new ArrayList<>();
        excelService.writeStatistics(workLogs);
        devices.forEach(device -> {
            List<WorkLog> workLogList = workLogs.stream()
                    .filter(workLog -> workLog.getDevice().getId().equals(device.getId()))
                    .collect(Collectors.toList());
            if (!workLogList.isEmpty()) {
                workLogResults.add(new WorkLogResult(device.getId(), workLogList, device.getPower(), device.getName()));
            }

        });
        workLogResults.forEach(workLogResult ->
                workLogResult.getWorkLogList().forEach(workLog -> {
                    workLog.setDevice(null);
                    workLog.getUser().setSmartHouse(null);
                }));
        return workLogResults;
    }


    public List<WorkLog> getUserWorkLogs(Timestamp start, Timestamp end) {
        House house = getHouse();
        List<WorkLog> workLogs = workLogRepository.findAllByDateOfActionBetween(start, end, house.getId());
        workLogs.forEach(workLog -> {
            workLog.setDevice(null);
            workLog.getUser().setSmartHouse(null);
        });
        return workLogs;
    }

    public List<DeviceDTO> findDevices(String searchParam) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<SolrDevice> solrDevices = solrDeviceRepository.find(searchParam, String.valueOf(user.getSmartHouse().getId()),
                new PageRequest(0, 5));
        List<DeviceDTO> deviceDTOS = new ArrayList<>();
        if (solrDevices != null) {
            solrDevices.forEach(solrDevice ->
                    deviceDTOS.add(toDTO(solrDevice)));
        }
        return deviceDTOS;
    }

    public List<DeviceDTO> getByPage(int page, int size) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<SolrDevice> solrDevices = solrDeviceRepository.findByHouseId(String.valueOf(user.getSmartHouse().getId()),
                new PageRequest(page, size));
        List<DeviceDTO> deviceDTOS = new ArrayList<>();
        if (solrDevices != null) {
            solrDevices.forEach(solrDevice ->
                    deviceDTOS.add(toDTO(solrDevice)));
        }
        return deviceDTOS;
    }

}
