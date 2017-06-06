package beans.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import entities.User;
import entities.WorkLog;
import exceptions.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.WorkLogRepository;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class RestService {
    private UserService userService;
    private WorkLogRepository workLogRepository;

    public String getStatistic(String startDate, String endDate, String token) {
        User user = userService.loadUserByToken(token);
        if (user == null) {
            throw new ServiceException("Wrong token");
        }
        Timestamp start, end;
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        try {
            start = new Timestamp(format.parse(startDate).getTime());
            end = new Timestamp(format.parse(endDate).getTime());
        } catch (ParseException e) {
            throw new ServiceException("Wrong date format");
        }
        List<WorkLog> workLogs = workLogRepository.findAllByDateOfActionBetweenAndDevice(start, end, user.getSmartHouse().getId());
        if (workLogs.isEmpty()) return "";
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode arrayNode = mapper.createArrayNode();
        for (WorkLog workLog : workLogs) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.YYYY HH:MM");
            ObjectNode objectNode = mapper.createObjectNode();
            objectNode.put("device", workLog.getDevice().getName());
            objectNode.put("action", "turned " + workLog.getAction());
            objectNode.put("user", workLog.getUser().getName());
            objectNode.put("dateOfAction", formatter.format(workLog.getDateOfAction()));
            objectNode.put("consumedEnergy", workLog.getConsumedEnergy());
            if ("on".equals(workLog.getAction())) {
                objectNode.put("cost", "-");
            } else {
                objectNode.put("cost", workLog.getHoursOfWork());
            }
            arrayNode.add(objectNode);
        }
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(arrayNode);
        } catch (JsonProcessingException e) {
            throw new ServiceException("create json failed");
        }
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setWorkLogRepository(WorkLogRepository workLogRepository) {
        this.workLogRepository = workLogRepository;
    }
}
