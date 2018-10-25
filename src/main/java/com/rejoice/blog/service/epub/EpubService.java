package com.rejoice.blog.service.epub;

import java.io.File;
import java.io.FileInputStream;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubReader;

@Component
public class EpubService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EpubService.class);
	
	@Autowired
	private ResourceLoader resourceLoader;

	@Value("${blog.resource.dir}")
	private String resourceDir;

	@Value("${blog.upload.images.dir}")
	private String imgDir;
	
	public void screenShot(String filePath) {

		try {
			String absoluteImgDir = resourceLoader.getResource(imgDir).getFile().getAbsolutePath();
			File file = resourceLoader.getResource(filePath).getFile();
			if (!file.exists()) {
				System.err.println("路径[" + filePath + "]对应的文件不存在!");
				return;
			}
			EpubReader epubReader = new EpubReader();
			Book book = epubReader.readEpub(new FileInputStream(file));
			// 封面
			Resource resource = book.getCoverImage();
			byte[] p = resource.getData();
			// 将图片输出
			String newFilename = absoluteImgDir + "/" + file.getName() + ".jpg";
			FileImageOutputStream imgout = new FileImageOutputStream(new File(newFilename));
			imgout.write(p, 0, p.length);
			imgout.close();
		} catch (Exception e) {
			LOGGER.warn("screen shot epub failed:",e);
		}
	}
}
