package beans.services;

import beans.converters.DeviceConverter;
import dto.DeviceDTO;
import dto.WorkLogResult;
import entities.*;
import exceptions.ServiceException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.LocalDate;
import org.openxmlformats.schemas.drawingml.x2006.chart.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import repository.*;

import java.io.FileOutputStream;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DeviceService {

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

    public List<WorkLogResult> getWorkLogsByDevice(Timestamp start, Timestamp end) throws Exception{
        House house = getHouse();
        List<Device> devices = deviceRepository.findAllBySmartHouse(house);
        List<WorkLog> workLogs = workLogRepository.findAllByDateOfActionBetweenAndActionAndDevice(start, end, house.getId());
        List<WorkLogResult> workLogResults = new ArrayList<>();
        devices.forEach(device -> {
            List<WorkLog> workLogList = workLogs.stream()
                    .filter(workLog -> workLog.getDevice().getId().equals(device.getId()))
                    .collect(Collectors.toList());
            if (workLogList.size() != 0) {
                workLogResults.add(new WorkLogResult(device.getId(), workLogList, device.getPower(), device.getName()));
            }

        });
        workLogResults.forEach(workLogResult -> {
            workLogResult.getWorkLogList().forEach(workLog -> workLog.setDevice(null));
        });
        testExcel(workLogResults);
        return workLogResults;
    }

    public void testExcel(List<WorkLogResult> workLogResults) throws Exception {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("Sheet1");
        Row row;
        Cell cell;
        workLogResults.forEach(workLogResult -> {
            List<Long> results = new ArrayList<>();
            for (int i = 0; i < 7; i++) {
                results.add((long) i);
            }
            workLogResult.getWorkLogList().forEach(workLog -> {
                LocalDate date = new LocalDate(workLog.getDateOfAction().getTime());
                switch (date.getDayOfWeek()){
                    case 0: {
                       // results.get(0) = (results.get(0) + Long.valueOf(workLog.getConsumedEnergy()))/2;
                        break;
                    }
                    case 1: {
                       // info[1] += (info[1] + Number(worklog.consumedEnergy)) / 2;
                        break;
                    }
                    case 2: {
                        //info[2] += (info[2] + Number(worklog.consumedEnergy)) / 2;
                        break;
                    }
                    case 3: {
                        //info[3] += (info[3] + Number(worklog.consumedEnergy)) / 2;
                        break;
                    }
                    case 4: {
                        //info[4] += (info[4] + Number(worklog.consumedEnergy)) / 2;
                        break;
                    }
                    case 5: {
                        //info[5] += (info[5] + Number(worklog.consumedEnergy)) / 2;
                        break;
                    }
                    case 6: {
                       // info[6] += (info[6] + Number(worklog.consumedEnergy)) / 2;
                        break;
                    }

                }
            });
        });
        row = sheet.createRow(0);
        row.createCell(0);
        row.createCell(1).setCellValue("HEADER 1");
        row.createCell(2).setCellValue("HEADER 2");
        row.createCell(3).setCellValue("HEADER 3");

        for (int r = 1; r < 5; r++) {
            row = sheet.createRow(r);
            cell = row.createCell(0);
            cell.setCellValue("Serie " + r);
            cell = row.createCell(1);
            cell.setCellValue(new java.util.Random().nextDouble());
            cell = row.createCell(2);
            cell.setCellValue(new java.util.Random().nextDouble());
            cell = row.createCell(3);
            cell.setCellValue(new java.util.Random().nextDouble());
        }

        Drawing drawing = sheet.createDrawingPatriarch();
        ClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 0, 5, 8, 20);

        Chart chart = drawing.createChart(anchor);

        CTChart ctChart = ((XSSFChart) chart).getCTChart();
        CTPlotArea ctPlotArea = ctChart.getPlotArea();
        CTBarChart ctBarChart = ctPlotArea.addNewBarChart();
        CTBoolean ctBoolean = ctBarChart.addNewVaryColors();
        ctBoolean.setVal(true);
        ctBarChart.addNewBarDir().setVal(STBarDir.COL);

        for (int r = 2; r < 6; r++) {
            CTBarSer ctBarSer = ctBarChart.addNewSer();
            CTSerTx ctSerTx = ctBarSer.addNewTx();
            CTStrRef ctStrRef = ctSerTx.addNewStrRef();
            ctStrRef.setF("Sheet1!$A$" + r);
            ctBarSer.addNewIdx().setVal(r - 2);
            CTAxDataSource cttAxDataSource = ctBarSer.addNewCat();
            ctStrRef = cttAxDataSource.addNewStrRef();
            ctStrRef.setF("Sheet1!$B$1:$D$1");
            CTNumDataSource ctNumDataSource = ctBarSer.addNewVal();
            CTNumRef ctNumRef = ctNumDataSource.addNewNumRef();
            ctNumRef.setF("Sheet1!$B$" + r + ":$D$" + r);

            ctBarSer.addNewSpPr().addNewLn().addNewSolidFill().addNewSrgbClr().setVal(new byte[]{0, 0, 0});
        }

        //telling the BarChart that it has axes and giving them Ids
        ctBarChart.addNewAxId().setVal(123456);
        ctBarChart.addNewAxId().setVal(123457);

        //cat axis
        CTCatAx ctCatAx = ctPlotArea.addNewCatAx();
        ctCatAx.addNewAxId().setVal(123456); //id of the cat axis
        CTScaling ctScaling = ctCatAx.addNewScaling();
        ctScaling.addNewOrientation().setVal(STOrientation.MIN_MAX);
        ctCatAx.addNewDelete().setVal(false);
        ctCatAx.addNewAxPos().setVal(STAxPos.B);
        ctCatAx.addNewCrossAx().setVal(123457); //id of the val axis
        ctCatAx.addNewTickLblPos().setVal(STTickLblPos.NEXT_TO);

        //val axis
        CTValAx ctValAx = ctPlotArea.addNewValAx();
        ctValAx.addNewAxId().setVal(123457); //id of the val axis
        ctScaling = ctValAx.addNewScaling();
        ctScaling.addNewOrientation().setVal(STOrientation.MIN_MAX);
        ctValAx.addNewDelete().setVal(false);
        ctValAx.addNewAxPos().setVal(STAxPos.L);
        ctValAx.addNewCrossAx().setVal(123456); //id of the cat axis
        ctValAx.addNewTickLblPos().setVal(STTickLblPos.NEXT_TO);

        //legend
        CTLegend ctLegend = ctChart.addNewLegend();
        ctLegend.addNewLegendPos().setVal(STLegendPos.B);
        ctLegend.addNewOverlay().setVal(false);

        System.out.println(ctChart);

        FileOutputStream fileOut = new FileOutputStream("D:\\BarChart.xlsx");
        wb.write(fileOut);
        fileOut.close();
    }
}
