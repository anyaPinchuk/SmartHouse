package beans.services;

import dto.ImageDTO;
import entities.WorkLog;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class ExcelService {

    public String writeCharts(ImageDTO imageDTO) throws Exception {
        String path = "D:\\report.xlsx";
        try {
            List<byte[]> bytes = new ArrayList<>();
            bytes.add(Base64.getDecoder().decode(imageDTO.getBarChartEnergy().substring(22).getBytes()));
            bytes.add(Base64.getDecoder().decode(imageDTO.getPieChartEnergy().substring(22).getBytes()));
            bytes.add(Base64.getDecoder().decode(imageDTO.getBarChartHours().substring(22).getBytes()));
            bytes.add(Base64.getDecoder().decode(imageDTO.getPieChartHours().substring(22).getBytes()));
            Workbook wb = new XSSFWorkbook(new FileInputStream("D:\\statistic.xlsx"));
            Sheet sheet = wb.createSheet("Charts");
            CreationHelper helper = wb.getCreationHelper();
            Drawing drawing = sheet.createDrawingPatriarch();
            int i = 0;
            for (byte[] element : bytes) {
                int pictureIdx = wb.addPicture(element, Workbook.PICTURE_TYPE_PNG);
                ClientAnchor anchor = helper.createClientAnchor();
                switch (i) {
                    case 0: {
                        anchor.setCol1(1);
                        anchor.setRow1(2);
                        anchor.setCol2(2);
                        anchor.setRow2(3);
                        break;
                    }
                    case 1: {
                        anchor.setCol1(12);
                        anchor.setRow1(2);
                        anchor.setCol2(13);
                        anchor.setRow2(3);
                        break;
                    }
                    case 2: {
                        anchor.setCol1(1);
                        anchor.setRow1(30);
                        anchor.setCol2(2);
                        anchor.setRow2(31);
                        break;
                    }
                    case 3: {
                        anchor.setCol1(12);
                        anchor.setRow1(30);
                        anchor.setCol2(13);
                        anchor.setRow2(31);
                        break;
                    }
                }
                Picture pict = drawing.createPicture(anchor, pictureIdx);
                pict.resize();
                i++;
            }
            FileOutputStream fileOut = null;
            fileOut = new FileOutputStream(path);
            wb.write(fileOut);
            fileOut.close();

        } catch (Exception ioex) {
        }
        return path;
    }

    public void writeStatistic(List<WorkLog> workLogs) throws Exception {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("Statistic data");
        Row row;
        Cell cell;
        sheet.setColumnWidth(1, 20*256);
        sheet.setColumnWidth(2, 10*256);
        sheet.setColumnWidth(3, 40*256);
        sheet.setColumnWidth(4, 20*256);
        sheet.setColumnWidth(5, 20*256);
        row = sheet.createRow(0);
        cell = row.createCell(1);
        cell.setCellValue("Device");
        cell = row.createCell(2);
        cell.setCellValue("Action");
        cell = row.createCell(3);
        cell.setCellValue("User");
        cell = row.createCell(4);
        cell.setCellValue("Date");
        cell = row.createCell(5);
        cell.setCellValue("Hours of work");
        cell = row.createCell(6);
        cell.setCellValue("Cost");
        int currentRow = 1;

        for (WorkLog workLog : workLogs) {
            SimpleDateFormat format= new SimpleDateFormat("dd.MM.YYYY HH:MM");
            row = sheet.createRow(currentRow);
            cell = row.createCell(1);
            cell.setCellValue(workLog.getDevice().getName());
            cell = row.createCell(2);
            cell.setCellValue("Turned " + workLog.getAction());
            cell = row.createCell(3);
            cell.setCellValue(workLog.getUser().getLogin());
            cell = row.createCell(4);
            cell.setCellValue(format.format(workLog.getDateOfAction()));
            cell = row.createCell(5);
            if ("on".equals(workLog.getAction())) {
                cell.setCellValue("-");
                cell = row.createCell(6);
                cell.setCellValue("-");
            } else {
                cell.setCellValue(workLog.getHoursOfWork());
                cell = row.createCell(6);
                cell.setCellValue(workLog.getCost());
            }
            currentRow++;
        }

        FileOutputStream fileOut = new FileOutputStream("D:\\statistic.xlsx");
        wb.write(fileOut);
        fileOut.close();
    }
}
