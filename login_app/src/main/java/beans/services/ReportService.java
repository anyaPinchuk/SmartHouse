package beans.services;

import dto.ImageDTO;
import entities.WorkLog;
import exceptions.ServiceException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class ReportService {

    public void writeCharts(ImageDTO imageDTO) {
        String pathToReport = "D:\\report.xlsx";
        String pathToStatistic = "D:\\statistic.xlsx";
        Workbook wb;
        try (FileInputStream fileInputStream = new FileInputStream(pathToStatistic)) {
            wb = new XSSFWorkbook(fileInputStream);
        } catch (IOException e) {
            throw new ServiceException("error while creating WorkBook", e);
        }
        List<byte[]> bytes = new ArrayList<>();
        bytes.add(Base64.getDecoder().decode(imageDTO.getPieChartEnergy().substring(22).getBytes()));
        bytes.add(Base64.getDecoder().decode(imageDTO.getPieChartHours().substring(22).getBytes()));
        bytes.add(Base64.getDecoder().decode(imageDTO.getBarChartHours().substring(22).getBytes()));
        bytes.add(Base64.getDecoder().decode(imageDTO.getBarChartEnergy().substring(22).getBytes()));
        bytes.add(Base64.getDecoder().decode(imageDTO.getSplineChart().substring(22).getBytes()));

        Sheet sheet = wb.createSheet("Charts");
        CreationHelper helper = wb.getCreationHelper();
        Drawing drawing = sheet.createDrawingPatriarch();
        Row row = sheet.createRow(0);

        Cell cell = row.createCell(1);
        cell.setCellValue("From:");
        cell = row.createCell(2);
        cell.setCellValue(imageDTO.getStartDate());
        cell = row.createCell(4);
        cell.setCellValue("To:");
        cell = row.createCell(5);
        cell.setCellValue(imageDTO.getEndDate());
        cell = row.createCell(7);
        if (!"".equals(imageDTO.getUserName())) {
            cell.setCellValue("User:");
            cell = row.createCell(8);
            cell.setCellValue(imageDTO.getUserName());
        }

        int i = 0;
        for (byte[] element : bytes) {
            int pictureIdx = wb.addPicture(element, Workbook.PICTURE_TYPE_PNG);
            ClientAnchor anchor = null;
            switch (i) {
                case 0: {
                    anchor = setAnchorParams(helper, 1, 2, 2, 3);
                    break;
                }
                case 1: {
                    anchor = setAnchorParams(helper, 12, 13, 2, 3);
                    break;
                }
                case 2: {
                    anchor = setAnchorParams(helper, 1, 2, 26, 27);
                    break;
                }
                case 3: {
                    anchor = setAnchorParams(helper, 12, 13, 26, 27);
                    break;
                }
                case 4: {
                    anchor = setAnchorParams(helper, 12, 13, 48, 49);
                    break;
                }
                default: {
                    break;
                }
            }
            Picture pict = drawing.createPicture(anchor, pictureIdx);
            pict.resize();
            i++;
        }
        try (FileOutputStream fileOut = new FileOutputStream(pathToReport);) {
            wb.write(fileOut);
            wb.close();
        } catch (IOException e) {
            throw new ServiceException("write charts into file failed", e);
        }
    }

    public void writeStatistics(List<WorkLog> workLogs) {
        try (Workbook wb = new XSSFWorkbook();
             FileOutputStream fileOut = new FileOutputStream("D:\\statistic.xlsx")) {
            Sheet sheet = wb.createSheet("Statistic data");
            Row row;
            Cell cell;
            Font font = wb.createFont();
            font.setFontHeightInPoints((short) 10);
            font.setFontName("Arial");
            font.setBold(true);
            font.setItalic(false);

            CellStyle cellStyle = wb.createCellStyle();
            cellStyle.setFillBackgroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            cellStyle.setFont(font);
            sheet.setColumnWidth(1, 20 * 256);
            sheet.setColumnWidth(2, 15 * 256);
            sheet.setColumnWidth(3, 20 * 256);
            sheet.setColumnWidth(4, 18 * 256);
            sheet.setColumnWidth(5, 17 * 256);

            row = sheet.createRow(0);

            cell = row.createCell(1);
            cell.setCellValue("SolrDevice");
            cell.setCellStyle(cellStyle);
            cell = row.createCell(2);
            cell.setCellValue("Action");
            cell.setCellStyle(cellStyle);
            cell = row.createCell(3);
            cell.setCellValue("User");
            cell.setCellStyle(cellStyle);
            cell = row.createCell(4);
            cell.setCellValue("Date");
            cell.setCellStyle(cellStyle);
            cell = row.createCell(5);
            cell.setCellValue("Hours of work");
            cell.setCellStyle(cellStyle);
            cell = row.createCell(6);
            cell.setCellValue("Cost, Br");
            cell.setCellStyle(cellStyle);

            int currentRow = 1;
            for (WorkLog workLog : workLogs) {
                SimpleDateFormat format = new SimpleDateFormat("dd.MM.YYYY HH:MM");
                row = sheet.createRow(currentRow);
                cell = row.createCell(1);
                cell.setCellValue(workLog.getDevice().getName());
                cell = row.createCell(2);
                cell.setCellValue("Turned " + workLog.getAction());
                cell = row.createCell(3);
                cell.setCellValue(workLog.getUser().getName());
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

            wb.write(fileOut);

        } catch (IOException e) {
            throw new ServiceException("write workbook into statistic.xlsx failed", e);
        }
    }

    public ClientAnchor setAnchorParams(CreationHelper helper, int col1, int col2, int row1, int row2) {
        ClientAnchor anchor = helper.createClientAnchor();
        anchor.setCol1(col1);
        anchor.setRow1(row1);
        anchor.setCol2(col2);
        anchor.setRow2(row2);
        return anchor;
    }

    public byte[] getReport() {
        try (InputStream inputStream = new FileInputStream(new File("D:\\report.xlsx"))) {
            return IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            throw new ServiceException("get report failed", e);
        }
    }
}
