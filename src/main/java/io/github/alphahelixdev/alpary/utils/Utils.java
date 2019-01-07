package io.github.alphahelixdev.alpary.utils;

import io.github.alphahelixdev.alpary.annotations.Singleton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Singleton
public class Utils {
	
	@Singleton
	private static Utils instance;
	
	private static final ArrayUtil ARRAYS = new ArrayUtil();
	private static final LocationUtil LOCATIONS = new LocationUtil();
	private static final MessageUtil MESSAGES = new MessageUtil();
	private static final NMSUtil NMS = new NMSUtil();
	private static final ScheduleUtil SCHEDULES = new ScheduleUtil();
	private static final SerializationUtil SERIALIZATIONS = new SerializationUtil();
	private static final SkinUtil SKINS = new SkinUtil();
	private static final StringUtil STRINGS = new StringUtil();
	private static final SkullUtil SKULLS = new SkullUtil();
	
	public static ArrayUtil arrays() {
		return ARRAYS;
	}
	
	public static LocationUtil locations() {
		return LOCATIONS;
	}
	
	public static MessageUtil messages() {
		return MESSAGES;
	}
	
	public static NMSUtil nms() {
		return NMS;
	}
	
	public static ScheduleUtil schedules() {
		return SCHEDULES;
	}
	
	public static SerializationUtil serializations() {
		return SERIALIZATIONS;
	}
	
	public static SkinUtil skins() {
		return SKINS;
	}
	
	public static StringUtil strings() {
		return STRINGS;
	}
	
	public static SkullUtil skulls() {
		return SKULLS;
	}
	
	public static Utils utils() {
		return instance;
	}
	
	public void unzip(String zipPath, String outputFolder) {
		try {
			ZipFile zipFile = new ZipFile(zipPath);
			Enumeration<?> enu = zipFile.entries();
			
			File folder = new File(outputFolder);
			if(!folder.exists()) {
				folder.mkdir();
			}
			
			while(enu.hasMoreElements()) {
				ZipEntry zipEntry = (ZipEntry) enu.nextElement();
				
				String name = zipEntry.getName();
				
				File file = new File(outputFolder + File.separator + name);
				if(name.endsWith("/")) {
					file.mkdirs();
					continue;
				}
				
				File parent = file.getParentFile();
				if(parent != null) {
					parent.mkdirs();
				}
				
				InputStream is = zipFile.getInputStream(zipEntry);
				FileOutputStream fos = new FileOutputStream(file);
				byte[] bytes = new byte[1024];
				int length;
				while((length = is.read(bytes)) >= 0) {
					fos.write(bytes, 0, length);
				}
				
				is.close();
				fos.close();
			}
			zipFile.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}