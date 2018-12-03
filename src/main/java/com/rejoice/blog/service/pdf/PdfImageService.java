package com.rejoice.blog.service.pdf;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.util.GraphicsRenderingHints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PdfImageService extends PdfService{
	@Value("${blog.resource.dir}")
	private String resourceDir;

	@Value("${blog.upload.images.dir}")
	private String imgDir;
	
	public static final Logger LOGGER = LoggerFactory.getLogger(PdfImageService.class);
	
	public void screenShot(String filePath) throws IOException {
		try {
			String absoluteImgDir = resourceLoader.getResource(imgDir).getFile().getAbsolutePath();
			File file = resourceLoader.getResource(filePath).getFile();
			if (!file.exists()) {
				System.err.println("路径[" + filePath + "]对应的pdf文件不存在!");
				return;
			}
			 Document document = null;
            float rotation = 0f;
            //缩略图显示倍数，1表示不缩放，0.5表示缩小到50%
            float zoom = 1f;
            document = new Document();
            document.setFile(file.getAbsolutePath());
            BufferedImage image = (BufferedImage)document.getPageImage(0, GraphicsRenderingHints.SCREEN, 
                        Page.BOUNDARY_CROPBOX, rotation, zoom);
            Iterator iter = ImageIO.getImageWritersBySuffix("jpg");
            ImageWriter writer = (ImageWriter)iter.next();
            FileOutputStream out = new FileOutputStream(new File(absoluteImgDir+"/"+file.getName()+".jpg"));
            ImageOutputStream outImage = ImageIO.createImageOutputStream(out);
            writer.setOutput(outImage);
            writer.write(new IIOImage(image, null, null));
		        
		} catch (Exception e) {
			LOGGER.warn("pdf screenshot failed");;
		}
	}
}
