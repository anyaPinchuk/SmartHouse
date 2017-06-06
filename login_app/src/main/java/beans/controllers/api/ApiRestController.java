package beans.controllers.api;

import beans.services.RestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/rest")
public class ApiRestController {
    private RestService restService;

    @RequestMapping(value = "/get", method = RequestMethod.GET, produces="application/json")
    public ResponseEntity getReportInJSON(@RequestParam String startDate, @RequestParam String endDate,
                                          @RequestHeader(value="Authorization") String token){
        return ResponseEntity.ok().body(restService.getStatistic(startDate, endDate, token));
    }

    @Autowired
    public void setRestService(RestService restService) {
        this.restService = restService;
    }
}
