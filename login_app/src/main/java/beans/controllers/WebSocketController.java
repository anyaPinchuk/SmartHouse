package beans.controllers;

import beans.services.DeviceService;
import dto.DeviceDTO;
import entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import repository.UserRepository;

import java.security.Principal;
import java.util.List;

@Controller
public class WebSocketController {
    private DeviceService deviceService;
    private UserRepository userRepository;

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @Autowired
    public void setDeviceService(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @SubscribeMapping("/devices")
    public List<DeviceDTO> getDevices(SimpMessageHeaderAccessor headerAccessor) {
        return deviceService.getAllByPrincipal(headerAccessor.getUser());
    }

    @SubscribeMapping("/connect")
    public String connect(SimpMessageHeaderAccessor headerAccessor, Principal principal){
        String session = principal.getName();
        User user = (User) ((UsernamePasswordAuthenticationToken) headerAccessor.getUser()).getPrincipal();
        if (!session.equals(user.getSessionID())) {
            user.setSessionID(session);
            userRepository.save(user);
        }
        return session;
    }

    @MessageMapping("/notifyOwner")
    public void notifyOwner(DeviceDTO deviceDTO, SimpMessageHeaderAccessor headerAccessor) {
        User user = (User) ((UsernamePasswordAuthenticationToken) headerAccessor.getUser()).getPrincipal();
        User owner = userRepository.findBySmartHouseAndRole(user.getSmartHouse(), "ROLE_OWNER");
        deviceDTO.setEmail(user.getName());
        messagingTemplate.convertAndSendToUser(owner.getSessionID(), "/queue/getNotify", deviceDTO);
    }
}
