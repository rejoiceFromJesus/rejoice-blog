package com.rejoice.blog.view;

import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.view.document.AbstractXlsView;

/**
 * Created by aboullaite on 2017-02-23.
 */
public class ExcelView extends AbstractXlsView{

    @Override
    protected void buildExcelDocument(Map<String, Object> model,
                                      Workbook workbook,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {

    	 int rowCount = 1;
 		workbook = new HSSFWorkbook(new FileInputStream(ResourceUtils.getFile("classpath:static/avgrate.xls")));
 		   // change the file name
         response.setHeader("Content-Disposition", "attachment; filename=\"avgrate.xls\"");

/*         // create excel xls sheet
         Sheet sheet = workbook.getSheet("sheet1");
         sheet.setDefaultRowHeight((short)50);
         sheet.setDefaultColumnWidth(30);

         // create style for header cells
         CellStyle style = workbook.createCellStyle();
         Font font = workbook.createFont();
         font.setFontName("Arial");
         style.setFillForegroundColor(HSSFColor.RED.index);
         style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
         font.setBold(true);
         font.setColor(HSSFColor.WHITE.index);
         style.setFont(font);

         // create header row
         Row header = sheet.createRow(rowCount); 
         header.createCell(0).setCellValue("贷款日期");
         header.getCell(0).setCellStyle(style);
         header.createCell(1).setCellValue("10天");
         header.getCell(1).setCellStyle(style);
         header.createCell(2).setCellValue("1个月");
         header.getCell(2).setCellStyle(style);
         header.createCell(3).setCellValue("3个月");
         header.getCell(3).setCellStyle(style);
         header.createCell(4).setCellValue("6个月");
         header.getCell(4).setCellStyle(style);
         header.createCell(5).setCellValue("1年");
         header.getCell(5).setCellStyle(style);
         header.createCell(6).setCellValue("1年以上");
         header.getCell(6).setCellStyle(style);
         
         rowCount++;
         //render data
         AverageRate averageRate;
         List<AverageRate> averageRateList = (List<AverageRate>) model.get("averageRateList");
         boolean days10= false;
         boolean month1= false;
         boolean months3 = false;
         boolean months6= false;
         boolean year1= false;
         boolean years= false;
         String currentDate = "";
         Row row = null;
         for (int i = 0; i < averageRateList.size(); i++) {
         	averageRate = averageRateList.get(i);
         	if(StringUtils.isBlank(averageRate.getTerm()) ||StringUtils.isBlank(averageRate.getAverageRate()) || StringUtils.isBlank(averageRate.getDaiDate())){
         		continue;
         	}
         	if("22.00".equals(averageRate.getAverageRate())){
         		continue;
         	}
         	if(StringUtils.isBlank(currentDate)){
         		row = sheet.createRow(rowCount ++);
         		row.createCell(0).setCellValue(averageRate.getDaiDate());
         		currentDate = averageRate.getDaiDate();
         	}
         	if(!averageRate.getDaiDate().equals(currentDate) && StringUtils.isNotBlank(currentDate)){
         		currentDate = averageRate.getDaiDate();
         		if(!(days10 || month1 || months3 || months6 || year1 || years)){
         			rowCount--;
         			sheet.removeRow(row);
         		}
         		//new date
         		row = sheet.createRow(rowCount ++);
         		row.createCell(0).setCellValue(averageRate.getDaiDate());
         		days10= false;
     	        month1= false;
     	        months3 = false;
     	        months6= false;
     	        year1= false;
     	        years= false;
         	}
         	String term = averageRate.getTerm();
         	String avgRate = averageRate.getAverageRate();
         	if("10天".equals(term) || "十天".equals(term)){
         		row.createCell(1).setCellValue(avgRate);
         		days10 = true;
         	} else if("1个月".equals(term) || "一个月".equals(term)){
         		row.createCell(2).setCellValue(avgRate);
         		month1 = true;
         	} else if("3个月".equals(term) || "三个月".equals(term)){
         		row.createCell(3).setCellValue(avgRate);
         		months3 = true;
         	} else if("6个月".equals(term) || "六个月".equals(term)){
         		row.createCell(4).setCellValue(avgRate);
         		months6 = true;
         	} else if("1年".equals(term) || "一年".equals(term)){
         		row.createCell(5).setCellValue(avgRate);
         		year1 = true;
         	} else if("1年以上".equals(term) || "一年以上".equals(term)){
         		row.createCell(6).setCellValue(avgRate);
         		years = true;
         	}
         }*/
         OutputStream outputStream = response.getOutputStream();  
         workbook.write(outputStream);  
         outputStream.flush();  
         outputStream.close();  

    }

}
