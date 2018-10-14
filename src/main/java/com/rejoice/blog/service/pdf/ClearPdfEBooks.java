package com.rejoice.blog.service.pdf;

public class ClearPdfEBooks {
	public static void exec(String[] args) throws Exception {
		String path1 = "E:\\迅雷下载\\sharepdf";
		String path2 = "E:\\BaiduNetdiskDownload\\sharepdf";
	/*	new PdfService(path1,path1+"\\newpdf\\").execute();
		new PdfService(path2,path2+"\\newpdf\\").execute();*/
		new FileRenameService().rename(path1+"\\newpdf\\",path2+"\\newpdf\\");
	}
	

}