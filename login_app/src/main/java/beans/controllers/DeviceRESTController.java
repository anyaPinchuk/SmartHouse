package beans.controllers;

import beans.services.DeviceService;
import beans.services.UserService;
import dto.DeviceDTO;
import dto.UserDTO;
import entities.Device;
import entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
    @PreAuthorize("hasAnyRole({'ROLE_OWNER', 'ROLE_CHILD', 'ROLE_ADULT', 'ROLE_GUEST'})")
    @SuppressWarnings("unchecked")
    public ResponseEntity<?> getAll() {
        LOG.info("handle post request by url /api/device/all");
        return ResponseEntity.ok(deviceService.getAll());
    }

    @PostMapping("/update")
    @PreAuthorize("hasAnyRole({'ROLE_OWNER', 'ROLE_CHILD', 'ROLE_ADULT', 'ROLE_GUEST'})")
    @SuppressWarnings("unchecked")
    public ResponseEntity<?> update(@RequestBody DeviceDTO deviceDTO, BindingResult bindingResult) {
        LOG.info("handle post request by url /api/device/update");
        if (!bindingResult.hasErrors()) {
            DeviceDTO device = deviceService.updateDevice(deviceDTO);
            messagingTemplate.convertAndSend("/topic/devices", deviceService.getAll());
            return ResponseEntity.ok(device);
        } else {
            LOG.info("bad request {}", bindingResult.getAllErrors());
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
    }


    @GetMapping("/get")
    public ResponseEntity<?> getByUser(@RequestParam String email) {
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
            messagingTemplate.convertAndSendToUser(user.getSessionID(),"/queue/updateDevices", deviceService.getAll(user));
            return ResponseEntity.ok().build();
        } else {
            LOG.info("bad request {}", bindingResult.getAllErrors());
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_OWNER')")
    @SuppressWarnings("unchecked")
    public ResponseEntity<?> add(@RequestBody DeviceDTO deviceDTO, BindingResult bindingResult) {
        LOG.info("handle post request by url /api/device/add");
        if (!bindingResult.hasErrors()) {
            Device device = deviceService.saveDevice(deviceDTO);
            return ResponseEntity.ok(device);
        } else {
            LOG.info("bad request {}", bindingResult.getAllErrors());
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
    }
}
