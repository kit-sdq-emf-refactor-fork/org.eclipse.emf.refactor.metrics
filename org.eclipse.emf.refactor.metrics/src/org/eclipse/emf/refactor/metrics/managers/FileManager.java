package org.eclipse.emf.refactor.metrics.managers;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileManager {	
	
	public static void copyFile(String target, String destination, String name) {
		File file = new File(destination);
		if (!file.exists()) {
			file.mkdirs();
		}
		File targetFile = new File(target);
		if (targetFile.exists()) {
			File newFile = new File(destination + name);
			try {
				newFile.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}			
			copy(target, destination + name);
		}
	}

	public static void copy(String filePath, String newFilePath) {
		try {
			BufferedInputStream is = new BufferedInputStream(
					new FileInputStream(filePath));
			new File(newFilePath).createNewFile();
			BufferedOutputStream os = new BufferedOutputStream(
					new FileOutputStream(newFilePath));
			int b;
			while ((b = is.read()) != -1) {
				os.write(b);
			}
			is.close();
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
