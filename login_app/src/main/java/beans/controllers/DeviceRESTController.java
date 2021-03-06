package beans.controllers;

import beans.services.DeviceService;
import beans.services.UserService;
import dto.DeviceDTO;
import dto.WorkLogResult;
import entities.User;
import entities.WorkLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/device")
public class DeviceRESTController {
    private DeviceService deviceService;
    private UserService userService;
    private static final Logger LOG = LoggerFactory.getLogger(DeviceRESTController.class);
    private static final String ERROR_MSG_BAD_REQUEST = "bad request {}";
    private static final String DATE_FORMAT_CONSTANT = "yyyy-MM-dd";
    private static final String ERROR_MSG_PARSE_DATE = "parse date {} or {} failed";

    private SimpMessageSendingOperations messagingTemplate;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setMessagingTemplate(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Autowired
    public void setDeviceService(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ROLE_OWNER', 'ROLE_CHILD', 'ROLE_ADULT', 'ROLE_GUEST')")
    @SuppressWarnings("unchecked")
    public ResponseEntity<?> getAll(@RequestParam String startPage) {
        LOG.info("handle get request by url /api/device/all");
        try {
            int page = Integer.parseInt(startPage);
            return ResponseEntity.ok(deviceService.getByPage(page, 5));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("start position or size per page is wrong");
        }

    }

    @GetMapping("/getPagesCount")
    @PreAuthorize("hasAnyRole('ROLE_OWNER', 'ROLE_CHILD', 'ROLE_ADULT', 'ROLE_GUEST')")
    @SuppressWarnings("unchecked")
    public ResponseEntity<?> getPagesCount() {
        LOG.info("handle get request by url /api/device/all");
        return ResponseEntity.ok(deviceService.getPagesCount());
    }

    @GetMapping("/getPagesCountWithSearch")
    @PreAuthorize("hasAnyRole('ROLE_OWNER', 'ROLE_CHILD', 'ROLE_ADULT', 'ROLE_GUEST')")
    @SuppressWarnings("unchecked")
    public ResponseEntity<?> getPagesCountWithSearch(@RequestParam String searchParam) {
        LOG.info("handle get request by url /api/device/all");
        return ResponseEntity.ok(deviceService.getPagesCountWithSearch(searchParam));
    }

    @PostMapping("/update")
    @PreAuthorize("hasAnyRole('ROLE_OWNER', 'ROLE_CHILD', 'ROLE_ADULT', 'ROLE_GUEST')")
    @SuppressWarnings("unchecked")
    public ResponseEntity<?> update(@RequestBody DeviceDTO deviceDTO, BindingResult bindingResult) {
        LOG.info("handle post request by url /api/device/update");
        if (!bindingResult.hasErrors()) {
            DeviceDTO device = deviceService.updateDevice(deviceDTO);
            messagingTemplate.convertAndSend("/topic/devices", deviceService.getByPage(0, 5));
            return ResponseEntity.ok(device);
        } else {
            LOG.info(ERROR_MSG_BAD_REQUEST, bindingResult.getAllErrors());
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
    }

    @GetMapping("/getByDate")
    @PreAuthorize("hasAnyRole('ROLE_OWNER', 'ROLE_CHILD', 'ROLE_ADULT', 'ROLE_GUEST')")
    @SuppressWarnings("unchecked")
    public ResponseEntity<?> getByDate(@RequestParam String startDate,
                                       @RequestParam String endDate, @RequestParam String user) {
        LOG.info("handle get request by url /api/device/getByDate");
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_CONSTANT);
        Timestamp start = null;
        Timestamp end = null;
        try {
            start = new Timestamp(format.parse(startDate).getTime());
            end = new Timestamp(format.parse(endDate).getTime());
        } catch (ParseException e) {
            LOG.error(ERROR_MSG_PARSE_DATE, startDate, endDate);
            ResponseEntity.badRequest().body("Wrong start or end date");
        }
        return ResponseEntity.ok(deviceService.getByDateInterval(start, end, user));
    }

    @GetMapping("/getWorkLogs")
    @PreAuthorize("hasAnyRole('ROLE_OWNER', 'ROLE_CHILD', 'ROLE_ADULT', 'ROLE_GUEST')")
    @SuppressWarnings("unchecked")
    public ResponseEntity<?> getWorkLogs(@RequestParam String startDate,
                                         @RequestParam String endDate, @RequestParam String user) {
        LOG.info("handle get request by url /api/device/getWorkLogs");
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_CONSTANT);
        Timestamp start = null;
        Timestamp end = null;
        try {
            start = new Timestamp(format.parse(startDate).getTime());
            end = new Timestamp(format.parse(endDate).getTime());
        } catch (ParseException e) {
            LOG.error(ERROR_MSG_PARSE_DATE, startDate, endDate);
            ResponseEntity.badRequest().body("Wrong start or end date");
        }
        List<WorkLogResult> workLogs = deviceService.getWorkLogsByDevice(start, end, user);
        return ResponseEntity.ok(workLogs);
    }

    @GetMapping("/getUserWorkLogs")
    @PreAuthorize("hasAnyRole('ROLE_OWNER', 'ROLE_CHILD', 'ROLE_ADULT', 'ROLE_GUEST')")
    @SuppressWarnings("unchecked")
    public ResponseEntity<?> getAllUserWorkLogs(@RequestParam String startDate,
                                                @RequestParam String endDate) {
        LOG.info("handle get request by url /api/device/getUserWorkLogs");
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT_CONSTANT);
        Timestamp start = null;
        Timestamp end = null;
        try {
            start = new Timestamp(format.parse(startDate).getTime());
            end = new Timestamp(format.parse(endDate).getTime());
        } catch (ParseException e) {
            LOG.error(ERROR_MSG_PARSE_DATE, startDate, endDate);
            ResponseEntity.badRequest().body("Wrong start or end date");
        }
        List<WorkLog> workLogs = deviceService.getUserWorkLogs(start, end);
        return ResponseEntity.ok(workLogs);
    }

