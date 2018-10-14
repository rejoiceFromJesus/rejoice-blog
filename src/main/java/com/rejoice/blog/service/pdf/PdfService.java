package com.rejoice.blog.service.pdf;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.action.PdfAction;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Link;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

@Service
public class PdfService {

	private Logger LOGGER = LoggerFactory.getLogger(PdfService.class);

	@Value("${blog.resource.dir}")
	private String resourceDir;

	@Value("${blog.upload.images.dir}")
	private String imgDir;

	@Autowired
	ResourceLoader resourceLoader;

	public void addLink(String path) throws IOException {
		String tempDir = resourceDir + "/temp";
		String tempAbsolutePath = resourceLoader.getResource(tempDir).getFile().getAbsolutePath().replace("\\", "/");
		Resource resource = resourceLoader.getResource(path);
		if (!resource.exists()) {
			return;
		}
		String srcAbsolutePath = resourceLoader.getResource(path).getFile().getAbsolutePath().replace("\\", "/");
		if (!path.endsWith(".pdf")) {
			if (path.endsWith(".azw3") || path.endsWith(".mobi") || path.endsWith(".epub")) {
			}
			return;
		}
		try {
			String newPath = this.manipulatePdf(srcAbsolutePath, tempAbsolutePath);
			Files.delete(Paths.get(srcAbsolutePath));
			Files.move(Paths.get(newPath), Paths.get(srcAbsolutePath));
		} catch (Exception e) {
			LOGGER.warn("add link failed:", e);
		} 
	}

	public void screenShot(String filePath) throws IOException {
		RandomAccessFile raf = null;
		try {
			String absoluteImgDir = resourceLoader.getResource(imgDir).getFile().getAbsolutePath();
			File file = resourceLoader.getResource(filePath).getFile();
			if (!file.exists()) {
				System.err.println("路径[" + filePath + "]对应的pdf文件不存在!");
				return;
			}
			raf = new RandomAccessFile(file, "r");
			FileChannel channel = raf.getChannel();
			ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
			PDFFile pdffile = new PDFFile(buf);
			PDFPage page = pdffile.getPage(1);
			// get the width and height for the doc at the default zoom
			Rectangle rect = new Rectangle(0, 0, (int) page.getBBox().getWidth(), (int) page.getBBox().getHeight());
			// generate the image
			Image img = page.getImage(rect.width, rect.height, // width &
					rect, // clip rect
					null, // null for the ImageObserver
					true, // fill background with white
					true // block until drawing is done
			);
			BufferedImage tag = new BufferedImage(rect.width, rect.height, BufferedImage.TYPE_INT_RGB);
			tag.getGraphics().drawImage(img, 0, 0, rect.width, rect.height, null);
			/* FileOutputStream out = new FileOutputStream(target+file.getName()+".jpg"); */
			ImageIO.write(tag, "jpg", new File(absoluteImgDir + "/" + file.getName() + ".jpg"));
			channel.close();
			raf.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String manipulatePdf(String src, String dest) throws Exception {
		PdfReader pdfReader = new PdfReader(src);
		String target = dest + src.substring(src.lastIndexOf("/"));
		PdfDocument pdfDoc = new PdfDocument(pdfReader, new PdfWriter(target));
		Document document = new Document(pdfDoc);
		com.itextpdf.kernel.geom.Rectangle pageSize;
		PdfCanvas canvas;
		int n = pdfDoc.getNumberOfPages();
		Link link = new Link("http://www.rejoiceblog.com/", PdfAction.createURI("http://www.rejoiceblog.com/"));
		Paragraph header = new Paragraph().add(link).setFont(PdfFontFactory.createFont(FontConstants.HELVETICA))
				.setFontSize(14).setFontColor(Color.RED);
		for (int i = 1; i <= n; i++) {
			PdfPage page = pdfDoc.getPage(i);
			pageSize = page.getPageSize();
			canvas = new PdfCanvas(page);

			float x = pdfDoc.getPage(i).getPageSize().getWidth() / 2 - 100;
			float y = pdfDoc.getPage(i).getPageSize().getTop() - 20;
			document.showTextAligned(header.setFontColor(Color.RED), x, y, i, TextAlignment.LEFT,
					VerticalAlignment.BOTTOM, 0);

			/*
			 * x = pdfDoc.getPage(i).getPageSize().getWidth() / 2-100; y =
			 * pdfDoc.getPage(i).getPageSize().getBottom() + 30;
			 * document.showTextAligned(header.setFontColor(Color.RED), x, y, i,
			 * TextAlignment.LEFT, VerticalAlignment.BOTTOM, 0);
			 */
		

		}
		document.close();
		pdfDoc.close();
		pdfReader.close();
		return target;
	}
}