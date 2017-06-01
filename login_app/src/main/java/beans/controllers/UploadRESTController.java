package beans.controllers;

import beans.services.ReportService;
import dto.ImageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/upload")
public class UploadRESTController {
    private static final Logger LOG = LoggerFactory.getLogger(UploadRESTController.class);
    private ReportService excelService;

    @Autowired
    public void setExcelService(ReportService excelService) {
        this.excelService = excelService;
    }

    @PostMapping("/image")
    @PreAuthorize("hasAnyRole('ROLE_OWNER', 'ROLE_CHILD', 'ROLE_ADULT', 'ROLE_GUEST')")
    public ResponseEntity<?> uploadCharts(@RequestBody ImageDTO imageDTO) throws Exception {
        LOG.info("handle post request by url /api/device/image");
        excelService.writeCharts(imageDTO);
        return ResponseEntity.ok().build();
    }

    @ResponseBody
    @PreAuthorize("hasAnyRole('ROLE_OWNER', 'ROLE_CHILD', 'ROLE_ADULT', 'ROLE_GUEST')")
    @RequestMapping(method = RequestMethod.GET, value = "/report", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public byte[] getReport(HttpServletResponse response) {
        LOG.info("handle get request by url /api/device/report");
        response.setHeader("Content-Disposition", "attachment; filename=report.xlsx");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("UTF-8");
        return excelService.getReport();
    }
}