    @GetMapping("/get")
    @PreAuthorize("hasRole('ROLE_OWNER')")
    public ResponseEntity<?> getByUser(@RequestParam String email) {
        LOG.info("handle get request by url /api/device/get");
        if (!"".equals(email)) {
            return ResponseEntity.ok(deviceService.getAllByUser(email));
        } else return ResponseEntity.ok(new ArrayList<>());
    }

    @PostMapping("/saveAll")
    @PreAuthorize("hasRole('ROLE_OWNER')")
    @SuppressWarnings("unchecked")
    public ResponseEntity<?> saveAll(@RequestBody List<DeviceDTO> devices, BindingResult bindingResult) {
        LOG.info("handle post request by url /api/device/saveAll");
        String email = devices.get(0).getEmail();
        User user = userService.loadAccountByUsername(email);
        if (!bindingResult.hasErrors()) {
            deviceService.updateDevices(devices);
            messagingTemplate.convertAndSendToUser(user.getSessionID(), "/queue/updateDevices", deviceService.getByPage(0,5));
            return ResponseEntity.ok().build();
        } else {
            LOG.error(ERROR_MSG_BAD_REQUEST, bindingResult.getAllErrors());
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_OWNER')")
    @SuppressWarnings("unchecked")
    public ResponseEntity<?> add(@Valid @RequestBody DeviceDTO deviceDTO, BindingResult bindingResult) {
        LOG.info("handle post request by url /api/device/add");
        if (!bindingResult.hasErrors()) {
            deviceService.saveDevice(deviceDTO);
            return ResponseEntity.ok().build();
        } else {
            LOG.error(ERROR_MSG_BAD_REQUEST, bindingResult.getAllErrors());
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
    }

    @GetMapping("/find")
    @PreAuthorize("hasAnyRole('ROLE_OWNER', 'ROLE_CHILD', 'ROLE_ADULT', 'ROLE_GUEST')")
    public ResponseEntity<?> findDevices(@RequestParam String searchParam) {
        LOG.info("handle get request by url /api/device/find");
        if (!"".equals(searchParam)) {
            return ResponseEntity.ok(deviceService.findDevices(searchParam));
        } else return ResponseEntity.ok(deviceService.getByPage(0, 5));
    }

}
