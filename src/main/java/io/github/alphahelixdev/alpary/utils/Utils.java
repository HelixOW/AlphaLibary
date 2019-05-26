package io.github.alphahelixdev.alpary.utils;

import io.github.whoisalphahelix.helix.annotations.Singleton;
import io.github.whoisalphahelix.helix.handlers.UtilHandler;
import io.github.whoisalphahelix.helix.utils.JsonUtil;
import io.github.whoisalphahelix.helix.utils.MathUtil;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
public class Utils extends UtilHandler {

	@Singleton
	private static Utils instance = new Utils();

	private final ArrayUtil arrayUtil = new ArrayUtil();
	private final LocationUtil locationUtil = new LocationUtil();
	private final MessageUtil messageUtil = new MessageUtil();
	private final NMSUtil nmsUtil = new NMSUtil();
	private final ScheduleUtil scheduleUtil = new ScheduleUtil();
	private final SerializationUtil serializationUtil = new SerializationUtil();
	private final SkinUtil skinUtil = new SkinUtil();
	private final StringUtil stringUtil = new StringUtil();
	private final SkullUtil skullUtil = new SkullUtil();

	public static ArrayUtil arrays() {
		return instance.getArrayUtil();
	}
	
	public static LocationUtil locations() {
		return instance.getLocationUtil();
	}
	
	public static MessageUtil messages() {
		return instance.getMessageUtil();
	}
	
	public static NMSUtil nms() {
		return instance.getNmsUtil();
	}
	
	public static ScheduleUtil schedules() {
		return instance.getScheduleUtil();
	}
	
	public static SerializationUtil serializations() {
		return instance.getSerializationUtil();
	}
	
	public static SkinUtil skins() {
		return instance.getSkinUtil();
	}
	
	public static StringUtil strings() {
		return instance.getStringUtil();
	}
	
	public static SkullUtil skulls() {
		return instance.getSkullUtil();
	}
	
	public static JsonUtil json() {
		return instance.getJsonUtil();
	}

	public static MathUtil math() {
		return UtilHandler.math();
	}

	public static Utils utils() {
		return instance;
	}

	public static void setInstance(Utils instance) {
		Utils.instance = instance;
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

	@Override
	public ArrayUtil getArrayUtil() {
		return this.arrayUtil;
	}

	@Override
	public StringUtil getStringUtil() {
		return this.stringUtil;
	}
}