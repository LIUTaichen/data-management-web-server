package com.dempseywood.webservice.equipmentstatus;

import com.dempseywood.model.Equipment;
import com.dempseywood.model.EquipmentStatus;
import com.dempseywood.model.Haul;
import com.dempseywood.repository.EquipmentRepository;
import com.dempseywood.repository.EquipmentStatusRepository;
import static  com.dempseywood.webservice.geofence.TruckStatus.*;

import com.dempseywood.webservice.geofence.TruckStatus;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Component
public class DailyExcelReportView extends AbstractXlsxView {


    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Sheet sheet = workbook.createSheet("Java Books");
        sheet.setDefaultColumnWidth(30);

        // create style for header cells
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Arial");
        style.setFillForegroundColor(HSSFColor.BLUE.index);
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        font.setColor(HSSFColor.WHITE.index);
        style.setFont(font);

        // create header row
        Row header = sheet.createRow(0);

        header.createCell(0).setCellValue("Book Title");
        header.getCell(0).setCellStyle(style);

        header.createCell(1).setCellValue("Author");
        header.getCell(1).setCellStyle(style);

        header.createCell(2).setCellValue("ISBN");
        header.getCell(2).setCellStyle(style);

        header.createCell(3).setCellValue("Published Date");
        header.getCell(3).setCellStyle(style);

        header.createCell(4).setCellValue("Price");
        header.getCell(4).setCellStyle(style);
    }




}
