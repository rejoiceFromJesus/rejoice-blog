package com.rejoice.blog.service.pdf;

import java.io.File;
import java.util.stream.Stream;
public class FileRenameService {
	public static void rename(String ...paths) {
		for (String path : paths) {
			File folder = new File(path);
			if(folder == null) {
				return;
			}
			Stream.of(folder.listFiles()).forEach(file -> {
				String oldPath = file.getAbsolutePath();
				if(!oldPath.endsWith(".pdf") && !oldPath.endsWith(".azw3") && !oldPath.endsWith(".mobi") && !oldPath.endsWith(".epub")) {
					return;
				}
				String newPath = oldPath.replace("[www.java1234.com]", "").replace("@www.java1234.com", "").replaceAll("\\(ED2000.COM\\)", "")
					;
				File newFile = new File(newPath);
				System.out.println("new file:"+newPath);
				file.renameTo(newFile);
			});
		}
	}

}