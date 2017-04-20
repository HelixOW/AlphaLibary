package de.alphahelix.alphalibary.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZIPUtil {

    public static void unzip(String zipPath, String outputFolder) {
        try {
            ZipFile zipFile = new ZipFile(zipPath);
            Enumeration<?> enu = zipFile.entries();

            File folder = new File(outputFolder);
            if (!folder.exists()) {
                folder.mkdir();
            }

            while (enu.hasMoreElements()) {
                ZipEntry zipEntry = (ZipEntry) enu.nextElement();

                String name = zipEntry.getName();

                File file = new File(outputFolder + File.separator + name);
                if (name.endsWith("/")) {
                    file.mkdirs();
                    continue;
                }

                File parent = file.getParentFile();
                if (parent != null) {
                    parent.mkdirs();
                }

                InputStream is = zipFile.getInputStream(zipEntry);
                FileOutputStream fos = new FileOutputStream(file);
                byte[] bytes = new byte[1024];
                int length;
                while ((length = is.read(bytes)) >= 0) {
                    fos.write(bytes, 0, length);
                }

                is.close();
                fos.close();
            }
            zipFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
