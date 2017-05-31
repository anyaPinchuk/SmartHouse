package beans.controllers;

import beans.services.DeviceService;
import beans.services.ReportService;
import beans.services.UserService;
import dto.DeviceDTO;
import dto.ImageDTO;
import dto.WorkLogResult;
import entities.Device;
import entities.User;
import entities.WorkLog;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
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
    private Logger LOG = LoggerFactory.getLogger(DeviceRESTController.class);

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
    public ResponseEntity<?> getAll() {
        LOG.info("handle get request by url /api/device/all");
        return ResponseEntity.ok(deviceService.getAll(null));
    }

    @PostMapping("/update")
    @PreAuthorize("hasAnyRole('ROLE_OWNER', 'ROLE_CHILD', 'ROLE_ADULT', 'ROLE_GUEST')")
    @SuppressWarnings("unchecked")
    public ResponseEntity<?> update(@RequestBody DeviceDTO deviceDTO, BindingResult bindingResult) {
        LOG.info("handle post request by url /api/device/update");
        if (!bindingResult.hasErrors()) {
            DeviceDTO device = deviceService.updateDevice(deviceDTO);
            messagingTemplate.convertAndSend("/topic/devices", deviceService.getAll(null));
            return ResponseEntity.ok(device);
        } else {
            LOG.info("bad request {}", bindingResult.getAllErrors());
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
    }

    @GetMapping("/getByDate")
    @PreAuthorize("hasAnyRole('ROLE_OWNER', 'ROLE_CHILD', 'ROLE_ADULT', 'ROLE_GUEST')")
    @SuppressWarnings("unchecked")
    public ResponseEntity<?> getByDate(@RequestParam String startDate,
                                       @RequestParam String endDate, @RequestParam String user) throws ParseException {
        LOG.info("handle get request by url /api/device/getByDate");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Timestamp start = new Timestamp(format.parse(startDate).getTime());
        Timestamp end = new Timestamp(format.parse(endDate).getTime());
        return ResponseEntity.ok(deviceService.getByDateInterval(start, end, user));
    }

    @GetMapping("/getWorkLogs")
    @PreAuthorize("hasAnyRole('ROLE_OWNER', 'ROLE_CHILD', 'ROLE_ADULT', 'ROLE_GUEST')")
    @SuppressWarnings("unchecked")
    public ResponseEntity<?> getWorkLogs(@RequestParam String startDate,
                                         @RequestParam String endDate, @RequestParam String user) throws Exception {
        LOG.info("handle get request by url /api/device/getWorkLogs");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Timestamp start = new Timestamp(format.parse(startDate).getTime());
        Timestamp end = new Timestamp(format.parse(endDate).getTime());
        List<WorkLogResult> workLogs = deviceService.getWorkLogsByDevice(start, end, user);
        return ResponseEntity.ok(workLogs);
    }

    @GetMapping("/getUserWorkLogs")
    @PreAuthorize("hasAnyRole('ROLE_OWNER', 'ROLE_CHILD', 'ROLE_ADULT', 'ROLE_GUEST')")
    @SuppressWarnings("unchecked")
    public ResponseEntity<?> getAllUserWorkLogs(@RequestParam String startDate,
                                         @RequestParam String endDate) throws Exception {
        LOG.info("handle get request by url /api/device/getUserWorkLogs");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Timestamp start = new Timestamp(format.parse(startDate).getTime());
        Timestamp end = new Timestamp(format.parse(endDate).getTime());
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
            messagingTemplate.convertAndSendToUser(user.getSessionID(), "/queue/updateDevices", deviceService.getAll(user));
            return ResponseEntity.ok().build();
        } else {
            LOG.info("bad request {}", bindingResult.getAllErrors());
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
            LOG.info("bad request {}", bindingResult.getAllErrors());
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
    }

    @GetMapping("/find")
    @PreAuthorize("hasAnyRole('ROLE_OWNER', 'ROLE_CHILD', 'ROLE_ADULT', 'ROLE_GUEST')")
    public ResponseEntity<?> findDevices(@RequestParam String param) {
        LOG.info("handle get request by url /api/device/find");
        if (!"".equals(param)) {
            return ResponseEntity.ok(deviceService.findDevices(param));
        } else return ResponseEntity.ok(deviceService.getAll(null));
    }

}
