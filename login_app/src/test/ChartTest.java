import dto.WorkLogResult;
import entities.WorkLog;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.charts.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.openxmlformats.schemas.drawingml.x2006.chart.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChartTest {
    @Test
    public void testExcel() throws Exception {
        try {

            Workbook wb = new XSSFWorkbook();
            Sheet sheet = wb.createSheet("Sheet 1");
            InputStream inputStream = new FileInputStream("D:\\chart.png");
            byte[] bytes = IOUtils.toByteArray(inputStream);
            int pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
            inputStream.close();
            CreationHelper helper = wb.getCreationHelper();
            Drawing drawing = sheet.createDrawingPatriarch();

            ClientAnchor anchor = helper.createClientAnchor();

            anchor.setCol1(1); //Column B
            anchor.setRow1(2); //Row 3
            anchor.setCol2(2); //Column C
            anchor.setRow2(3); //Row 4

            Picture pict = drawing.createPicture(anchor, pictureIdx);

            // pict.resize();
            //Create the Cell B3
            //Cell cell = sheet.createRow(2).createCell(1);


            //set width to n character widths = count characters * 256
//            double widthUnits = pict.getImageDimension().getWidth();
//            sheet.setColumnWidth(1, (int) widthUnits);
//
//            //set height to n points in twips = n * 20
//            double heightUnits = pict.getImageDimension().getHeight();
//            cell.getRow().setHeight((short) heightUnits);

            //Write the Excel file
            FileOutputStream fileOut = null;
            fileOut = new FileOutputStream("myFile.xlsx");
            wb.write(fileOut);
            fileOut.close();

        } catch (IOException ioex) {
        }
    }

    @Test
    public void testDecode() throws Exception {

        byte[] bytesFromFile = new byte[0];
        bytesFromFile = Files.readAllBytes(Paths.get("D:\\bytes.txt"));

    }
}
