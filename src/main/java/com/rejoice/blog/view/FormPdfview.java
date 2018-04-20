package com.rejoice.blog.view;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.URL;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.rejoice.blog.common.util.FreemarkerUtils;

/**
 * Created by Administrator on 2016/8/19 0019.
 */
public class FormPdfview extends AbstractITextPdfView {
	
	 private static final String imageFilePath = "D:b.jpg";
	
    @Override
    protected void buildPdfDocument(Map<String, Object> model,
                                    Document document, PdfWriter writer, HttpServletRequest request,
                                    HttpServletResponse response) throws Exception {
    	 response.setContentType("application/pdf");
         response.setContentType("application/pdf;charset=utf-8");
         model.put("host", "http://"+InetAddress.getLocalHost().getHostAddress()+":"+request.getLocalPort());
         //from ftl
         this.parseFromHTml(model, document, writer, request, response);
    }
    
    private static void parseFromHTml(Map<String, Object> model,
            Document document, PdfWriter writer, HttpServletRequest request,
            HttpServletResponse response) throws IOException{
    	document.addHeader("我是页眉", "页眉内容");
    	document.addAuthor("rejoice");
    	document.setPageCount(12);
    	document.addTitle("我是title");
    	String[] paths = request.getRequestURI().split("/");
    	String template = paths[paths.length-1];  
    	//download 
        //response.setHeader("Content-Disposition", "attachment; filename=\"simpleForm.pdf\"");
    	URL fileResource = FormPdfview.class.getResource("/templates/ftl/pdf");
    	model.put("contextPath", request.getContextPath());
        String html = FreemarkerUtils.loadFtlHtml(new File(fileResource.getFile()), template.substring(0,template.lastIndexOf("."))+".ftl", model);
        XMLWorkerHelper.getInstance().parseXHtml(writer, document, new StringReader(html));
    }


    /*private static final Font getChineseFont(float size) {
        Font FontChinese = null;
        try {
            BaseFont bfChinese = BaseFont.createFont("STSong-Light",
                    "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            FontChinese = new Font(bfChinese, size, Font.NORMAL);
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        return FontChinese;
    }*/
}
